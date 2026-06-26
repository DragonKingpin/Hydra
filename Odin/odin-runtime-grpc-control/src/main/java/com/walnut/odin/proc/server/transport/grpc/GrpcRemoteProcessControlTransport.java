package com.walnut.odin.proc.server.transport.grpc;

import java.lang.reflect.Constructor;
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
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.grpc.server.GrpcAppointServer;
import com.walnut.odin.proc.RemoteProcess;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.pinecone.hydra.proc.signal.ProcSignal;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.entity.RemoteProcessSignalResult;
import com.walnut.odin.proc.entity.RemoteTerminationReport;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;
import com.walnut.odin.proc.server.RavenRemoteProcessManagerServer;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.proc.server.transport.CompositeRemoteProcessControlEventHooker;
import com.walnut.odin.proc.server.transport.RemoteProcessControlEventHooker;
import com.walnut.odin.proc.server.transport.RemoteProcessControlSession;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransport;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportType;
import com.walnut.odin.proc.server.transport.entity.RemoteProcessControlTransportInspection;
import com.walnut.odin.proc.server.transport.entity.TransportConnection;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.CommandResult;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrameType;
import io.grpc.BindableService;
import io.grpc.ServerInterceptors;

public class GrpcRemoteProcessControlTransport implements RemoteProcessControlTransport {

    protected Logger                            log = LoggerFactory.getLogger( this.getClass() );

    protected RemoteProcessManagerServer        mRemoteProcessManagerServer;

    protected GuidAllocator                     mGuidAllocator;

    protected GrpcAppointServer                 mGrpcAppointServer;

    protected CompositeRemoteProcessControlEventHooker  mEventHooker;

    protected Map<Long, GrpcRemoteProcessControlClientile> mClientileMap;

    protected GrpcCorrelationWaiter<Object>     mCorrelationWaiter;

    protected GrpcRemoteProcessFrameMapper      mFrameMapper;

    protected Object                            mProcessorLifecycleController;

    protected ScheduledExecutorService          mHeartbeatGuardian;

    protected Map<Class<?>, BindableService>    mAdditionalGrpcServiceMap;

    public GrpcRemoteProcessControlTransport( RemoteProcessManagerServer remoteProcessManagerServer,
                                              GrpcAppointServer grpcAppointServer,
                                              RemoteProcessControlEventHooker eventHooker ) {
        this.mRemoteProcessManagerServer = remoteProcessManagerServer;
        this.mGuidAllocator              = remoteProcessManagerServer.getGuidAllocator();
        this.mGrpcAppointServer          = grpcAppointServer;
        this.mEventHooker                = new CompositeRemoteProcessControlEventHooker();
        this.mClientileMap               = new ConcurrentHashMap<>();
        this.mCorrelationWaiter          = new GrpcCorrelationWaiter<>();
        this.mFrameMapper                = new GrpcRemoteProcessFrameMapper( this.mGuidAllocator );
        this.mAdditionalGrpcServiceMap   = new ConcurrentHashMap<>();
        this.addEventHooker( eventHooker );
    }

    public RemoteProcessManagerServer remoteProcessManagerServer() {
        return this.mRemoteProcessManagerServer;
    }

    public GrpcAppointServer grpcAppointServer() {
        return this.mGrpcAppointServer;
    }

    public GrpcCorrelationWaiter<Object> correlationWaiter() {
        return this.mCorrelationWaiter;
    }

    public GrpcRemoteProcessFrameMapper frameMapper() {
        return this.mFrameMapper;
    }

    public <T extends BindableService> T queryAdditionalGrpcService( Class<T> serviceClass ) {
        BindableService service = this.mAdditionalGrpcServiceMap.get( serviceClass );
        if ( serviceClass.isInstance( service ) ) {
            return serviceClass.cast( service );
        }

        for ( BindableService candidate : this.mAdditionalGrpcServiceMap.values() ) {
            if ( serviceClass.isInstance( candidate ) ) {
                return serviceClass.cast( candidate );
            }
        }
        return null;
    }

