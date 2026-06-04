package com.acorn.redqueen.service.registry.husky.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acorn.redqueen.service.registry.husky.protocol.PassiveServiceManipulatedIface;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransport;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransportType;
import com.pinecone.hydra.service.registry.server.transport.entity.ServiceControlTransportInspection;
import com.pinecone.hydra.service.registry.server.transport.entity.ServiceTransportConnection;
import com.pinecone.hydra.service.registry.server.transport.entity.ServiceTransportHandle;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.ChannelHandleException;
import com.pinecone.hydra.umc.msg.ChannelPool;
import com.pinecone.hydra.umc.msg.FairChannelPool;
import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umc.msg.UMCChannel;
import com.pinecone.hydra.umc.msg.event.ChannelEventHandler;
import com.pinecone.hydra.umc.msg.event.ChannelInactiveHandler;
import com.pinecone.hydra.umc.wolf.server.UlfServer;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;

public class HuskyServiceControlTransport implements ServiceControlTransport {

    protected Logger                              mLogger = LoggerFactory.getLogger( this.getClass() );

    protected ServiceManager                      mServiceManager;

    protected UlfServer                           mRPCServer;

    protected DuplexAppointServer                 mDuplexAppointServer;

    protected Map<String, Object>                 mPendingControllerMap;

    protected Map<String, IfaceCompileEntry>      mPendingIfaceCompileMap;

    public HuskyServiceControlTransport( ServiceManager serviceManager, UlfServer rpcServer ) {
        this.mServiceManager          = serviceManager;
        this.mRPCServer               = rpcServer;
        this.mPendingControllerMap    = new LinkedHashMap<>();
        this.mPendingIfaceCompileMap  = new LinkedHashMap<>();
    }

    protected String makeControllerKey( Object controller ) {
        return controller.getClass().getName();
    }

    protected String makeIfaceCompileKey( Class<?> ifaceClass, boolean bAsIface ) {
        return ifaceClass.getName() + "#" + bAsIface;
    }

    protected void registerController0( Object controller ) {
        this.mDuplexAppointServer.registerController( controller );
        this.mLogger.info( "[HuskyServiceControllerRegistered] (Controller: `{}`) <Done>", controller.getClass().getName() );
    }

    protected void compileIface0( Class<?> ifaceClass, boolean bAsIface ) {
        this.mDuplexAppointServer.compile( ifaceClass, bAsIface );
        this.mLogger.info( "[HuskyServiceIfaceCompiled] (Iface: `{}`, AsIface: `{}`) <Done>", ifaceClass.getName(), bAsIface );
    }

    protected void flushPendingControllers() {
        for ( Object controller : this.mPendingControllerMap.values() ) {
            this.registerController0( controller );
        }
    }

    protected void flushPendingIfaceCompiles() {
        for ( IfaceCompileEntry entry : this.mPendingIfaceCompileMap.values() ) {
            this.compileIface0( entry.mIfaceClass, entry.mbAsIface );
        }
    }

    protected void registerUlfServerEventHandlers() {
        MessageNode messageNode = this.mDuplexAppointServer.getMessageNode();
        UlfServer ulfServer = (UlfServer) messageNode;

        ulfServer.registerDataArrivedEventHandlers( new ChannelEventHandler() {
            @Override
            public void afterEventTriggered( ChannelControlBlock block, Object context ) {
                if ( block == null ) {
                    return;
                }

                UMCChannel channel = block.getChannel();
                if ( channel == null ) {
                    return;
                }

                long nClientId = channel.getIdentityID();
                Object channelId = channel.getChannelID();
                if ( nClientId <= 0 ) {
                    return;
                }

                HuskyServiceControlTransport.this.mServiceManager.serviceEventHooker().afterNewConnectionInbound(
                        nClientId,
                        channelId,
                        channel,
                        context,
                        () -> new HuskyServiceClientile( HuskyServiceControlTransport.this )
                );
            }
        } );

        ulfServer.registerChannelInactiveHandler( new ChannelInactiveHandler() {
            @Override
            public boolean afterChannelInactive( ChannelControlBlock ccb, Object context ) throws ChannelHandleException {
                if ( ccb == null ) {
                    return false;
                }

                UMCChannel channel = ccb.getChannel();
                if ( channel == null ) {
                    return false;
                }

                Long clientId = channel.getIdentityID();
                Object channelId = channel.getChannelID();
                HuskyServiceControlTransport.this.mServiceManager.serviceEventHooker().afterConnectionDetach( clientId, channelId, channel );
                return false;
            }
        } );
    }

