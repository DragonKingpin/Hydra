package com.acorn.redqueen.service.registry.grpc.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.grpc.server.GrpcAppointServer;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransport;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransportType;
import com.pinecone.hydra.service.registry.server.transport.entity.ServiceControlTransportInspection;
import com.pinecone.hydra.service.registry.server.transport.entity.ServiceTransportConnection;
import com.acorn.redqueen.service.registry.grpc.server.lifecycle.GrpcServiceControlClientile;
import com.acorn.redqueen.service.registry.grpc.server.lifecycle.GrpcServiceLifecycleTransformer;
import com.acorn.redqueen.service.registry.grpc.server.lifecycle.GrpcServiceLifecycleService;
import com.acorn.redqueen.service.registry.grpc.server.meta.GrpcServiceMetaService;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.Heartbeat;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.RegisterServiceCommand;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame;

import io.grpc.ServerInterceptors;

public class GrpcServiceControlTransport implements ServiceControlTransport {

    protected Logger mLogger = LoggerFactory.getLogger( this.getClass() );

    protected ServiceManager mServiceManager;

    protected GuidAllocator mGuidAllocator;

    protected GrpcAppointServer mGrpcAppointServer;

    protected Map<Long, GrpcServiceControlClientile> mClientileMap;

    protected GrpcServiceLifecycleTransformer mFrameMapper;

    protected ScheduledExecutorService mHeartbeatGuardian;

    public GrpcServiceControlTransport( ServiceManager serviceManager, GrpcAppointServer grpcAppointServer ) {
        this.mServiceManager = serviceManager;
        this.mGuidAllocator = serviceManager.getServicesInstrument().getGuidAllocator();
        this.mGrpcAppointServer = grpcAppointServer;
        this.mClientileMap = new ConcurrentHashMap<>();
        this.mFrameMapper = new GrpcServiceLifecycleTransformer( this.mGuidAllocator );
    }

    public ServiceManager serviceManager() {
        return this.mServiceManager;
    }

    public GrpcAppointServer grpcAppointServer() {
        return this.mGrpcAppointServer;
    }

    public GrpcServiceLifecycleTransformer frameMapper() {
        return this.mFrameMapper;
    }

    public String nextGuidString() {
        return this.mGuidAllocator.nextGUID().toString();
    }

    public void bindClientSession( GrpcServiceControlSession session ) {
        GrpcServiceControlClientile clientile = this.mClientileMap.computeIfAbsent(
                session.clientId(),
                clientId -> new GrpcServiceControlClientile( session.clientId() )
        );
        clientile.attachSession( session );
        this.mServiceManager.serviceEventHooker().afterNewConnectionInbound(
                session.clientId(),
                session.sessionGuid(),
                session,
                this,
                () -> clientile
        );
        this.mServiceManager.transportRegistry().bindClient( session.clientId(), this );
        this.mLogger.info(
                "[GrpcServiceControl] [SessionBind] (ClientId: `{}`, Session: `{}`, RemoteAddress: `{}`) <Done>",
                session.clientId(),
                session.sessionGuid(),
                session.remoteAddress()
        );
    }

    public void detachClientSession( GrpcServiceControlSession session ) {
        if ( session == null ) {
            return;
        }

        GrpcServiceControlClientile clientile = this.mClientileMap.get( session.clientId() );
        if ( clientile != null ) {
            clientile.detachSession( session );
            if ( !clientile.isActive() ) {
                this.mClientileMap.remove( session.clientId() );
            }
        }
        this.mServiceManager.serviceEventHooker().afterConnectionDetach(
                session.clientId(),
                session.sessionGuid(),
                session
        );
        session.close();
        this.mLogger.info(
                "[GrpcServiceControl] [SessionDetach] (ClientId: `{}`, Session: `{}`, Instance: `{}`) <Done>",
                session.clientId(),
                session.sessionGuid(),
                session.instanceGuid()
        );
    }