    public Collection<BindableService> additionalGrpcServices() {
        return this.mAdditionalGrpcServiceMap.values();
    }

    @Override
    public GrpcRemoteProcessControlTransport addEventHooker( RemoteProcessControlEventHooker hooker ) {
        if ( hooker == null ) {
            return this;
        }
        this.mEventHooker.addHooker( hooker );
        hooker.onTransportHooked( this );
        for ( BindableService service : this.mAdditionalGrpcServiceMap.values() ) {
            hooker.onAdditionalServiceRegistered( this, service );
        }
        return this;
    }

    protected String nextGuidString() {
        return this.mGuidAllocator.nextGUID().toString();
    }

    public void bindClientSession( GrpcRemoteProcessControlSession session ) {
        GrpcRemoteProcessControlClientile clientile = this.mClientileMap.computeIfAbsent(
                session.clientId(),
                clientId -> new GrpcRemoteProcessControlClientile( clientId, this )
        );
        clientile.attachSession( session );
        this.log.info(
                "[GrpcControl] [SessionBind] (ClientId: `{}`, Session: `{}`, RemoteAddress: `{}`, ActiveSessions: `{}`) <Done>",
                session.clientId(),
                session.sessionGuid(),
                session.remoteAddress(),
                clientile.sessions().size()
        );
        this.mEventHooker.onClientInitialized( this, session.clientId() );
    }

    public void detachClientSession( GrpcRemoteProcessControlSession session ) {
        GrpcRemoteProcessControlClientile clientile = this.mClientileMap.get( session.clientId() );
        if ( clientile != null ) {
            clientile.detachSession( session );
            this.log.info(
                    "[GrpcControl] [SessionDetach] (ClientId: `{}`, Session: `{}`, RemoteAddress: `{}`, ActiveSessions: `{}`) <Done>",
                    session.clientId(),
                    session.sessionGuid(),
                    session.remoteAddress(),
                    clientile.sessions().size()
            );
            if ( !clientile.isActive() ) {
                this.mClientileMap.remove( session.clientId() );
                this.log.info( "[GrpcControl] [ClientUnhook] (ClientId: `{}`) <Detached>", session.clientId() );
                this.mRemoteProcessManagerServer.detachClient( session.clientId() );
            }
        }
        else {
            this.log.warn(
                    "[GrpcControl] [SessionDetach] (ClientId: `{}`, Session: `{}`, RemoteAddress: `{}`) <MissingClient>",
                    session.clientId(),
                    session.sessionGuid(),
                    session.remoteAddress()
            );
        }
        session.close();
    }

    @Override
    public RemoteProcessControlTransportType transportType() {
        return RemoteProcessControlTransportType.Grpc;
    }

    @Override
    public boolean containsClient( long clientId ) {
        GrpcRemoteProcessControlClientile clientile = this.mClientileMap.get( clientId );
        return clientile != null && clientile.isActive();
    }

    @Override
    public Collection<TransportConnection> queryClientConnections( long clientId ) {
        List<TransportConnection> connections = new ArrayList<>();
        GrpcRemoteProcessControlClientile clientile = this.mClientileMap.get( clientId );
        if ( clientile == null ) {
            return connections;
        }

        for ( RemoteProcessControlSession session : clientile.sessions() ) {
            TransportConnection connection = new TransportConnection();
            connection.setType( "Session" );
            if ( session instanceof GrpcRemoteProcessControlSession ) {
                GrpcRemoteProcessControlSession grpcSession = (GrpcRemoteProcessControlSession) session;
                connection.setIdentity( grpcSession.sessionGuid() );
                connection.setRemoteAddress( grpcSession.remoteAddress() );
                connection.setLastActiveTimeMillis( grpcSession.lastActiveTimeMillis() );
                connection.setLastHeartbeatTimeMillis( grpcSession.lastHeartbeatTimeMillis() );
            }
            else {
                connection.setIdentity( session.getClass().getSimpleName() + "@" + System.identityHashCode( session ) );
            }
            connection.setStatus( session.isActive() ? "ACTIVE" : "INACTIVE" );
            connection.setActive( session.isActive() );
            connections.add( connection );
        }
        return connections;
    }

