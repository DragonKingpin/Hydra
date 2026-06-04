package com.walnut.odin.proc.server.transport.husky;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.id.GUID;
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
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.control.RemoteProcessControlFrameIface;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;
import com.walnut.odin.proc.server.MasterProcessLifecycleIface;
import com.walnut.odin.proc.server.ReactiveSlaveProcessLifecycleController;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransport;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportType;
import com.walnut.odin.proc.server.transport.entity.RemoteProcessControlTransportInspection;
import com.walnut.odin.proc.server.transport.entity.TransportConnection;
import com.walnut.odin.proc.server.transport.entity.TransportHandle;

public class HuskyRemoteProcessControlTransport implements RemoteProcessControlTransport {

    protected static final String        MASTER_IFACE_PREFIX = "com.walnut.odin.proc.server.MasterProcessLifecycleIface.";

    protected Logger                     log = LoggerFactory.getLogger( this.getClass() );

    protected RemoteProcessManagerServer mRemoteProcessManagerServer;

    protected UlfServer                  mRPCServer;

    protected DuplexAppointServer        mDuplexAppointServer;

    protected Map<String, Object>        mPendingControllerMap;

    protected Map<String, IfaceCompileEntry> mPendingIfaceCompileMap;

    public HuskyRemoteProcessControlTransport( RemoteProcessManagerServer remoteProcessManagerServer, UlfServer rpcServer ) {
        this.mRemoteProcessManagerServer = remoteProcessManagerServer;
        this.mRPCServer                  = rpcServer;
        this.mPendingControllerMap       = new LinkedHashMap<>();
        this.mPendingIfaceCompileMap     = new LinkedHashMap<>();
    }

    protected String makeControllerKey( Object controller ) {
        return controller.getClass().getName();
    }

    protected String makeIfaceCompileKey( Class<?> ifaceClass, boolean bAsIface ) {
        return ifaceClass.getName() + "#" + bAsIface;
    }

    protected void registerController0( Object controller ) {
        this.mDuplexAppointServer.registerController( controller );
        this.log.info( "[HuskyControlControllerRegistered] (Controller: `{}`) <Done>", controller.getClass().getName() );
    }