    public String registerService( GrpcServiceControlSession session, ServiceControlFrame frame ) throws ClientServiceRegisterException {
        RegisterServiceCommand command = frame.getRegisterService();
        RegisterServiceDTO dto = new RegisterServiceDTO();
        dto.setClientId( session.clientId() );
        dto.setServiceId( command.getServiceGuid() );
        dto.setDeployId( command.getDeployGuid() );
        dto.setInstanceGuid( command.getInstanceGuid() );
        dto.setTransportType( ServiceControlTransportType.Grpc.name() );
        dto.setEndpointProtocol( command.getEndpointProtocol() );
        dto.setEndpointHost( command.getEndpointHost() );
        if ( command.getEndpointPort() > 0 ) {
            dto.setEndpointPort( command.getEndpointPort() );
        }
        dto.setEndpointPath( command.getEndpointPath() );
        dto.setEndpointAddress( command.getEndpointAddress() );
        dto.setVersion( command.getVersion() );
        dto.setZone( command.getZone() );
        if ( command.getWeight() > 0 ) {
            dto.setWeight( command.getWeight() );
        }
        dto.setMetadataJson( command.getMetadataJson() );
        dto.setRuntimeNodeId( command.getRuntimeNodeId() );
        dto.setRuntimeNodeAlias( command.getRuntimeNodeAlias() );
        dto.setRuntimeNodeMetadataJson( command.getRuntimeNodeMetadataJson() );
        String szInstanceGuid = this.mServiceManager.serviceLifecycleService().registerService( dto );
        session.bindInstance( szInstanceGuid );
        return szInstanceGuid;
    }

    public void deregisterService( GrpcServiceControlSession session, String szInstanceGuid ) {
        if ( szInstanceGuid == null || szInstanceGuid.isBlank() ) {
            return;
        }
        this.mServiceManager.serviceLifecycleService().deregisterServiceByInstanceId( szInstanceGuid );
    }

    public void heartbeat( GrpcServiceControlSession session, Heartbeat heartbeat ) {
        session.touchHeartbeat();
        this.mLogger.debug(
                "[GrpcServiceControl] [Heartbeat] (ClientId: `{}`, Session: `{}`, Instance: `{}`, Status: `{}`) <Arrived>",
                session.clientId(),
                session.sessionGuid(),
                heartbeat.getInstanceGuid(),
                heartbeat.getStatus()
        );
    }

    @Override
    public ServiceControlTransportType transportType() {
        return ServiceControlTransportType.Grpc;
    }

    @Override
    public boolean containsClient( long nClientId ) {
        GrpcServiceControlClientile clientile = this.mClientileMap.get( nClientId );
        return clientile != null && clientile.isActive();
    }

    @Override
    public Collection<ServiceTransportConnection> queryClientConnections( long nClientId ) {
        List<ServiceTransportConnection> connections = new ArrayList<>();
        GrpcServiceControlClientile clientile = this.mClientileMap.get( nClientId );
        if ( clientile == null ) {
            return connections;
        }

        for ( GrpcServiceControlSession session : clientile.sessions() ) {
            ServiceTransportConnection connection = new ServiceTransportConnection();
            connection.setType( "Session" );
            connection.setIdentity( session.sessionGuid() );
            connection.setRemoteAddress( session.remoteAddress() );
            connection.setStatus( session.isActive() ? "ACTIVE" : "INACTIVE" );
            connection.setActive( session.isActive() );
            connections.add( connection );
        }
        return connections;
    }

    @Override
    public int queryConnectedClientCount() {
        int nCount = 0;
        for ( GrpcServiceControlClientile clientile : this.mClientileMap.values() ) {
            if ( clientile.isActive() ) {
                nCount++;
            }
        }
        return nCount;
    }

    @Override
    public int queryRegisteredControllerCount() {
        return this.mGrpcAppointServer == null ? 0 : 2;
    }

    @Override
    public String queryControllerSummary() {
        return "GrpcServiceLifecycleService, GrpcServiceMetaService";
    }

    @Override
    public ServiceControlTransportInspection inspectTransport() {
        ServiceControlTransportInspection inspection = ServiceControlTransport.super.inspectTransport();
        inspection.setRouteSource( this );
        inspection.setEndpointSource( this.mGrpcAppointServer == null ? this : this.mGrpcAppointServer );
        return inspection;
    }

    @Override
    public void shutdownClientService( long nClientId, GUID instanceGuid, String szReason ) throws ServiceControlRPCException {
        GrpcServiceControlClientile clientile = this.mClientileMap.get( nClientId );
        if ( clientile == null || !clientile.isActive() ) {
            throw new ServiceControlRPCException( "gRPC service client is not connected, clientId => `" + nClientId + "`." );
        }

        GrpcServiceControlSession session = clientile.activeSession();
        if ( session == null ) {
            throw new ServiceControlRPCException( "gRPC service client has no active session, clientId => `" + nClientId + "`." );
        }

        session.send( this.mFrameMapper.shutdownServiceFrame( session, instanceGuid, szReason ) );
    }

    @Override
    public void registerController( Object controller ) throws ServiceControlRPCException {
        this.mLogger.info( "[GrpcServiceControl] [ControllerRegisterSkipped] gRPC uses generated service. <Pass>" );
    }