    @Override
    public int queryConnectedClientCount() {
        int nCount = 0;
        for ( GrpcRemoteProcessControlClientile clientile : this.mClientileMap.values() ) {
            if ( clientile.isActive() ) {
                nCount++;
            }
        }
        return nCount;
    }

    @Override
    public int queryRegisteredControllerCount() {
        return this.mGrpcAppointServer == null ? 0 : 1 + this.mAdditionalGrpcServiceMap.size();
    }

    @Override
    public int queryCompiledIfaceCount() {
        return 0;
    }

    @Override
    public RemoteProcessControlTransportInspection inspectTransport() {
        RemoteProcessControlTransportInspection inspection = RemoteProcessControlTransport.super.inspectTransport();
        inspection.setRouteSource( this );
        inspection.setEndpointSource( this.mGrpcAppointServer == null ? this : this.mGrpcAppointServer );
        return inspection;
    }

    public void acceptClientFrame( GrpcRemoteProcessControlSession session, RemoteProcessControlFrame frame ) {
        if ( session == null || !session.isActive() ) {
            return;
        }
        session.touchActive();
        String szCorrelationGuid = frame.getCorrelationGuid();
        switch ( frame.getFrameType() ) {
            case HEARTBEAT: {
                session.touchHeartbeat();
                this.log.debug(
                        "[GrpcControl] [Heartbeat] (ClientId: `{}`, Session: `{}`, RemoteAddress: `{}`, Status: `{}`) <Arrived>",
                        session.clientId(),
                        session.sessionGuid(),
                        session.remoteAddress(),
                        frame.getHeartbeat().getStatus()
                );
                break;
            }
            case COMMAND_RESULT: {
                this.mCorrelationWaiter.complete( szCorrelationGuid, frame.getCommandResult() );
                break;
            }
            case PROCESS_RUNTIME_META: {
                this.mCorrelationWaiter.complete( szCorrelationGuid, frame.getProcessRuntimeMeta() );
                break;
            }
            case PROCESS_TERMINATED: {
                this.acceptProcessTerminated( session, frame );
                break;
            }
            case ERROR: {
                this.mCorrelationWaiter.completeExceptionally( szCorrelationGuid, new GrpcRemoteProcessControlException( frame.getError().getMessage() ) );
                break;
            }
            default: {
                this.log.info( "[GrpcControlFrameReceived] (ClientId: `{}`, Type: `{}`) <Pass>", session.clientId(), frame.getFrameType() );
                break;
            }
        }
    }

