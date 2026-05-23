package com.walnut.odin.proc.client;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.LocalUProcess;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.wolf.WolvesAppointClient;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.event.ChannelEventHandler;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.walnut.odin.proc.ArchRemoteProcessManagerNode;
import com.walnut.odin.proc.ProcessesUtils;
import com.walnut.odin.proc.RemoteProcess;
import com.walnut.odin.proc.RemoteProcessLifecycleExaminer;
import com.walnut.odin.proc.ProcessLifecycleExaminer;
import com.walnut.odin.proc.RemoteProcessLifecycleException;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.RemoteVitalizationStatus;
import com.walnut.odin.proc.control.RemoteProcessControlFrameIface;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class RavenRemoteProcessManagerClient extends ArchRemoteProcessManagerNode implements RemoteProcessManagerClient {

    protected DuplexAppointClient            mDuplexAppointClient;

    protected SlaveProcessLifecycleIface     mProcessLifecycleIface;

    protected ProcessLifecycleExaminer       mProcessLifecycleExaminer;

    protected RemoteProcessControlFrameIface mControlFrameIface;

    protected RemoteProcessControlStateSynchronizer mStateSynchronizer;

    protected ChannelEventHandler            mControlChannelConnectedHandler;

    protected boolean                        mbControlSubsystemReady;

    protected long                           mnClientId;

    protected UlfClient                      mRPCClient;

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
                    return;
                }

                RavenRemoteProcessManagerClient.this.requestControlStateSynchronization( RemoteProcessControlSyncReasons.ChannelConnected );
            }
        };
        this.mRPCClient.registerChannelConnectedHandler( this.mControlChannelConnectedHandler );
    }

    protected void requestControlStateSynchronization( String szReason ) {
        if ( this.mStateSynchronizer == null ) {
            return;
        }
        this.mStateSynchronizer.requestSynchronize( szReason );
    }

    protected void synchronizeControlStateBlocking( String szReason ) {
        if ( this.mStateSynchronizer == null ) {
            return;
        }
        this.mStateSynchronizer.synchronizeBlocking( szReason );
    }

    protected void vitalizeRPCSubsystem() throws RemoteProcessServiceRPCException {
        try {
            if ( this.mDuplexAppointClient.getMessageNode().isTerminated() ) {
                this.mDuplexAppointClient.execute();
                this.mDuplexAppointClient.embraces( 2 );
                this.mbControlSubsystemReady = true;
                this.synchronizeControlStateBlocking( RemoteProcessControlSyncReasons.Startup );

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

        this.mbControlSubsystemReady = false;
        this.mDuplexAppointClient.terminate();
        if ( this.mControlChannelConnectedHandler != null ) {
            this.mRPCClient.deregisterChannelConnectedHandler( this.mControlChannelConnectedHandler );
            this.mControlChannelConnectedHandler = null;
        }
        this.mDuplexAppointClient = null;
    }

    @Override
    public UProcess createLocalUProcess( ExecutionImage image, UProcess parent, Map<String, String> startupArgs, Map<String, String> contextEnvironmentVars ) {
        LocalUProcess localHostedProcess = this.mProcessManager.createLocalHostedProcess( image, parent, startupArgs, contextEnvironmentVars );

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
        try {
            String imageAddress = handlerDTO.getImageAddress();
            boolean isURI       = handlerDTO.isImageAddressURI();
            RemoteVitalizationResponse response = new RemoteVitalizationResponse();
            response.setRemoteVitalizationStatus( RemoteVitalizationStatus.New );
            response.setImageResolutionMode( handlerDTO.getImageResolutionMode() );

            this.notifyProcessLifecycleHandlers( imageAddress, null, UProcessStatus.Preparing );

            ExecutionImage image;
            if ( isURI ) {
                URI uri = new URI( imageAddress );
                image = this.queryExecutionImage( uri );
            }
            else {
                image = this.queryExecutionImage( imageAddress );
            }
            if ( image == null ) {
                response.setRemoteVitalizationStatus( RemoteVitalizationStatus.NoImage );
                return response;
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

            LocalUProcess localHostedProcess = this.mProcessManager.createLocalHostedProcess( image, this.mProcessManager.getRootUProcess(), startupArgs, envVariables );
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
            throw new RemoteProcessLifecycleException( e );
        }
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
    public DuplexAppointClient duplexAppointClient() {
        return this.mDuplexAppointClient;
    }
}