    @Override
    public void compileIface( Class<?> ifaceClass, boolean bAsIface ) throws ServiceControlRPCException {
        this.mLogger.info( "[GrpcServiceControl] [IfaceCompileSkipped] gRPC uses proto generated service. <Pass>" );
    }

    @Override
    public void startService() throws ServiceControlRPCException {
        if ( this.mGrpcAppointServer == null ) {
            throw new GrpcServiceControlException( "GrpcAppointServer is required." );
        }
        if ( !this.mGrpcAppointServer.isShutdown() ) {
            this.mLogger.info( "[GrpcServiceControl] gRPC appoint server has already started. <Pass>" );
            return;
        }

        try {
            this.mGrpcAppointServer.serverBuilder().addService(
                    ServerInterceptors.intercept(
                            new GrpcServiceLifecycleService( this ),
                            new GrpcRemoteAddressServerInterceptor()
                    )
            );
            this.mGrpcAppointServer.serverBuilder().addService(
                    ServerInterceptors.intercept(
                            new GrpcServiceMetaService( this.mServiceManager ),
                            new GrpcRemoteAddressServerInterceptor()
                    )
            );
            this.mGrpcAppointServer.execute();
            this.startHeartbeatGuardian();
            this.mLogger.info( "[GrpcServiceControl] (Port: `{}`) <Started>", this.mGrpcAppointServer.getConfig().getPort() );
        }
        catch ( Exception e ) {
            throw new GrpcServiceControlException( e );
        }
    }

    @Override
    public void terminateService() throws IllegalStateException {
        this.stopHeartbeatGuardian();
        for ( GrpcServiceControlClientile clientile : this.mClientileMap.values() ) {
            for ( GrpcServiceControlSession session : clientile.sessions() ) {
                this.detachClientSession( session );
            }
        }
        if ( this.mGrpcAppointServer != null ) {
            this.mGrpcAppointServer.shutdown();
        }
    }

    @Override
    public boolean isStarted() {
        return this.mGrpcAppointServer != null && !this.mGrpcAppointServer.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return this.mGrpcAppointServer == null || this.mGrpcAppointServer.isTerminated();
    }

    protected void startHeartbeatGuardian() {
        if ( !this.mGrpcAppointServer.getConfig().isEnableHeartbeat() ) {
            return;
        }
        long nHeartbeatIntervalMillis = this.mGrpcAppointServer.getConfig().getHeartbeatIntervalMillis();
        if ( nHeartbeatIntervalMillis <= 0 || this.mHeartbeatGuardian != null ) {
            return;
        }
        this.mHeartbeatGuardian = Executors.newSingleThreadScheduledExecutor( runnable -> {
            Thread thread = new Thread( runnable, "redqueen-grpc-service-heartbeat-guardian" );
            thread.setDaemon( true );
            return thread;
        } );
        this.mHeartbeatGuardian.scheduleAtFixedRate(
                this::evictSilentSessions,
                nHeartbeatIntervalMillis,
                nHeartbeatIntervalMillis,
                TimeUnit.MILLISECONDS
        );
    }

    protected void stopHeartbeatGuardian() {
        if ( this.mHeartbeatGuardian == null ) {
            return;
        }
        this.mHeartbeatGuardian.shutdownNow();
        this.mHeartbeatGuardian = null;
    }

    protected void evictSilentSessions() {
        long nNow = System.currentTimeMillis();
        long nIdleTimeoutMillis = this.controlIdleTimeoutMillis();
        for ( GrpcServiceControlClientile clientile : this.mClientileMap.values() ) {
            for ( GrpcServiceControlSession session : clientile.sessions() ) {
                if ( !session.isActive() ) {
                    continue;
                }
                long nSilentMillis = nNow - session.lastActiveTimeMillis();
                if ( nSilentMillis <= nIdleTimeoutMillis ) {
                    continue;
                }
                this.mLogger.warn(
                        "[GrpcServiceControl] [HeartbeatTimeout] (ClientId: `{}`, Session: `{}`, SilentMillis: `{}`) <Detach>",
                        session.clientId(),
                        session.sessionGuid(),
                        nSilentMillis
                );
                session.closeByServer( "REDQUEEN_GRPC_HEARTBEAT_TIMEOUT" );
                this.detachClientSession( session );
            }
        }
    }

    protected long controlIdleTimeoutMillis() {
        long nHeartbeatIntervalMillis = this.mGrpcAppointServer.getConfig().getHeartbeatIntervalMillis();
        long nKeepAliveTimeoutMillis = this.mGrpcAppointServer.getConfig().getKeepAliveTimeoutSeconds() * 1000L;
        return nHeartbeatIntervalMillis * 3L + nKeepAliveTimeoutMillis;
    }
}