    protected void initRPCSubsystem() throws ServiceControlRPCException {
        if ( this.mDuplexAppointServer != null && !this.mDuplexAppointServer.getMessageNode().isTerminated() ) {
            this.mLogger.info( "[Notice] DuplexAppointServer has already started. <Pass>" );
            return;
        }

        try {
            this.mDuplexAppointServer = new WolvesAppointServer( this.mRPCServer, HuskyDuplexExpress.class );
            this.registerUlfServerEventHandlers();
            this.registerController0( new ServiceLifecycleController( this.mServiceManager ) );
            this.registerController0( new ServiceMetaController( this.mServiceManager ) );
            this.compileIface0( PassiveServiceManipulatedIface.class, false );
            this.flushPendingControllers();
            this.flushPendingIfaceCompiles();

            this.mServiceManager.infoLifecycle( "Husky Service Control Transport Register Controllers", LogStatuses.StatusDone );
        }
        catch ( Exception e ) {
            throw new ServiceControlRPCException( e );
        }
    }

    protected void vitalizeRPCSubsystem() throws ServiceControlRPCException {
        try {
            if ( this.mDuplexAppointServer.getMessageNode().isTerminated() ) {
                this.mDuplexAppointServer.execute();
                this.mServiceManager.infoLifecycle( "Husky Service Control Transport Vitalization", LogStatuses.StatusDone );
            }
        }
        catch ( Exception e ) {
            throw new ServiceControlRPCException( e );
        }
    }

    @Override
    public ServiceControlTransportType transportType() {
        return ServiceControlTransportType.Husky;
    }

    @Override
    public boolean containsClient( long nClientId ) {
        if ( this.mDuplexAppointServer == null ) {
            return false;
        }

        ChannelPool pool = this.mDuplexAppointServer.getUMCTExpress().getPoolByClientId( nClientId );
        return pool != null && !pool.isEmpty();
    }

    @Override
    public Collection<ServiceTransportConnection> queryClientConnections( long nClientId ) {
        List<ServiceTransportConnection> connections = new ArrayList<>();
        if ( this.mDuplexAppointServer == null ) {
            return connections;
        }

        ChannelPool pool = this.mDuplexAppointServer.getUMCTExpress().getPoolByClientId( nClientId );
        if ( pool == null ) {
            return connections;
        }

        this.collectChannelConnections( connections, pool.getPooledChannels() );
        if ( connections.isEmpty() && pool instanceof FairChannelPool ) {
            FairChannelPool fairChannelPool = (FairChannelPool) pool;
            this.collectChannelConnections( connections, fairChannelPool.getMajorQueue() );
        }
        return connections;
    }

    @Override
    public int queryConnectedClientCount() {
        if ( this.mDuplexAppointServer == null ) {
            return 0;
        }
        int nCount = 0;
        for ( ServiceTransportHandle handle : this.mServiceManager.transportRegistry().transportHandles() ) {
            if ( this.containsClient( handle.getClientId() ) ) {
                nCount++;
            }
        }
        return nCount;
    }

    @Override
    public int queryRegisteredControllerCount() {
        int nBaseCount = this.mDuplexAppointServer == null ? 0 : 2;
        return nBaseCount + this.mPendingControllerMap.size();
    }

    @Override
    public int queryCompiledIfaceCount() {
        int nBaseCount = this.mDuplexAppointServer == null ? 0 : 1;
        return nBaseCount + this.mPendingIfaceCompileMap.size();
    }

    @Override
    public String queryControllerSummary() {
        return "ServiceLifecycleController, ServiceMetaController";
    }

    @Override
    public String queryIfaceSummary() {
        return "ServiceLifecycleIface, ServiceMetaManipulationIface, PassiveServiceManipulatedIface";
    }

    @Override
    public ServiceControlTransportInspection inspectTransport() {
        ServiceControlTransportInspection inspection = ServiceControlTransport.super.inspectTransport();
        inspection.setRouteSource( this.mDuplexAppointServer == null ? this : this.mDuplexAppointServer );
        inspection.setEndpointSource( this.mDuplexAppointServer == null ? this.mRPCServer : this.mDuplexAppointServer );
        return inspection;
    }

    protected void ensureClientConnected( long nClientId ) throws ServiceControlRPCException {
        if ( this.containsClient( nClientId ) ) {
            return;
        }

        throw new ServiceControlRPCException( "Husky service client is not connected, clientId => `" + nClientId + "`." );
    }