    protected void acceptProcessTerminated( GrpcRemoteProcessControlSession session, RemoteProcessControlFrame frame ) {
        RemoteTerminationReport terminationReport = this.mFrameMapper.toTerminationReport( frame.getProcessRuntimeMeta() );
        if ( terminationReport == null || terminationReport.getPID() == null || terminationReport.getPID().isEmpty() ) {
            this.log.warn(
                    "[RemoteProcessTerminated] [gRPC] (ClientId: `{}`) <Invalid>",
                    session.clientId()
            );
            return;
        }

        if ( this.mRemoteProcessManagerServer instanceof RavenRemoteProcessManagerServer ) {
            RavenRemoteProcessManagerServer ravenServer = (RavenRemoteProcessManagerServer) this.mRemoteProcessManagerServer;
            RavenRemoteProcessManagerServer.RemoteTerminationAcceptance acceptance = ravenServer.acceptRemoteProcessTermination(
                    session.clientId(), terminationReport
            );
            if ( acceptance.isDuplicate() ) {
                this.log.info(
                        "[RemoteProcessTerminated] [gRPC] (ClientId: `{}`, PID: `{}`, ExitCode: `{}`) <Duplicate>",
                        session.clientId(), terminationReport.getPID(), terminationReport.getExitCode()
                );
                return;
            }
            if ( !acceptance.isAccepted() ) {
                this.log.warn(
                        "[RemoteProcessTerminated] [gRPC] (ClientId: `{}`, PID: `{}`) <Invalid>",
                        session.clientId(), terminationReport.getPID()
                );
                return;
            }

            UProcess process = acceptance.getProcess();
            String procName = process == null ? "NonExistent" : process.getName();
            this.log.info(
                    "[RemoteProcessTerminated] [gRPC] (ClientId: `{}`, PID: `{}`, ExitCode: `{}`) <Done>",
                    session.clientId(), terminationReport.getPID(), terminationReport.getExitCode()
            );
            this.log.info(
                    "[RemoteProcessTerminated] [gRPC] [MirrorUnhook] (ClientId: `{}`, PID: `{}`, Process: `{}`) <Done>",
                    session.clientId(), terminationReport.getPID(), procName
            );
            return;
        }

        UProcess that = RavenRemoteProcessManagerServer.invokeExpunge( this.mRemoteProcessManagerServer, terminationReport.getPID() );
        String procName = "NonExistent";
        if ( that instanceof RemoteProcess ) {
            procName = that.getName();
            RemoteProcess remoteProcess = (RemoteProcess) that;
            remoteProcess.notifyRemoteEvent( session.clientId(), UProcessStatus.Terminated, terminationReport );
        }
        this.log.info(
                "[RemoteProcessTerminated] [gRPC] (ClientId: `{}`, PID: `{}`, ExitCode: `{}`) <Done>",
                session.clientId(), terminationReport.getPID(), terminationReport.getExitCode()
        );
        this.log.info(
                "[RemoteProcessTerminated] [gRPC] [MirrorUnhook] (ClientId: `{}`, PID: `{}`, Process: `{}`) <Done>",
                session.clientId(), terminationReport.getPID(), procName
        );
    }

    @Override
    public void registerController( Object controller ) throws RemoteProcessServiceRPCException {
        this.mProcessorLifecycleController = controller;
        this.log.info( "[GrpcControlControllerRegistered] (Controller: `{}`) <Done>", controller.getClass().getName() );
    }

    @Override
    public void compileIface( Class<?> ifaceClass, boolean bAsIface ) throws RemoteProcessServiceRPCException {
        this.log.info( "[GrpcControlIfaceCompileSkipped] gRPC control uses generated lifecycle iface. <Pass>" );
    }

