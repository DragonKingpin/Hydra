package com.acorn.skynet.device.husky.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.acorn.skynet.device.husky.protocol.PassiveDeviceManipulatedIface;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.registry.DeviceControlRPCException;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.device.registry.server.transport.DeviceControlTransport;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.ChannelHandleException;
import com.pinecone.hydra.umc.msg.ChannelPool;
import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umc.msg.UMCChannel;
import com.pinecone.hydra.umc.msg.event.ChannelEventHandler;
import com.pinecone.hydra.umc.msg.event.ChannelInactiveHandler;
import com.pinecone.hydra.umc.wolf.server.UlfServer;

public class HuskyDeviceControlTransport implements DeviceControlTransport {

    protected final DuplexAppointServer appointServer;

    protected DeviceManager deviceManager;

    protected boolean eventHandlersRegistered;

    protected final ConcurrentMap<Long, HuskyDeviceClientile> clientiles;

    public HuskyDeviceControlTransport( DuplexAppointServer appointServer ) {
        this.appointServer = appointServer;
        this.clientiles = new ConcurrentHashMap<>();
    }

    @Override
    public DeviceControlTransport hookDeviceManager( DeviceManager deviceManager ) {
        if ( this.deviceManager != null ) {
            throw new IllegalStateException( "Device manager has already hooked." );
        }

        this.deviceManager = deviceManager;
        this.appointServer.registerController( new HuskyDeviceControlController( deviceManager ) );
        this.appointServer.registerController( new HuskyDeviceLifecycleController( deviceManager ) );
        this.appointServer.registerController( new HuskyDeviceMetaController( deviceManager ) );
        this.appointServer.registerController( new HuskyDeviceTopologyController( deviceManager ) );
        this.appointServer.compile( PassiveDeviceManipulatedIface.class, false );
        this.registerUlfServerEventHandlers();
        this.deviceManager.getLogger().info( "HuskyDeviceControlTransport[{}] has been hooked to device manager.", this.getName() );
        return this;
    }

    protected void registerUlfServerEventHandlers() {
        if ( this.eventHandlersRegistered ) {
            return;
        }

        MessageNode messageNode = this.appointServer.getMessageNode();
        if ( !( messageNode instanceof UlfServer ) ) {
            return;
        }

        UlfServer ulfServer = (UlfServer) messageNode;
        ulfServer.registerDataArrivedEventHandlers( new ChannelEventHandler() {
            @Override
            public void afterEventTriggered( ChannelControlBlock block, Object context ) {
                if ( block == null || block.getChannel() == null ) {
                    return;
                }

                UMCChannel channel = block.getChannel();
                long clientId = channel.getIdentityID();
                Object channelId = channel.getChannelID();
                if ( clientId <= 0 || channelId == null ) {
                    return;
                }

                synchronized ( HuskyDeviceControlTransport.this.clientiles ) {
                    HuskyDeviceClientile clientile = HuskyDeviceControlTransport.this.clientiles.computeIfAbsent(
                            clientId,
                            key -> new HuskyDeviceClientile()
                    );
                    clientile.attachChannel( clientId, channelId, channel );
                }
            }
        } );

        ulfServer.registerChannelInactiveHandler( new ChannelInactiveHandler() {
            @Override
            public boolean afterChannelInactive( ChannelControlBlock ccb, Object context ) throws ChannelHandleException {
                if ( ccb == null || ccb.getChannel() == null || HuskyDeviceControlTransport.this.deviceManager == null ) {
                    return false;
                }

                UMCChannel channel = ccb.getChannel();
                long clientId = channel.getIdentityID();
                Object channelId = channel.getChannelID();
                if ( clientId <= 0 || channelId == null ) {
                    return false;
                }

                synchronized ( HuskyDeviceControlTransport.this.clientiles ) {
                    HuskyDeviceClientile clientile = HuskyDeviceControlTransport.this.clientiles.get( clientId );
                    if ( clientile != null ) {
                        clientile.detachChannel( channelId );
                    }

                    if ( clientile == null || clientile.isDefunct() ) {
                        HuskyDeviceControlTransport.this.clientiles.remove( clientId );
                        HuskyDeviceControlTransport.this.deviceManager.deviceRuntimeService().detachConnectionByClientId(
                                clientId,
                                "Husky channel inactive."
                        );
                    }
                }
                return false;
            }
        } );
        this.eventHandlersRegistered = true;
    }

    @Override
    public Long getTransportId() {
        return this.appointServer.getMessageNodeId();
    }

    @Override
    public String getName() {
        return this.appointServer.getName();
    }

    @Override
    public PatriarchalConfig getConfig() {
        return this.appointServer.getConfig();
    }

    @Override
    public void execute() throws Exception {
        this.appointServer.execute();
    }

    @Override
    public boolean isStarted() {
        return !this.appointServer.getMessageNode().isTerminated();
    }

    @Override
    public boolean containsClient( long clientId ) {
        ChannelPool pool = this.appointServer.getUMCTExpress().getPoolByClientId( clientId );
        return pool != null && !pool.isEmpty();
    }

    @Override
    public void shutdownClientDevice( long clientId, GUID instanceGuid, String reason ) throws DeviceControlRPCException {
        try {
            this.ensureClientConnected( clientId );
            PassiveDeviceManipulatedIface iface = this.appointServer.getIface( clientId, PassiveDeviceManipulatedIface.class );
            iface.shutdownDevice( instanceGuid == null ? null : instanceGuid.toString(), reason );
        }
        catch ( Exception e ) {
            throw new DeviceControlRPCException( e );
        }
    }

    protected void ensureClientConnected( long clientId ) throws DeviceControlRPCException {
        if ( this.containsClient( clientId ) ) {
            return;
        }

        throw new DeviceControlRPCException( "Husky device client is not connected, clientId => `" + clientId + "`." );
    }

    @Override
    public void close() {
        this.appointServer.close();
    }
}
