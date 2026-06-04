package com.acorn.skynet.device.grpc.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.acorn.skynet.device.grpc.server.meta.GrpcDeviceMetaService;
import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame;
import com.acorn.skynet.device.grpc.transformer.GrpcDeviceLifecycleTransformer;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;
import com.pinecone.hydra.device.registry.DeviceControlRPCException;
import com.pinecone.hydra.device.registry.instruction.DeviceDeregisterInstruction;
import com.pinecone.hydra.device.registry.instruction.DeviceRegisterInstruction;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.device.registry.server.connection.DeviceConnection;
import com.pinecone.hydra.device.registry.server.transport.DeviceControlTransport;
import com.pinecone.hydra.device.registry.server.transport.DeviceControlTransportType;
import com.pinecone.hydra.grpc.server.GrpcAppointServer;

import io.grpc.ServerInterceptors;

public class GrpcDeviceLifecycleTransport implements DeviceControlTransport {

    protected final GrpcAppointServer grpcAppointServer;

    protected final Map<Long, GrpcDeviceLifecycleClientile> clientileMap;

    protected DeviceManager deviceManager;

    protected GuidAllocator guidAllocator;

    protected GrpcDeviceLifecycleTransformer transformer;

    public GrpcDeviceLifecycleTransport( GrpcAppointServer grpcAppointServer ) {
        this.grpcAppointServer = grpcAppointServer;
        this.clientileMap = new ConcurrentHashMap<>();
    }

    public GrpcDeviceLifecycleTransport( DeviceManager deviceManager, GrpcAppointServer grpcAppointServer ) {
        this( grpcAppointServer );
        this.hookDeviceManager( deviceManager );
    }

    @Override
    public DeviceControlTransport hookDeviceManager( DeviceManager deviceManager ) {
        if ( this.deviceManager != null ) {
            throw new IllegalStateException( "Device manager has already hooked." );
        }

        this.deviceManager = deviceManager;
        this.guidAllocator = deviceManager.getDeviceInstrument().getGuidAllocator();
        this.transformer = new GrpcDeviceLifecycleTransformer( this.guidAllocator );
        this.deviceManager.getLogger().info(
                "GrpcDeviceLifecycleTransport[{}] has been hooked to device manager.",
                this.getName()
        );
        return this;
    }

    public GrpcDeviceLifecycleTransformer transformer() {
        return this.transformer;
    }

    public String nextGuidString() {
        return this.guidAllocator.nextGUID().toString();
    }

    public void bindClientSession( GrpcDeviceLifecycleSession session ) {
        GrpcDeviceLifecycleClientile clientile = this.clientileMap.computeIfAbsent(
                session.clientId(),
                clientId -> new GrpcDeviceLifecycleClientile( session.clientId() )
        );
        clientile.attachSession( session );
        this.deviceManager.getLogger().info(
                "[GrpcDeviceLifecycle] [SessionBind] (ClientId: `{}`, Session: `{}`, RemoteAddress: `{}`) <Done>",
                session.clientId(),
                session.sessionGuid(),
                session.remoteAddress()
        );
    }

    public void detachClientSession( GrpcDeviceLifecycleSession session ) {
        if ( session == null ) {
            return;
        }

        GrpcDeviceLifecycleClientile clientile = this.clientileMap.get( session.clientId() );
        if ( clientile != null ) {
            clientile.detachSession( session );
            if ( !clientile.isActive() ) {
                this.clientileMap.remove( session.clientId() );
            }
        }
        if ( this.deviceManager != null && session.clientId() > 0 ) {
            this.deviceManager.deviceRuntimeService().detachConnectionByClientId(
                    session.clientId(),
                    "gRPC lifecycle stream detached."
            );
        }
        session.close();
        this.deviceManager.getLogger().info(
                "[GrpcDeviceLifecycle] [SessionDetach] (ClientId: `{}`, Session: `{}`, Instance: `{}`) <Done>",
                session.clientId(),
                session.sessionGuid(),
                session.instanceGuid()
        );
    }