    @Override
    public void startService() throws RemoteProcessServiceRPCException {
        if ( this.mGrpcAppointServer == null ) {
            throw new RemoteProcessServiceRPCException( "GrpcAppointServer is required." );
        }

        if ( !this.mGrpcAppointServer.isShutdown() ) {
            this.log.info( "[GrpcControlTransportStarted] gRPC appoint server has already started. <Pass>" );
            return;
        }

        try {
            this.mGrpcAppointServer.serverBuilder().addService(
                    ServerInterceptors.intercept(
                            new GrpcRemoteProcessControlService( this ),
                            new GrpcRemoteAddressServerInterceptor()
                    )
            );
            if ( this.mProcessorLifecycleController != null ) {
                this.mGrpcAppointServer.serverBuilder().addService(
                        new GrpcProcessorLifecycleService( this.mProcessorLifecycleController )
                );
            }
            this.registerAdditionalGrpcServices();
            this.mGrpcAppointServer.execute();
            this.startHeartbeatGuardian();
        }
        catch ( Exception e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }

    /**
     * Instantiates and registers additional gRPC services listed in the
     * {@code additionalGrpcServices} config section.  Each class is loaded
     * via reflection and must either:
     * <ol>
     *   <li>Have a constructor accepting {@link GrpcRemoteProcessControlTransport}</li>
     *   <li>Have a no-arg constructor</li>
     * </ol>
     * <p>The check mode determines failure behaviour:
     * <ul>
     *   <li>{@code strict} — throw on any error (default)</li>
     *   <li>{@code warn} — log a warning and continue</li>
     * </ul>
     */
    protected void registerAdditionalGrpcServices() throws RemoteProcessServiceRPCException {
        List<String> classNames = this.mGrpcAppointServer.getConfig().getAdditionalGrpcServiceClassNames();
        if ( classNames == null || classNames.isEmpty() ) {
            return;
        }

        boolean bStrict = this.mGrpcAppointServer.getConfig().isAdditionalGrpcServicesStrictMode();

        for ( String szClassName : classNames ) {
            try {
                Class<?> clazz = Class.forName( szClassName );
                BindableService serviceInstance = this.instantiateGrpcService( clazz );
                this.mAdditionalGrpcServiceMap.put( serviceInstance.getClass(), serviceInstance );
                this.mGrpcAppointServer.serverBuilder().addService(
                        ServerInterceptors.intercept( serviceInstance, new GrpcRemoteAddressServerInterceptor() )
                );
                this.mEventHooker.onAdditionalServiceRegistered( this, serviceInstance );
                this.log.info(
                        "[GrpcControl] [AdditionalServiceRegistered] (ClassName: `{}`) <Done>",
                        szClassName
                );
            }
            catch ( Exception e ) {
                if ( bStrict ) {
                    throw new RemoteProcessServiceRPCException(
                            "Failed to register additional gRPC service: " + szClassName, e
                    );
                }
                this.log.warn(
                        "[GrpcControl] [AdditionalServiceFailed] (ClassName: `{}`, CheckMode: `warn`) <Skipped>: {}",
                        szClassName,
                        e.getMessage()
                );
            }
        }
    }

    protected BindableService instantiateGrpcService( Class<?> clazz ) throws Exception {
        // Try constructor(GrpcRemoteProcessControlTransport) first
        try {
            Constructor<?> ctor = clazz.getConstructor( GrpcRemoteProcessControlTransport.class );
            return (BindableService) ctor.newInstance( this );
        }
        catch ( NoSuchMethodException ignored ) {
            // fall through to try no-arg
        }

        // Try no-arg constructor
        try {
            Constructor<?> ctor = clazz.getConstructor();
            return (BindableService) ctor.newInstance();
        }
        catch ( NoSuchMethodException e ) {
            throw new IllegalArgumentException(
                    "Class " + clazz.getName() + " must have a constructor( GrpcRemoteProcessControlTransport ) or a no-arg constructor."
            );
        }
    }

    @Override
    public void terminateService() throws IllegalStateException {
        if ( this.mGrpcAppointServer == null ) {
            throw new IllegalStateException( "Grpc control transport dose not started yet." );
        }

        this.mGrpcAppointServer.shutdown();
        this.stopHeartbeatGuardian();
    }

    @Override
    public boolean isStarted() {
        return this.mGrpcAppointServer != null && !this.mGrpcAppointServer.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return this.mGrpcAppointServer == null || this.mGrpcAppointServer.isTerminated();
    }

    @Override
    public void startRemoteUProcess( long clientId, GUID pid ) throws RemoteProcessServiceRPCException {
        try {
            this.sendAndAwaitCommandResult( clientId, this.mFrameMapper.processIdFrame( clientId, this.nextGuidString(), RemoteProcessControlFrameType.START_REMOTE_PROCESS, pid ) );
        }
        catch ( Exception e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }

    @Override
    public RemoteProcessSignalResult signalRemoteUProcess( long clientId, GUID pid, ProcSignal signal, long graceTimeoutMillis, String szReason ) throws RemoteProcessLifecycleException {
        try {
            CommandResult result = this.sendAndAwaitCommandResult(
                    clientId,
                    this.mFrameMapper.processSignalFrame( clientId, this.nextGuidString(), pid, signal, graceTimeoutMillis, szReason )
            );
            return this.mFrameMapper.toSignalResult( result, pid, signal, szReason );
        }
        catch ( Exception e ) {
            throw new RemoteProcessLifecycleException( e );
        }
    }

    @Override
    public RemoteVitalizationResponse vitalizeRemoteUProcess( long clientId, UProcessMirrorDTO processDTO ) throws RemoteProcessLifecycleException {
        try {
            CommandResult result = this.sendAndAwaitCommandResult(
                    clientId,
                    this.mFrameMapper.commandFrame( clientId, this.nextGuidString(), RemoteProcessControlFrameType.VITALIZE_REMOTE_PROCESS, processDTO )
            );
            return this.mFrameMapper.toVitalizationResponse( result, processDTO );
        }
        catch ( Exception e ) {
            throw new RemoteProcessLifecycleException( e );
        }
    }

    @Override
    public RemoteVitalizationResponse createRemoteUProcess( long clientId, UProcessMirrorDTO processDTO ) throws RemoteProcessLifecycleException {
        try {
            CommandResult result = this.sendAndAwaitCommandResult(
                    clientId,
                    this.mFrameMapper.commandFrame( clientId, this.nextGuidString(), RemoteProcessControlFrameType.CREATE_REMOTE_PROCESS, processDTO )
            );
            return this.mFrameMapper.toVitalizationResponse( result, processDTO );
        }
        catch ( Exception e ) {
            throw new RemoteProcessLifecycleException( e );
        }
    }

    @Override
    public boolean hasOwnProcess( long clientId, GUID pid ) throws RemoteProcessServiceRPCException {
        return this.queryBooleanCommand( clientId, RemoteProcessControlFrameType.HAS_OWN_PROCESS, pid );
    }

    @Override
    public boolean containProcess( long clientId, GUID pid ) throws RemoteProcessServiceRPCException {
        return this.queryBooleanCommand( clientId, RemoteProcessControlFrameType.CONTAIN_PROCESS, pid );
    }

    @Override
    public UProcessRuntimeMeta queryProcessRuntimeMeta( long clientId, GUID pid ) throws RemoteProcessLifecycleException {
        try {
            Object result = this.sendAndAwait(
                    clientId,
                    this.mFrameMapper.processIdFrame( clientId, this.nextGuidString(), RemoteProcessControlFrameType.QUERY_PROCESS_RUNTIME_META, pid )
            );
            return this.mFrameMapper.toRuntimeMeta( (com.walnut.odin.proc.server.transport.grpc.lifecycle.ProcessRuntimeMeta) result );
        }
        catch ( Exception e ) {
            throw new RemoteProcessLifecycleException( e );
        }
    }

    protected boolean queryBooleanCommand( long clientId, RemoteProcessControlFrameType type, GUID pid ) throws RemoteProcessServiceRPCException {
        try {
            CommandResult result = this.sendAndAwaitCommandResult( clientId, this.mFrameMapper.processIdFrame( clientId, this.nextGuidString(), type, pid ) );
            return result != null && result.getSuccess();
        }
        catch ( Exception e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }

    protected CommandResult sendAndAwaitCommandResult( long clientId, RemoteProcessControlFrame frame ) throws Exception {
        return (CommandResult) this.sendAndAwait( clientId, frame );
    }

    protected Object sendAndAwait( long clientId, RemoteProcessControlFrame frame ) throws Exception {
        GrpcRemoteProcessControlSession session = this.resolveActiveSession( clientId );
        if ( session == null ) {
            this.log.warn(
                    "[GrpcControl] [CommandDispatch] (ClientId: `{}`, Type: `{}`, Correlation: `{}`) <NoActiveSession>",
                    clientId,
                    frame.getFrameType(),
                    frame.getCorrelationGuid()
            );
            throw new GrpcRemoteProcessControlException( "No active gRPC client session: " + clientId );
        }
        this.mCorrelationWaiter.prepare( frame.getCorrelationGuid() );
        session.send( frame );
        return this.mCorrelationWaiter.await( frame.getCorrelationGuid(), 30000L );
    }

    protected GrpcRemoteProcessControlSession resolveActiveSession( long clientId ) {
        GrpcRemoteProcessControlClientile clientile = this.mClientileMap.get( clientId );
        if ( clientile == null ) {
            return null;
        }
        for ( com.walnut.odin.proc.server.transport.RemoteProcessControlSession session : clientile.sessions() ) {
            if ( session instanceof GrpcRemoteProcessControlSession && session.isActive() ) {
                return (GrpcRemoteProcessControlSession) session;
            }
        }
        return null;
    }

    protected void startHeartbeatGuardian() {
        if ( !this.mGrpcAppointServer.getConfig().isEnableHeartbeat() ) {
            return;
        }
        long nHeartbeatIntervalMillis = this.mGrpcAppointServer.getConfig().getHeartbeatIntervalMillis();
        if ( nHeartbeatIntervalMillis <= 0 ) {
            return;
        }
        if ( this.mHeartbeatGuardian != null ) {
            return;
        }
        this.mHeartbeatGuardian = Executors.newSingleThreadScheduledExecutor( runnable -> {
            Thread thread = new Thread( runnable, "odin-grpc-heartbeat-guardian" );
            thread.setDaemon( true );
            return thread;
        } );
        this.mHeartbeatGuardian.scheduleAtFixedRate(
                this::evictSilentSessions,
                nHeartbeatIntervalMillis,
                nHeartbeatIntervalMillis,
                TimeUnit.MILLISECONDS
        );
        this.log.info(
                "[GrpcControl] [HeartbeatGuardian] (IntervalMillis: `{}`, IdleTimeoutMillis: `{}`) <Started>",
                nHeartbeatIntervalMillis,
                this.controlIdleTimeoutMillis()
        );
    }

    protected void stopHeartbeatGuardian() {
        if ( this.mHeartbeatGuardian == null ) {
            return;
        }
        this.mHeartbeatGuardian.shutdownNow();
        this.mHeartbeatGuardian = null;
        this.log.info( "[GrpcControl] [HeartbeatGuardian] <Stopped>" );
    }

    protected void evictSilentSessions() {
        long nNow = System.currentTimeMillis();
        long nIdleTimeoutMillis = this.controlIdleTimeoutMillis();
        for ( GrpcRemoteProcessControlClientile clientile : this.mClientileMap.values() ) {
            for ( RemoteProcessControlSession controlSession : clientile.sessions() ) {
                if ( !( controlSession instanceof GrpcRemoteProcessControlSession ) ) {
                    continue;
                }
                GrpcRemoteProcessControlSession session = (GrpcRemoteProcessControlSession) controlSession;
                if ( !session.isActive() ) {
                    continue;
                }
                long nSilentMillis = nNow - session.lastActiveTimeMillis();
                if ( nSilentMillis <= nIdleTimeoutMillis ) {
                    continue;
                }
                this.log.warn(
                        "[GrpcControl] [HeartbeatTimeout] (ClientId: `{}`, Session: `{}`, RemoteAddress: `{}`, SilentMillis: `{}`, IdleTimeoutMillis: `{}`, LastActiveTimeMillis: `{}`, LastHeartbeatTimeMillis: `{}`) <Detach>",
                        session.clientId(),
                        session.sessionGuid(),
                        session.remoteAddress(),
                        nSilentMillis,
                        nIdleTimeoutMillis,
                        session.lastActiveTimeMillis(),
                        session.lastHeartbeatTimeMillis()
                );
                session.closeByServer( "ODIN_GRPC_HEARTBEAT_TIMEOUT" );
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