    protected PassiveServiceManipulatedIface queryPassiveServiceManipulatedIface( long nClientId ) throws ServiceControlRPCException {
        try {
            this.ensureClientConnected( nClientId );
            return this.mDuplexAppointServer.getIface( nClientId, PassiveServiceManipulatedIface.class );
        }
        catch ( Exception e ) {
            throw new ServiceControlRPCException( e );
        }
    }

    @Override
    public void shutdownClientService( long nClientId, GUID instanceGuid, String szReason ) throws ServiceControlRPCException {
        PassiveServiceManipulatedIface iface = this.queryPassiveServiceManipulatedIface( nClientId );
        iface.shutdownService( instanceGuid.toString(), szReason );
    }

    protected void collectChannelConnections( List<ServiceTransportConnection> connections, Collection<?> channels ) {
        if ( channels == null ) {
            return;
        }

        for ( Object item : channels ) {
            if ( !( item instanceof ChannelControlBlock ) ) {
                continue;
            }

            ServiceTransportConnection connection = this.createTransportConnection( (ChannelControlBlock) item );
            if ( connection != null ) {
                connections.add( connection );
            }
        }
    }

    protected ServiceTransportConnection createTransportConnection( ChannelControlBlock block ) {
        if ( block == null || block.getChannel() == null ) {
            return null;
        }

        UMCChannel channel = block.getChannel();
        ServiceTransportConnection connection = new ServiceTransportConnection();
        connection.setType( "Channel" );
        connection.setIdentity( String.valueOf( channel.getChannelID() ) );
        connection.setRemoteAddress( String.valueOf( channel.remoteAddress() ) );
        connection.setStatus( String.valueOf( channel.getChannelStatus() ) );
        connection.setActive( !channel.isShutdown() );
        return connection;
    }

    @Override
    public void registerController( Object controller ) throws ServiceControlRPCException {
        if ( controller == null ) {
            return;
        }

        String szKey = this.makeControllerKey( controller );
        if ( this.mPendingControllerMap.containsKey( szKey ) ) {
            this.mLogger.info( "[HuskyServiceControllerRegisterSkipped] (Controller: `{}`) <Existed>", szKey );
            return;
        }

        this.mPendingControllerMap.put( szKey, controller );
        if ( this.mDuplexAppointServer != null ) {
            this.registerController0( controller );
        }
    }

    @Override
    public boolean supportsRuntimeIfaceCompile() {
        return true;
    }

    @Override
    public void compileIface( Class<?> ifaceClass, boolean bAsIface ) throws ServiceControlRPCException {
        if ( ifaceClass == null ) {
            return;
        }

        String szKey = this.makeIfaceCompileKey( ifaceClass, bAsIface );
        if ( this.mPendingIfaceCompileMap.containsKey( szKey ) ) {
            this.mLogger.info( "[HuskyServiceIfaceCompileSkipped] (Iface: `{}`, AsIface: `{}`) <Existed>", ifaceClass.getName() );
            return;
        }

        this.mPendingIfaceCompileMap.put( szKey, new IfaceCompileEntry( ifaceClass, bAsIface ) );
        if ( this.mDuplexAppointServer != null ) {
            this.compileIface0( ifaceClass, bAsIface );
        }
    }

    @Override
    public void startService() throws ServiceControlRPCException {
        this.initRPCSubsystem();
        this.vitalizeRPCSubsystem();
    }

    @Override
    public void terminateService() throws IllegalStateException {
        if ( this.mDuplexAppointServer == null ) {
            throw new IllegalStateException( "Husky service control transport dose not started yet." );
        }

        DuplexAppointServer appointServer = this.mDuplexAppointServer;
        this.mDuplexAppointServer = null;
        appointServer.terminate();
        if ( this.mRPCServer instanceof WolfMCServer ) {
            ( (WolfMCServer)this.mRPCServer ).close();
        }
    }

    @Override
    public boolean isStarted() {
        return this.mDuplexAppointServer != null && !this.mDuplexAppointServer.getMessageNode().isTerminated();
    }

    @Override
    public boolean isTerminated() {
        return this.mDuplexAppointServer == null || this.mDuplexAppointServer.getMessageNode().isTerminated();
    }

    protected static class IfaceCompileEntry {

        protected Class<?> mIfaceClass;

        protected boolean  mbAsIface;

        protected IfaceCompileEntry( Class<?> ifaceClass, boolean bAsIface ) {
            this.mIfaceClass = ifaceClass;
            this.mbAsIface   = bAsIface;
        }

    }

}