    protected void compileIface0( Class<?> ifaceClass, boolean bAsIface ) {
        this.mDuplexAppointServer.compile( ifaceClass, bAsIface );
        this.log.info( "[HuskyControlIfaceCompiled] (Iface: `{}`, AsIface: `{}`) <Done>", ifaceClass.getName(), bAsIface );
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

                long clientId = channel.getIdentityID();
                if ( clientId <= 0 ) {
                    return;
                }

                DuplexAppointServer appointServer = HuskyRemoteProcessControlTransport.this.mDuplexAppointServer;
                if ( appointServer == null || appointServer.getUMCTExpress() == null ) {
                    return;
                }

                ChannelPool pool = appointServer.getUMCTExpress().getPoolByClientId( clientId );
                if ( pool == null || pool.isEmpty() ) {
                    return;
                }

                HuskyRemoteProcessControlTransport.this.mRemoteProcessManagerServer.transportRegistry().bindClient(
                        clientId,
                        HuskyRemoteProcessControlTransport.this
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

                long clientId = channel.getIdentityID();
                if ( clientId <= 0 ) {
                    return false;
                }

                DuplexAppointServer appointServer = HuskyRemoteProcessControlTransport.this.mDuplexAppointServer;
                if ( appointServer == null || appointServer.getUMCTExpress() == null ) {
                    return false;
                }

                ChannelPool pool = appointServer.getUMCTExpress().getPoolByClientId( clientId );
                if ( pool == null || pool.isEmpty() ) {
                    HuskyRemoteProcessControlTransport.this.mRemoteProcessManagerServer.transportRegistry().detachClient( clientId );
                }
                return false;
            }
        } );
    }

    protected void initRPCSubsystem() throws RemoteProcessServiceRPCException {
        if ( this.mDuplexAppointServer != null && !this.mDuplexAppointServer.getMessageNode().isTerminated() ) {
            this.log.info( "[Notice] DuplexAppointServer has already started. <Pass>" );
            return;
        }

        try {
            this.mDuplexAppointServer = new WolvesAppointServer( this.mRPCServer, HuskyDuplexExpress.class );
            this.registerUlfServerEventHandlers();
            ReactiveSlaveProcessLifecycleController controller = new ReactiveSlaveProcessLifecycleController( this.mRemoteProcessManagerServer );
            ReactiveRemoteProcessControlFrameController controlFrameController = new ReactiveRemoteProcessControlFrameController( this.mRemoteProcessManagerServer, this );
            this.registerController0( controller );
            this.registerController0( controlFrameController );
            this.compileIface0( MasterProcessLifecycleIface.class, false );
            this.compileIface0( RemoteProcessControlFrameIface.class, false );
            this.flushPendingControllers();
            this.flushPendingIfaceCompiles();

            this.mRemoteProcessManagerServer.infoLifecycle( "Husky Control Transport Register Controllers", LogStatuses.StatusDone );
        }
        catch ( Exception e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }

    protected void vitalizeRPCSubsystem() throws RemoteProcessServiceRPCException {
        try {
            if ( this.mDuplexAppointServer.getMessageNode().isTerminated() ) {
                this.mDuplexAppointServer.execute();
                this.mRemoteProcessManagerServer.infoLifecycle( "Husky Control Transport Vitalization", LogStatuses.StatusDone );
            }
        }
        catch ( Exception e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }

    protected Object invokeInform( long clientId, String szMethodName, Object... arguments ) throws RemoteProcessServiceRPCException {
        try {
            this.ensureClientConnected( clientId );
            return this.mDuplexAppointServer.invokeInform( clientId, MASTER_IFACE_PREFIX + szMethodName, arguments );
        }
        catch ( IOException e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
        catch ( IllegalArgumentException e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }

    protected void ensureClientConnected( long clientId ) throws RemoteProcessServiceRPCException {
        if ( this.containsClient( clientId ) ) {
            return;
        }

        throw new RemoteProcessServiceRPCException( "Remote process control client is not connected, clientId => `" + clientId + "`." );
    }

    @Override
    public RemoteProcessControlTransportType transportType() {
        return RemoteProcessControlTransportType.Husky;
    }

    @Override
    public boolean containsClient( long clientId ) {
        if ( this.mDuplexAppointServer == null ) {
            return false;
        }
        ChannelPool pool = this.mDuplexAppointServer.getUMCTExpress().getPoolByClientId( clientId );
        return pool != null && !pool.isEmpty();
    }

    @Override
    public Collection<TransportConnection> queryClientConnections( long clientId ) {
        List<TransportConnection> connections = new ArrayList<>();
        if ( this.mDuplexAppointServer == null ) {
            return connections;
        }

        ChannelPool pool = this.mDuplexAppointServer.getUMCTExpress().getPoolByClientId( clientId );
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
        for ( TransportHandle handle : this.mRemoteProcessManagerServer.transportRegistry().transportHandles() ) {
            if ( handle.getTransport() == this && this.containsClient( handle.getClientId() ) ) {
                nCount++;
            }
        }
        return nCount;
    }

    @Override
    public int queryRegisteredControllerCount() {
        int nBaseCount = this.mDuplexAppointServer == null ? 0 : 1;
        return nBaseCount + this.mPendingControllerMap.size();
    }

    @Override
    public int queryCompiledIfaceCount() {
        return this.mPendingIfaceCompileMap.size();
    }

    @Override
    public RemoteProcessControlTransportInspection inspectTransport() {
        RemoteProcessControlTransportInspection inspection = RemoteProcessControlTransport.super.inspectTransport();
        inspection.setRouteSource( this.mDuplexAppointServer == null ? this : this.mDuplexAppointServer );
        inspection.setEndpointSource( this.mDuplexAppointServer == null ? this.mRPCServer : this.mDuplexAppointServer );
        return inspection;
    }

    protected void collectChannelConnections( List<TransportConnection> connections, Collection<?> channels ) {
        if ( channels == null ) {
            return;
        }

        for ( Object item : channels ) {
            if ( !( item instanceof ChannelControlBlock ) ) {
                continue;
            }

            TransportConnection connection = this.createTransportConnection( (ChannelControlBlock) item );
            if ( connection != null ) {
                connections.add( connection );
            }
        }
    }

    protected TransportConnection createTransportConnection( ChannelControlBlock block ) {
        if ( block == null || block.getChannel() == null ) {
            return null;
        }

        UMCChannel channel = block.getChannel();
        TransportConnection connection = new TransportConnection();
        connection.setType( "Channel" );
        connection.setIdentity( String.valueOf( channel.getChannelID() ) );
        connection.setRemoteAddress( String.valueOf( channel.remoteAddress() ) );
        connection.setLocalAddress( String.valueOf( channel.localAddress() ) );
        connection.setStatus( String.valueOf( channel.getChannelStatus() ) );
        connection.setActive( !channel.isShutdown() );
        return connection;
    }

    @Override
    public void registerController( Object controller ) throws RemoteProcessServiceRPCException {
        if ( controller == null ) {
            return;
        }

        String szKey = this.makeControllerKey( controller );
        if ( this.mPendingControllerMap.containsKey( szKey ) ) {
            this.log.info( "[HuskyControlControllerRegisterSkipped] (Controller: `{}`) <Existed>", szKey );
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
    public void compileIface( Class<?> ifaceClass, boolean bAsIface ) throws RemoteProcessServiceRPCException {
        if ( ifaceClass == null ) {
            return;
        }

        String szKey = this.makeIfaceCompileKey( ifaceClass, bAsIface );
        if ( this.mPendingIfaceCompileMap.containsKey( szKey ) ) {
            this.log.info( "[HuskyControlIfaceCompileSkipped] (Iface: `{}`, AsIface: `{}`) <Existed>", ifaceClass.getName(), bAsIface );
            return;
        }

        this.mPendingIfaceCompileMap.put( szKey, new IfaceCompileEntry( ifaceClass, bAsIface ) );
        if ( this.mDuplexAppointServer != null ) {
            this.compileIface0( ifaceClass, bAsIface );
        }
    }

    @Override
    public void startService() throws RemoteProcessServiceRPCException {
        this.initRPCSubsystem();
        this.vitalizeRPCSubsystem();
    }

    @Override
    public void terminateService() throws IllegalStateException {
        if ( this.mDuplexAppointServer == null ) {
            throw new IllegalStateException( "Husky control transport dose not started yet." );
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

    @Override
    public void startRemoteUProcess( long clientId, GUID pid ) throws RemoteProcessServiceRPCException {
        this.invokeInform( clientId, "startRemoteUProcess", pid );
    }

    @Override
    public RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, UProcessMirrorDTO processDTO ) throws RemoteProcessLifecycleException {
        try {
            return (RemoteVitalizationResponse) this.invokeInform( clientId, "vitalizeRemoteUProcess", processDTO );
        }
        catch ( RemoteProcessServiceRPCException e ) {
            throw new RemoteProcessLifecycleException( e );
        }
    }

    @Override
    public RemoteVitalizationResponse createRemoteUProcess( long clientId, UProcessMirrorDTO processDTO ) throws RemoteProcessLifecycleException {
        try {
            return (RemoteVitalizationResponse) this.invokeInform( clientId, "createRemoteUProcess", processDTO );
        }
        catch ( RemoteProcessServiceRPCException e ) {
            throw new RemoteProcessLifecycleException( e );
        }
    }

    @Override
    public boolean hasOwnProcess( long clientId, GUID pid ) throws RemoteProcessServiceRPCException {
        return (boolean) this.invokeInform( clientId, "hasOwnProcess", pid.toString() );
    }

    @Override
    public boolean containProcess( long clientId, GUID pid ) throws RemoteProcessServiceRPCException {
        return (boolean) this.invokeInform( clientId, "containProcess", pid.toString() );
    }

    @Override
    public UProcessRuntimeMeta queryProcessRuntimeMeta( long clientId, GUID pid ) throws RemoteProcessLifecycleException {
        try {
            return (UProcessRuntimeMeta) this.invokeInform( clientId, "queryRemoteProcessRuntimeMeta", pid.toString() );
        }
        catch ( RemoteProcessServiceRPCException e ) {
            throw new RemoteProcessLifecycleException( e );
        }
    }

    protected static class IfaceCompileEntry {
        protected Class<?>    mIfaceClass;

        protected boolean     mbAsIface;

        protected IfaceCompileEntry( Class<?> ifaceClass, boolean bAsIface ) {
            this.mIfaceClass = ifaceClass;
            this.mbAsIface   = bAsIface;
        }
    }

}
