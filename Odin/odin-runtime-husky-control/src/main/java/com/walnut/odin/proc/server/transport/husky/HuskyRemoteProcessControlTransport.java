package com.walnut.odin.proc.server.transport.husky;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umc.wolf.server.UlfServer;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;
import com.walnut.odin.proc.server.MasterProcessLifecycleIface;
import com.walnut.odin.proc.server.ReactiveSlaveProcessLifecycleController;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransport;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportType;

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

    protected void initRPCSubsystem() throws RemoteProcessServiceRPCException {
        if ( this.mDuplexAppointServer != null && !this.mDuplexAppointServer.getMessageNode().isTerminated() ) {
            this.log.info( "[Notice] DuplexAppointServer has already started. <Pass>" );
            return;
        }

        try {
            this.mDuplexAppointServer = new WolvesAppointServer( this.mRPCServer, HuskyDuplexExpress.class );
            ReactiveSlaveProcessLifecycleController controller = new ReactiveSlaveProcessLifecycleController( this.mRemoteProcessManagerServer );
            this.registerController0( controller );
            this.compileIface0( MasterProcessLifecycleIface.class, false );
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
            return this.mDuplexAppointServer.invokeInform( clientId, MASTER_IFACE_PREFIX + szMethodName, arguments );
        }
        catch ( IOException e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }

    @Override
    public RemoteProcessControlTransportType transportType() {
        return RemoteProcessControlTransportType.Husky;
    }

    @Override
    public boolean containsClient( long clientId ) {
        return true;
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

        this.mDuplexAppointServer.terminate();
        this.mDuplexAppointServer = null;
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