    public DeviceInstanceEntry registerDevice( GrpcDeviceLifecycleSession session, DeviceLifecycleFrame frame ) {
        DeviceRegisterInstruction instruction = this.transformer.toRegisterInstruction( frame.getRegisterDevice() );
        if ( instruction.getClientId() == null && session.clientId() > 0 ) {
            instruction.setClientId( session.clientId() );
        }

        DeviceInstanceEntry instance = this.deviceManager.deviceRuntimeService().registerDevice(
                this.createConnection( session ),
                instruction
        );
        session.bindInstance( instance.getInstanceGuid().toString() );
        return instance;
    }

    public void deregisterDevice( GrpcDeviceLifecycleSession session, DeviceLifecycleFrame frame ) {
        DeviceDeregisterInstruction instruction = this.transformer.toDeregisterInstruction( frame.getDeregisterDevice() );
        this.deviceManager.deviceRuntimeService().deregisterDevice( this.createConnection( session ), instruction );
    }

    protected DeviceConnection createConnection( GrpcDeviceLifecycleSession session ) {
        DeviceConnection connection = new DeviceConnection();
        connection.setConnectionId( session.sessionGuid() );
        connection.setSessionGuid( this.guidAllocator.parse( session.sessionGuid() ) );
        connection.setTransportType( DeviceControlTransportType.Grpc.getName() );
        connection.setRemoteAddress( session.remoteAddress() );
        connection.setConnectedTime( System.currentTimeMillis() );
        return connection;
    }

    @Override
    public Long getTransportId() {
        return this.grpcAppointServer.getMessageNodeId();
    }

    @Override
    public String getName() {
        return this.grpcAppointServer.getName();
    }

    @Override
    public PatriarchalConfig getConfig() {
        return this.grpcAppointServer.getConfig();
    }

    @Override
    public void execute() throws Exception {
        if ( this.grpcAppointServer == null ) {
            throw new IllegalStateException( "GrpcAppointServer is required." );
        }
        if ( !this.grpcAppointServer.isShutdown() ) {
            this.deviceManager.getLogger().info( "[GrpcDeviceLifecycle] gRPC appoint server has already started. <Pass>" );
            return;
        }

        this.grpcAppointServer.serverBuilder().addService(
                ServerInterceptors.intercept(
                        new GrpcDeviceLifecycleService( this ),
                        new GrpcRemoteAddressServerInterceptor()
                )
        );
        this.grpcAppointServer.serverBuilder().addService(
                ServerInterceptors.intercept(
                        new GrpcDeviceMetaService( this.deviceManager ),
                        new GrpcRemoteAddressServerInterceptor()
                )
        );
        this.grpcAppointServer.execute();
        this.deviceManager.getLogger().info(
                "[GrpcDeviceLifecycle] (Port: `{}`) <Started>",
                this.grpcAppointServer.getConfig().getPort()
        );
    }

    @Override
    public boolean isStarted() {
        return this.grpcAppointServer != null && !this.grpcAppointServer.isShutdown();
    }

    @Override
    public boolean containsClient( long clientId ) {
        GrpcDeviceLifecycleClientile clientile = this.clientileMap.get( clientId );
        return clientile != null && clientile.isActive();
    }

    @Override
    public void shutdownClientDevice( long clientId, GUID instanceGuid, String reason ) throws DeviceControlRPCException {
        GrpcDeviceLifecycleClientile clientile = this.clientileMap.get( clientId );
        if ( clientile == null || !clientile.isActive() ) {
            throw new DeviceControlRPCException( "gRPC device client is not connected, clientId => `" + clientId + "`." );
        }

        GrpcDeviceLifecycleSession session = clientile.activeSession();
        if ( session == null ) {
            throw new DeviceControlRPCException( "gRPC device client has no active session, clientId => `" + clientId + "`." );
        }

        session.send( this.transformer.shutdownDeviceFrame( session, instanceGuid, reason ) );
    }

    @Override
    public void close() {
        for ( GrpcDeviceLifecycleClientile clientile : this.clientileMap.values() ) {
            for ( GrpcDeviceLifecycleSession session : clientile.sessions() ) {
                session.closeByServer( "GRPC_DEVICE_LIFECYCLE_TRANSPORT_CLOSED" );
                this.detachClientSession( session );
            }
        }
        if ( this.grpcAppointServer != null ) {
            this.grpcAppointServer.shutdown();
        }
    }
}
