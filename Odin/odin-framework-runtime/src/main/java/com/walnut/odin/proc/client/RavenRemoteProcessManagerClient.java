package com.walnut.odin.proc.client;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.LocalUProcess;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.wolf.WolvesAppointClient;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.event.ChannelEventHandler;
import com.pinecone.hydra.umc.wolf.client.UlfAsyncMessengerChannelControlBlock;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.walnut.odin.proc.ArchRemoteProcessManagerNode;
import com.walnut.odin.proc.ProcessesUtils;
import com.walnut.odin.proc.RemoteProcess;
import com.walnut.odin.proc.RemoteProcessLifecycleExaminer;
import com.walnut.odin.proc.ProcessLifecycleExaminer;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.RemoteTerminationStatus;
import com.pinecone.hydra.proc.signal.ProcessSignalHandler;
import com.pinecone.hydra.proc.signal.ProcSignal;
import com.pinecone.hydra.proc.signal.SignalHandleResult;
import com.walnut.odin.proc.RemoteVitalizationStatus;
import com.walnut.odin.proc.control.RemoteProcessControlFrameIface;
import com.walnut.odin.proc.entity.RemoteProcessSignalResult;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;
import com.pinecone.hydra.umct.husky.HuskyCTPConstants;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class RavenRemoteProcessManagerClient extends ArchRemoteProcessManagerNode implements RemoteProcessManagerClient {

    public interface ControlStateSynchronizedHandler {
        void afterControlStateSynchronized( String szReason );
    }

    protected static final int          ControlPassiveChannelLine = 2;

    protected DuplexAppointClient            mDuplexAppointClient;

    protected SlaveProcessLifecycleIface     mProcessLifecycleIface;

    protected ProcessLifecycleExaminer       mProcessLifecycleExaminer;

    protected RemoteProcessControlFrameIface mControlFrameIface;

    protected RemoteProcessControlStateSynchronizer mStateSynchronizer;

    protected ChannelEventHandler            mControlChannelConnectedHandler;

    protected List<ControlStateSynchronizedHandler> mControlStateSynchronizedHandlers = new CopyOnWriteArrayList<>();

    protected ReentrantLock                  mControlRecoveryLock = new ReentrantLock();

    protected boolean                        mbControlSubsystemReady;

    protected long                           mnClientId;

    protected UlfClient                      mRPCClient;

    protected ConcurrentMap<GUID, RemoteTerminationStatus> mSignalTerminationStatuses = new ConcurrentHashMap<>();

    public RavenRemoteProcessManagerClient( ProcessManager processManager, UlfClient rpcClient ) {
        super( processManager );
        this.mRPCClient                = rpcClient;
        this.mnClientId                = rpcClient.getMessageNodeId();
    }


    protected void initRPCSubsystem() throws RemoteProcessServiceRPCException {
        if ( this.mDuplexAppointClient != null && !this.mDuplexAppointClient.getMessageNode().isTerminated() ) {
            throw new IllegalStateException( "DuplexAppointClient has started." );
        }

        this.mDuplexAppointClient = new WolvesAppointClient( this.mRPCClient );
        try {
            this.mDuplexAppointClient.compile( SlaveProcessLifecycleIface.class, false );
            this.mDuplexAppointClient.compile( RemoteProcessControlFrameIface.class, false );
            this.mProcessLifecycleIface = this.mDuplexAppointClient.getIface( SlaveProcessLifecycleIface.class );
            this.mControlFrameIface     = this.mDuplexAppointClient.getIface( RemoteProcessControlFrameIface.class );
            this.mDuplexAppointClient.getRouteDispatcher().registerController( new ReactiveMasterProcessLifecycleController( this ) );

            this.mProcessLifecycleExaminer = new RemoteProcessLifecycleExaminer( this, this.mProcessLifecycleIface );
            this.mStateSynchronizer        = new RemoteProcessControlStateSynchronizer( this, this.mControlFrameIface );
            this.registerControlChannelConnectedHandler();
            this.infoLifecycle( "RPC Subsystem Register Controllers", LogStatuses.StatusDone );
        }
        catch ( Exception e ) {
            this.mProcessLifecycleIface = null;
            this.mControlFrameIface     = null;
            this.mStateSynchronizer     = null;
            throw new RemoteProcessServiceRPCException( e );
        }
    }

    protected void registerControlChannelConnectedHandler() {
        if ( this.mControlChannelConnectedHandler != null ) {
            return;
        }

        this.mControlChannelConnectedHandler = new ChannelEventHandler() {
            @Override
            public void afterEventTriggered( ChannelControlBlock block, Object context ) {
                if ( !RavenRemoteProcessManagerClient.this.mbControlSubsystemReady ) {
                    RavenRemoteProcessManagerClient.this.getLogger().info(
                            "[RemoteProcessControlSync] [ChannelConnected] (ClientId: `{}`) <SubsystemNotReady>",
                            RavenRemoteProcessManagerClient.this.mnClientId
                    );
                    return;
                }

                if ( RavenRemoteProcessManagerClient.this.isControlPassiveChannel( block ) ) {
                    RavenRemoteProcessManagerClient.this.getLogger().info(
                            "[RemoteProcessControlSync] [ChannelConnected] (ClientId: `{}`) <PassiveChannelPass>",
                            RavenRemoteProcessManagerClient.this.mnClientId
                    );
                    return;
                }

                RavenRemoteProcessManagerClient.this.getLogger().info(
                        "[RemoteProcessControlSync] [ChannelConnected] (ClientId: `{}`) <Requested>",
                        RavenRemoteProcessManagerClient.this.mnClientId
                );
                RavenRemoteProcessManagerClient.this.requestControlStateSynchronization( RemoteProcessControlSyncReasons.ChannelConnected );
            }
        };
        this.mRPCClient.registerChannelConnectedHandler( this.mControlChannelConnectedHandler );
    }

    protected boolean isControlPassiveChannel( ChannelControlBlock block ) {
        if ( !( block instanceof UlfAsyncMessengerChannelControlBlock ) ) {
            return false;
        }

        Channel channel = ( (UlfAsyncMessengerChannelControlBlock)block ).getChannel().getNativeHandle();
        Object passive = channel.attr( AttributeKey.valueOf( HuskyCTPConstants.HCTP_DUP_PASSIVE_CHANNEL_KEY ) ).get();
        return passive instanceof Boolean && (Boolean)passive;
    }

    protected void requestControlStateSynchronization( String szReason ) {
        if ( this.mStateSynchronizer == null ) {
            return;
        }
        this.mStateSynchronizer.requestSynchronize( szReason );
    }

    protected boolean synchronizeControlStateBlocking( String szReason ) {
        if ( this.mStateSynchronizer == null ) {
            return false;
        }
        return this.mStateSynchronizer.synchronizeBlocking( szReason );
    }

    public void registerControlStateSynchronizedHandler( ControlStateSynchronizedHandler handler ) {
        if ( handler == null ) {
            return;
        }
        this.mControlStateSynchronizedHandlers.add( handler );
    }

    public void deregisterControlStateSynchronizedHandler( ControlStateSynchronizedHandler handler ) {
        if ( handler == null ) {
            return;
        }
        this.mControlStateSynchronizedHandlers.remove( handler );
    }

    protected void notifyControlStateSynchronized( String szReason ) {
        for ( ControlStateSynchronizedHandler handler : this.mControlStateSynchronizedHandlers ) {
            try {
                handler.afterControlStateSynchronized( szReason );
            }
            catch ( Exception e ) {
                this.getLogger().warn(
                        "[RemoteProcessControlSync] [Handler] (Reason: `{}`) <Failure>",
                        szReason,
                        e
                );
            }
        }
    }

    protected void recoverControlPassiveChannels( String szReason, Throwable cause ) {
        if ( !( this.mDuplexAppointClient instanceof WolvesAppointClient ) ) {
            return;
        }
        if ( !this.mControlRecoveryLock.tryLock() ) {
            return;
        }

        try {
            this.getLogger().info(
                    "[RemoteProcessControlRecovery] Passive channel rebuild started. (Reason: `{}`) <Start>",
                    szReason
            );
            ( (WolvesAppointClient)this.mDuplexAppointClient ).rebuildPassiveChannels( ControlPassiveChannelLine );
            this.getLogger().info(
                    "[RemoteProcessControlRecovery] Passive channel rebuild done. (Reason: `{}`) <Done>",
                    szReason
            );
        }
        catch ( Exception e ) {
            if ( cause != null && cause != e ) {
                e.addSuppressed( cause );
            }
            this.getLogger().warn(
                    "[RemoteProcessControlRecovery] Passive channel rebuild failed. (Reason: `{}`) <Failure>",
                    szReason,
                    e
            );
        }
        finally {
            this.mControlRecoveryLock.unlock();
        }
    }

    protected void vitalizeRPCSubsystem() throws RemoteProcessServiceRPCException {
        try {
            if ( this.mDuplexAppointClient.getMessageNode().isTerminated() ) {
                this.mDuplexAppointClient.execute();
                this.mDuplexAppointClient.embraces( ControlPassiveChannelLine );
                this.mbControlSubsystemReady = this.synchronizeControlStateBlocking( RemoteProcessControlSyncReasons.Startup );
                if ( !this.mbControlSubsystemReady ) {
                    throw new RemoteProcessServiceRPCException( "Remote process control synchronization failed during startup." );
                }

                this.infoLifecycle( "RPC Subsystem Service Vitalization, ( ClientId: `" + this.mnClientId + "` )", LogStatuses.StatusDone );
            }
        }
        catch ( Exception e ) {
            throw new RemoteProcessServiceRPCException( e );
        }
    }


    @Override
    public long getClientId() {
        return this.mnClientId;
    }

    @Override
    public void startService() throws RemoteProcessServiceRPCException {
        this.initRPCSubsystem();
        this.vitalizeRPCSubsystem();
    }

    @Override
    public void terminateService() {
        if ( this.mDuplexAppointClient == null ) {
            throw new IllegalStateException( "RPCClient dose not started yet." );
        }

        DuplexAppointClient appointClient = this.mDuplexAppointClient;
        this.mbControlSubsystemReady = false;
        this.mDuplexAppointClient = null;
        this.mControlChannelConnectedHandler = null;
        try {
            appointClient.terminate();
        }
        finally {
            appointClient.close();
        }
    }

    @Override
    public UProcess createLocalUProcess( ExecutionImage image, UProcess parent, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars ) {
        LocalUProcess localHostedProcess = this.mProcessManager.createLocalHostedProcessPrototypically( image, parent, startupArgs, contextEnvironmentVars );

        if ( this.mStateSynchronizer != null ) {
            this.mStateSynchronizer.reportProcessMirror( localHostedProcess );
            this.getLogger().info( "[SuperiorRegister] [createLocalUProcess] <Done>" );
        }
        else if ( this.mProcessLifecycleIface != null ) {
            UProcessMirrorDTO processMirrorDTO = new UProcessMirrorDTO( localHostedProcess.getName(), localHostedProcess.getLocalPID(), localHostedProcess.getGuid().toString() );
            this.mProcessLifecycleIface.registerRemoteProcess( this.mnClientId, processMirrorDTO );
            this.getLogger().info( "[SuperiorRegister] [createLocalUProcess] <Done::Legacy>" );
        }
        else {
            this.getLogger().info( "[SuperiorRegister] [createLocalUProcess] <Pass>" ); // Missing central connection, skip reporting; 失联，跳过上报中央.
        }
        return localHostedProcess;
    }

    @Override
    public RemoteVitalizationResponse createLocalUProcess( UProcessMirrorDTO handlerDTO, UProcess[] lpProcess ) throws RemoteProcessLifecycleException {
        String imageAddress = handlerDTO == null ? null : handlerDTO.getImageAddress();
        boolean isURI       = handlerDTO != null && handlerDTO.isImageAddressURI();
        RemoteVitalizationResponse response = this.createRemoteVitalizationResponse(
                handlerDTO, RemoteVitalizationStatus.New, null
        );
        try {
            this.notifyProcessLifecycleHandlers( imageAddress, null, UProcessStatus.Preparing );

            ExecutionImage image;
            if ( isURI ) {
                URI uri = new URI( imageAddress );
                if ( uri.getScheme() == null ) {
                    return this.markVitalizationFailure(
                            response,
                            RemoteVitalizationStatus.NoImage,
                            "Invalid image URI without scheme: `" + imageAddress + "`"
                    );
                }
                image = this.queryExecutionImage( uri );
            }
            else {
                if ( imageAddress == null || imageAddress.trim().isEmpty() ) {
                    return this.markVitalizationFailure(
                            response,
                            RemoteVitalizationStatus.NoImage,
                            "Invalid empty execution image address."
                    );
                }
                image = this.queryExecutionImage( imageAddress );
            }
            if ( image == null ) {
                return this.markVitalizationFailure(
                        response,
                        RemoteVitalizationStatus.NoImage,
                        "Execution image not found: `" + imageAddress + "`"
                );
            }
            this.mProcessManager.getImageModifier().applyImageAddress( image, imageAddress );

            String szStartupArguments      = handlerDTO.getStartupArguments();
            String szEnvironmentVariables  = handlerDTO.getEnvironmentVariables();
            String szParentPID             = handlerDTO.getParentPID();

            Map<String, String> startupArgs  = ProcessesUtils.decode( szStartupArguments );
            Map<String, String> envVariables = ProcessesUtils.decode( szEnvironmentVariables );
            GUID parentPID = null;
            if ( szParentPID != null ) {
                parentPID = this.mProcessManager.getGuidAllocator().parse( szParentPID );
            }

            LocalUProcess localHostedProcess = this.mProcessManager.createLocalHostedProcessPrototypically( image, this.mProcessManager.getRootUProcess(), startupArgs, envVariables );
            localHostedProcess.applyActualParentPID( parentPID );
            response.setName( localHostedProcess.getName() );
            response.setProcessID( localHostedProcess.getPID() );
            response.setLocalPID( localHostedProcess.getLocalPID() );
            response.setEnvironmentVariables( szEnvironmentVariables );
            response.setStartupArguments( szStartupArguments );

            response.setImageAddress(imageAddress);
            response.setImageAddressURI(isURI);
            response.setImageResolutionMode( handlerDTO.getImageResolutionMode() );

            if ( lpProcess != null && lpProcess.length > 0 ) {
                lpProcess[0] = localHostedProcess;
            }

            this.notifyProcessLifecycleHandlers( imageAddress, null, UProcessStatus.Created );
            return response;
        }
        catch ( URISyntaxException e ) {
            return this.markVitalizationFailure(
                    response,
                    RemoteVitalizationStatus.NoImage,
                    "Invalid image URI: `" + imageAddress + "`, cause: " + e.getMessage()
            );
        }
        catch ( Exception e ) {
            return this.markVitalizationFailure(
                    response,
                    RemoteVitalizationStatus.Error,
                    "Remote local process creation failed for image `" + imageAddress + "`, cause: " + this.describeThrowable( e )
            );
        }
    }

    protected RemoteVitalizationResponse createRemoteVitalizationResponse(
            UProcessMirrorDTO handlerDTO, RemoteVitalizationStatus status, String errorMsg
    ) {
        RemoteVitalizationResponse response = new RemoteVitalizationResponse();
        response.setRemoteVitalizationStatus( status );
        response.setErrorMsg( errorMsg );
        if ( handlerDTO == null ) {
            return response;
        }
        response.setImageAddress( handlerDTO.getImageAddress() );
        response.setImageAddressURI( handlerDTO.isImageAddressURI() );
        try {
            response.setImageResolutionMode( handlerDTO.getImageResolutionMode() );
        }
        catch ( Exception e ) {
            response.setErrorMsg( this.describeThrowable( e ) );
        }
        return response;
    }

    protected RemoteVitalizationResponse markVitalizationFailure(
            RemoteVitalizationResponse response, RemoteVitalizationStatus status, String errorMsg
    ) {
        response.setRemoteVitalizationStatus( status );
        response.setErrorMsg( errorMsg );
        this.getLogger().warn(
                "[RemoteProcessCreation] [PRC] (Process: `{}`, Status: `{}`, Reason: `{}`) <Rejected>",
                response.getImageAddress(),
                status,
                errorMsg
        );
        return response;
    }

    protected String describeThrowable( Throwable cause ) {
        if ( cause == null ) {
            return null;
        }
        String message = cause.getMessage();
        if ( message == null || message.trim().isEmpty() ) {
            return cause.getClass().getName();
        }
        return cause.getClass().getName() + ": " + message;
    }

    @Override
    public RemoteVitalizationResponse vitalizeLocalUProcess( UProcessMirrorDTO handlerDTO ) throws RemoteProcessLifecycleException {
        UProcess[] lpProcess = new UProcess[1];
        RemoteVitalizationResponse response = this.createLocalUProcess( handlerDTO, lpProcess );
        LocalUProcess localHostedProcess = (LocalUProcess) lpProcess[ 0 ];

        if ( response.getStatus() != RemoteVitalizationStatus.New.getCode() && response.getStatus() != RemoteVitalizationStatus.Vitalized.getCode() ) {
            return response;
        }

        // Asynchronous startup may cause consistency errors if local execution finishes before the remote mirror is ready to handle events.
        // Sync and confirmation are required.
        // 进程启动为异步过程，若本地执行过快，远端镜像未就绪即本地完成（远端进程可能无法被后续事件清理），将导致一致性错误，需上报并等待同步。
        // Note: Strong consistency is required. RPC sync must precede remote mirror process initialization.
        // PS：该过程要求强一致性，必须先通过 RPC 同步，等待远端镜像进程完成创建。
        String pid = this.mProcessLifecycleIface.reportProcessCreated( this.mnClientId, response );
        if ( !response.getPID().equals( pid ) ) {
            throw new RemoteProcessLifecycleException( "An internal error has been happened, whit unmatched remote-process PID." );
        }

        this.mProcessLifecycleExaminer.startProcess( localHostedProcess );

        return response;
    }

    @Override
    public void startLocalUProcess( GUID pid ) throws IllegalArgumentException {
        UProcess process = this.mProcessManager.getProcess( pid );
        if ( process == null ) {
            throw new IllegalArgumentException( "No such process, PID => `" + pid + "`" );
        }

        this.mProcessLifecycleExaminer.startProcess( process );
    }

    @Override
    public void register( UProcess that ) {
        this.mProcessManager.register( that );
    }

    @Override
    public void erase( UProcess that ) {
        this.mProcessManager.erase( that );
    }

    @Override
    public UProcessRuntimeMeta queryProcessRuntimeMeta( GUID pid ) throws RemoteProcessLifecycleException {
        UProcess process = this.mProcessManager.getProcess( pid );
        if ( process instanceof RemoteProcess ) {
            RemoteProcess remoteProcess = (RemoteProcess) process;
            return remoteProcess.retrieveRemoteRuntimeMeta(); // Cascading retrieval of runtime meta information
        }

        if ( process == null ) {
            return null;
        }

        UProcessRuntimeMeta meta = ProcessesUtils.extractProcessMeta( process );
        // 不要直接return 老子好打断点.
        return meta;
    }

    @Override
    public RemoteProcessSignalResult signalLocalUProcess( GUID pid, ProcSignal signal, long graceTimeoutMillis, String szReason ) {
        ProcSignal appliedSignal = signal == null ? ProcSignal.SIGTERM : signal;
        UProcess process = this.mProcessManager.getProcess( pid );
        RemoteProcessSignalResult result = this.newSignalResult( pid, appliedSignal, process != null );
        result.setReason( szReason );
        if ( process == null ) {
            result.setMessage( "Local process not found." );
            result.setOperatorActionRequired( true );
            return result;
        }

        this.registerSignalTerminationStatus( pid, appliedSignal );
        try {
            SignalHandleResult entryPointResult = this.signalEntryPoint( process, appliedSignal, graceTimeoutMillis, szReason );
            if ( entryPointResult != null ) {
                this.applySignalHandleResult( result, entryPointResult );
            }
            else {
                this.interpretSignal( process, appliedSignal, result );
            }
        }
        catch ( Exception e ) {
            this.consumeSignalTerminationStatus( pid );
            result.setAccepted( false );
            result.setMessage( this.describeThrowable( e ) );
            result.setOperatorActionRequired( true );
            return result;
        }

        boolean terminal = process.getStatus() != null && process.getStatus().isTerminal();
        if ( appliedSignal == ProcSignal.SIGKILL && result.getTerminator() == null ) {
            result.setSchedulerClosed( true );
            result.setPhysicalClosed( terminal );
            result.setOperatorActionRequired( !terminal );
            if ( !terminal ) {
                result.setMessage( "Java internal process interpreted SIGKILL; physical closure is cooperative." );
                this.getLogger().warn(
                        "[RemoteProcessSignal] [JavaInternal] (ProcessId: `{}`, Signal: `{}`) <CooperativeClosureRequired>",
                        pid,
                        appliedSignal
                );
            }
        }
        else {
            if ( !result.isSchedulerClosed() ) {
                result.setSchedulerClosed( terminal );
            }
            if ( !result.isPhysicalClosed() ) {
                result.setPhysicalClosed( terminal );
            }
            result.setOperatorActionRequired( result.isOperatorActionRequired() || appliedSignal == ProcSignal.SIGKILL && !result.isPhysicalClosed() );
        }
        return result;
    }

    protected SignalHandleResult signalEntryPoint(
            UProcess process, ProcSignal signal, long graceTimeoutMillis, String szReason
    ) {
        if ( process == null || process.getExecutionImage() == null ) {
            return null;
        }
        EntryPointRunnable entryPoint = process.getEntryPoint();
        if ( !( entryPoint instanceof ProcessSignalHandler ) ) {
            return null;
        }
        ProcessSignalHandler handler = (ProcessSignalHandler)entryPoint;
        if ( !handler.supports( signal ) ) {
            return null;
        }
        return handler.signal( signal, graceTimeoutMillis, szReason );
    }

    protected void applySignalHandleResult( RemoteProcessSignalResult target, SignalHandleResult source ) {
        if ( source == null || target == null ) {
            return;
        }
        target.setAccepted( source.isAccepted() );
        target.setSchedulerClosed( source.isSchedulerClosed() );
        target.setPhysicalClosed( source.isPhysicalClosed() );
        target.setOperatorActionRequired( source.isOperatorActionRequired() );
        target.setTerminator( source.getTerminator() );
        target.setMessage( source.getMessage() );
    }

    protected void interpretSignal( UProcess process, ProcSignal appliedSignal, RemoteProcessSignalResult result ) {
        switch ( appliedSignal ) {
            case SIGINT:
                process.interrupt();
                result.setMessage( "Local process interrupt signal accepted." );
                break;
            case SIGKILL:
                process.kill();
                result.setMessage( "Local process kill signal accepted." );
                break;
            case SIGTERM:
            default:
                process.apoptosis();
                result.setMessage( "Local process apoptosis signal accepted." );
                break;
        }
    }

    protected void registerSignalTerminationStatus( GUID pid, ProcSignal signal ) {
        if ( pid == null ) {
            return;
        }
        this.mSignalTerminationStatuses.put( pid, this.toSignalTerminationStatus( signal ) );
    }

    @Override
    public RemoteTerminationStatus consumeSignalTerminationStatus( GUID pid ) {
        if ( pid == null ) {
            return null;
        }
        return this.mSignalTerminationStatuses.remove( pid );
    }

    protected RemoteTerminationStatus toSignalTerminationStatus( ProcSignal signal ) {
        if ( signal == null ) {
            return RemoteTerminationStatus.SignalApoptosis;
        }
        switch ( signal ) {
            case SIGINT:
                return RemoteTerminationStatus.SignalInterrupted;
            case SIGKILL:
                return RemoteTerminationStatus.SignalElimination;
            case SIGTERM:
            default:
                return RemoteTerminationStatus.SignalApoptosis;
        }
    }

    protected RemoteProcessSignalResult newSignalResult( GUID pid, ProcSignal signal, boolean accepted ) {
        RemoteProcessSignalResult result = new RemoteProcessSignalResult();
        result.setProcessId( pid == null ? null : pid.toString() );
        result.setSignal( signal == null ? ProcSignal.SIGTERM.name() : signal.name() );
        result.setAccepted( accepted );
        result.setTransport( "husky-local" );
        result.setTerminator( "odin-uprocess" );
        result.setSchedulerClosed( false );
        result.setPhysicalClosed( false );
        result.setOperatorActionRequired( false );
        return result;
    }

    @Override
    public DuplexAppointClient duplexAppointClient() {
        return this.mDuplexAppointClient;
    }
}
