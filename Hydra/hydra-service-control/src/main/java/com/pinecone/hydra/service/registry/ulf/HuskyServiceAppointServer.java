package com.pinecone.hydra.service.registry.ulf;

import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.hydra.service.registry.appoint.ServiceAppointServer;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.ChannelHandleException;
import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umc.msg.event.ChannelEventHandler;
import com.pinecone.hydra.umc.msg.event.ChannelInactiveHandler;
import com.pinecone.hydra.umc.wolf.server.UlfServer;

public class HuskyServiceAppointServer implements ServiceAppointServer {

    protected DuplexAppointServer mAppointServer;

    protected ServiceManager      mServiceManager;

    public HuskyServiceAppointServer( DuplexAppointServer duplexAppointServer ) {
        this.mAppointServer = duplexAppointServer;
    }

    public HuskyServiceAppointServer( DuplexAppointServer duplexAppointServer, ServiceManager serviceManager ) {
        this( duplexAppointServer );
        this.mServiceManager = serviceManager;
    }

    @Override
    public ServiceManager serviceManager() {
        return this.mServiceManager;
    }

    @Override
    public ServiceAppointServer hookServiceManager( ServiceManager serviceManager ) {
        if( this.mServiceManager != null ) {
            throw new IllegalStateException( "Manager has already hooked." );
        }

        this.mServiceManager = serviceManager;
        this.mAppointServer.registerController( new ServiceLifecycleController( this.mServiceManager ) );
        this.mAppointServer.registerController( new ServiceMetaController( this.mServiceManager ) );

        MessageNode messageNode = this.mAppointServer.getMessageNode();
        UlfServer ulfServer   = (UlfServer) messageNode;
        ulfServer.registerDataArrivedEventHandlers(new ChannelEventHandler() {
            @Override
            public void afterEventTriggered( ChannelControlBlock block, Object context ) {
                long clientId    = block.getChannel().getIdentityID();
                Object channelId = block.getChannel().getChannelID();

                mServiceManager.serviceEventHooker().afterNewConnectionInbound(
                        clientId, channelId, block.getChannel(), context,
                        () -> new HuskyServiceClientile( HuskyServiceAppointServer.this )
                );
            }
        });

        ulfServer.registerChannelInactiveHandler(new ChannelInactiveHandler() {
            @Override
            public boolean afterChannelInactive( ChannelControlBlock ccb, Object context ) throws ChannelHandleException {
                Long clientId    = ccb.getChannel().getIdentityID();
                Object channelId = ccb.getChannel().getChannelID();

                mServiceManager.serviceEventHooker().afterConnectionDetach( clientId, channelId, ccb.getChannel() );
                return false;
            }
        });

        this.mServiceManager.getLogger().info( "AppointServer[{}] has been hooked to service manager.", this.mAppointServer.getName() );
        return this;
    }

    @Override
    public String getName() {
        return this.mAppointServer.getName();
    }

    @Override
    public PatriarchalConfig getConfig() {
        return this.mAppointServer.getConfig();
    }

    @Override
    public void close() {
        this.mAppointServer.close();
    }

    @Override
    public void execute() throws Exception {
        this.mAppointServer.execute();
    }

    @Override
    public long getMessageNodeId() {
        return this.mAppointServer.getMessageNodeId();
    }

    @Override
    public boolean isTerminated() {
        return this.mAppointServer.getMessageNode().isTerminated();
    }

    @Override
    public boolean isStarted() {
        return !this.isTerminated();
    }
}
