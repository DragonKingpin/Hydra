package com.walnut.odin.task.troll;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.CollectionUtils;
import com.pinecone.framework.util.datetime.DatePattern;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.event.ProcessEvent;
import com.pinecone.hydra.proc.event.ProcessEventHandler;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.ImageLoader;
import com.pinecone.hydra.proc.image.ImageModifier;
import com.pinecone.hydra.proc.image.URLImageLoader;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.pinecone.hydra.system.ko.MetaPersistenceException;
import com.pinecone.hydra.task.TaskInstanceExecState;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.walnut.odin.conduct.CollectiveTaskRegiment;
import com.walnut.odin.conduct.lifecycle.TaskInstanceLifecycleInstrument;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionReason;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionResult;
import com.walnut.odin.proc.ProcessRemoteEventHandler;
import com.walnut.odin.proc.RemoteProcess;
import com.walnut.odin.proc.RemoteVitalizationStatus;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.mapper.InstanceExecMapper;

public class TrollTaskExecutionLauncher implements TaskExecutionLauncher, Slf4jTraceable {

    protected Logger mLogger;

    protected RemoteProcessManagerServer mRemoteProcessManagerServer;

    protected CollectiveTaskRegiment mCollectiveTaskRegiment;

    protected CentralizedTaskInstrument mTaskInstrument;

    protected InstanceInstrument mInstanceInstrument;

    protected InstanceExecMapper mInstanceExecMapper;

    protected TaskInstanceLifecycleInstrument mTaskInstanceLifecycleInstrument;

    protected ProcessManager mProcessManager;

    protected RavenTaskConfig mRavenTaskConfig;

    protected DateTimeFormatter mInstanceTitleTimeFormat;

    protected DateTimeFormatter mDefaultDateTimeFormat;

    protected GuidAllocator mGuidAllocator;

    protected ImageModifier mImageModifier;


    public TrollTaskExecutionLauncher( CollectiveTaskRegiment taskRegiment ) {
        this.mLogger                      = LoggerFactory.getLogger( this.getClass() );
        this.mRemoteProcessManagerServer  = taskRegiment.remoteProcessManagerServer();
        this.mProcessManager              = taskRegiment.processManager();
        this.mCollectiveTaskRegiment      = taskRegiment;
        this.mTaskInstrument              = taskRegiment.taskInstrument();
        this.mInstanceInstrument          = this.mTaskInstrument.getInstanceInstrument();
        this.mInstanceExecMapper          = this.mTaskInstrument.getRavenTaskMasterManipulator().getScheduleManipulator().getInstanceExecMapper();
        this.mTaskInstanceLifecycleInstrument = taskRegiment.taskInstanceLifecycleInstrument();
        this.mRavenTaskConfig             = (RavenTaskConfig) this.mTaskInstrument.getConfig();
        this.mGuidAllocator               = this.mTaskInstrument.getGuidAllocator();
        this.mInstanceTitleTimeFormat     = DatePattern.createFormatter( this.mRavenTaskConfig.getInstanceTitleTimeFormat() );
        this.mDefaultDateTimeFormat       = DatePattern.createFormatter( this.mRavenTaskConfig.getDefaultDateTimeFormat() );
        this.mImageModifier               = this.mProcessManager.getImageModifier();

        this.infoLifecycle( "Welcome to use Odin Troll task execution system.", LogStatuses.StatusReady );
    }

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }

    @Override
    public ProcessManager processManager() {
        return this.mProcessManager;
    }

    @Override
    public LocalDateTime evalBusinessTime( RavenTaskInstance instance, LocalDateTime biz ) {
        TaskScheduleCycle cycle = instance.getKernelScheduleCycle();

        LocalDateTime adjustedTime;

        switch ( cycle ) {
            case Month:
            case Week:
            case Day: {
                adjustedTime = biz.withHour(0).withMinute(0).withSecond(0).withNano(0);
                break;
            }
            case Hour: {
                adjustedTime = biz.withMinute(0).withSecond(0).withNano(0);
                break;
            }
            case Minute: {
                adjustedTime = biz.withSecond(0).withNano(0);
                break;
            }
            case Undefined:
            default: {
                adjustedTime = biz;
                break;
            }
        }

        return adjustedTime;
    }

    @Override
    public LocalDateTime evalBusinessTime( RavenTaskInstance instance ) {
        return this.evalBusinessTime( instance, LocalDateTime.now() );
    }

    @Override
    public String evalBusinessTimeLabel( RavenTaskInstance instance, LocalDateTime biz ) {
        return this.evalBusinessTime( instance, biz ).format( this.mInstanceTitleTimeFormat );
    }

    @Override
    public String evalBusinessTimeLabel( RavenTaskInstance instance ) {
        return this.evalBusinessTime( instance ).format( this.mInstanceTitleTimeFormat );
    }

    @Override
    public String evalInstanceName( RavenTaskInstance instance, LocalDateTime now, LocalDateTime bizTimeEpoch ) {
        String bizTimeLab     = this.evalBusinessTimeLabel( instance, bizTimeEpoch );
        String execTimeLab    = now.format( this.mInstanceTitleTimeFormat );
        String szInstanceName = String.format(
                "%s_%s_ET_%s",
                instance.getOwnedTask().getName(),
                bizTimeLab,
                execTimeLab
        );
        return szInstanceName;
    }

    @Override
    public String evalInstanceName( RavenTaskInstance instance, LocalDateTime bizTimeEpoch ) {
        return this.evalInstanceName( instance, LocalDateTime.now(), bizTimeEpoch );
    }




    @Override
    public void initializeInstance( RavenTaskInstance instance, LaunchFeature feature ) {
        LocalDateTime now = LocalDateTime.now();
        this.getLogger().info(
                "[TaskLaunchSequence] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`, Time: `{}`) <Start>",
                instance.getOwnedTask().getName(),
                instance.getOwnedTask().getFullName(),
                instance.getOwnedTask().getId(),
                now.format( this.mDefaultDateTimeFormat )
        );

        String szInstanceName = this.evalInstanceName( instance, now, feature.getBizTimeEpoch() );
        InstanceEntry entry   = instance.getInstanceEntry();

        entry.setInstanceName( szInstanceName );
        String bizTimeLab = this.evalBusinessTimeLabel( instance, feature.getBizTimeEpoch() );
        InstanceEntry previous = this.mInstanceInstrument.findLastExecuted( instance.getTaskGuid(), bizTimeLab );

        int runCount      = 0;
        int sequenceCnt   = 0;
        int retryCnt      = 0;

        if ( previous != null ) {
            runCount = previous.getRunCount() + 1;

            if ( feature.isRetry() ) {
                sequenceCnt = previous.getSequenceCnt();
                retryCnt    = previous.getRetryCnt() + 1;
            }
            else {
                sequenceCnt = previous.getSequenceCnt() + 1;
                retryCnt    = 0;
            }
        }

        if ( previous == null ) {
            runCount    = 1;
            sequenceCnt = 1;
            retryCnt    = 0;
        }

        LocalDateTime bizTime = this.evalBusinessTime( instance, feature.getBizTimeEpoch() );
        entry.setRunCount( runCount );
        entry.setSequenceCnt( sequenceCnt );
        entry.setRetryCnt( retryCnt );
        if ( entry.getGuid() == null ) {
            entry.setGuid( this.mGuidAllocator.nextGUID() );
        }
        entry.setInstanceStatus( TaskInstanceStatus.New );
        entry.setBusinessTime( bizTime );

        this.mTaskInstrument.getInstanceInstrument().addInstance( entry );
        this.getLogger().info(
                "[TaskLaunchSequence] [Schema] (Task: `{}`, InstanceName: `{}`, InsGuid: `{}`, RunCount: {}, SequenceCnt: {}, RetryCnt: {}, RetryMode: {}, BusinessTime: {}) <Ready to elevate>",
                instance.getOwnedTask().getName(),
                szInstanceName,
                entry.getGuid(),
                runCount,
                sequenceCnt,
                retryCnt,
                feature.isRetry(),
                bizTime
        );
    }

    protected void updateExecutionState(
            RavenTaskInstance instance, TaskInstanceExecState state, LocalDateTime startTime, LocalDateTime finishTime
    ) {
        InstanceEntry entry = instance.getInstanceEntry();
        this.mInstanceExecMapper.updateStateByInstanceGuidAndRetryFields(
                entry.getGuid(), entry.getRetryCnt(), state.getName(), startTime, null, finishTime
        );
    }

    protected void afterProcessCreated( RavenTaskInstance instance, UProcess process ) throws MetaPersistenceException {
        if ( process == null ) {
            this.markProcessCreationFailed( instance, LaunchErrorCauses.ProcessCreationFailure );
            return;
        }

        InstanceEntry entry = instance.getInstanceEntry();
        instance.update();

        TaskInstanceTransitionResult result = this.mTaskInstanceLifecycleInstrument.transitAny(
                entry.getGuid(),
                List.of( TaskInstanceStatus.ProcessCreating, TaskInstanceStatus.New, TaskInstanceStatus.DepartureStandby ),
                TaskInstanceStatus.ProcessStandby,
                TaskInstanceTransitionReason.ProcessCreated
        );
        if ( result.isSucceeded() ) {
            entry.setInstanceStatus( TaskInstanceStatus.ProcessStandby );
            this.updateExecutionState( instance, TaskInstanceExecState.Submitted, LocalDateTime.now(), null );
        }
    }

    protected void markProcessCreationFailed( RavenTaskInstance instance, String szCause ) {
        InstanceEntry entry = instance.getInstanceEntry();
        if ( entry.getGuid() == null ) {
            entry.setErrorCause( szCause );
            return;
        }

        try {
            entry.setErrorCause( szCause );
            instance.update();
        }
        catch ( MetaPersistenceException e ) {
            this.mLogger.error( "[TaskLaunchSequence] [MetaPersistenceException] (Instance: `{}`) <Error>", entry.getGuid(), e );
        }

        TaskInstanceTransitionResult result = this.mTaskInstanceLifecycleInstrument.transitAny(
                entry.getGuid(),
                List.of( TaskInstanceStatus.ProcessCreating, TaskInstanceStatus.New, TaskInstanceStatus.DepartureStandby ),
                TaskInstanceStatus.Error,
                TaskInstanceTransitionReason.ProcessCreationFailed
        );
        if ( result.isSucceeded() ) {
            entry.setInstanceStatus( TaskInstanceStatus.Error );
        }

        this.updateExecutionState( instance, TaskInstanceExecState.Fail, null, LocalDateTime.now() );
    }

    protected void markProcessCreationFailedIfNecessary( RavenTaskInstance instance, Exception cause ) {
        if ( instance.getInstanceEntry().getInstanceStatus() == TaskInstanceStatus.Error ) {
            return;
        }

        String szCause = cause.getMessage();
        if ( szCause == null ) {
            szCause = cause.getClass().getName();
        }
        this.markProcessCreationFailed( instance, szCause );
    }

    protected URI evalImageURI( RavenTaskInstance instance, LaunchFeature feature ) {
        URI imageURI = feature.getDesignatedImageURI();
        if ( imageURI == null ) {
            imageURI = instance.getProcessImageURI();
        }

        return imageURI;
    }

    protected UProcess prepareProcessHandle( UProcess process, LaunchFeature feature ) {
        List<ProcessEventHandler> handlers = feature.getSysProcEventHandlers();
        if ( CollectionUtils.isNoneEmpty(handlers) ) {
            for ( ProcessEventHandler handler : handlers ) {
                this.mImageModifier.addSystemProcessEventHandler( process.getExecutionImage().getEntryPoint(), handler );
            }
        }
        return process;
    }

    protected void recordExecutionImage( RavenTaskInstance instance, URI imageURI ) {
        if ( imageURI == null ) {
            return;
        }

        String szImagePath = imageURI.toString();
        InstanceEntry entry = instance.getInstanceEntry();
        entry.setImagePath( szImagePath );
        this.mInstanceExecMapper.updateImagePathByInstanceGuidAndRetry(
                entry.getGuid(), entry.getRetryCnt(), szImagePath
        );
    }

    @Override
    public UProcess createLocally( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException {
        try {
            this.initializeInstance( instance, feature );
            URI imageURI = this.evalImageURI( instance, feature );
            ImageLoader imageLoader = this.mProcessManager.getImageLoader();
            ExecutionImage image;
            UProcess process = null;
            if ( imageLoader instanceof URLImageLoader ) {
                URLImageLoader urlImageLoader = (URLImageLoader) imageLoader;
                image = urlImageLoader.queryExecutionImage( imageURI );
            }
            else {
                image = imageLoader.queryExecutionImage( imageURI.getPath() );
            }

            if ( image == null ) {
                instance.getInstanceEntry().setErrorCause( LaunchErrorCauses.NoSuchImage );
                this.markProcessCreationFailed( instance, LaunchErrorCauses.NoSuchImage );
                throw new InstanceLaunchException( LaunchErrorCauses.NoSuchImage );
            }
            else {
                instance.getInstanceEntry().setImagePath( imageURI.toString() );
                this.mLogger.info( "[TaskLaunchSequence] [LocalProcessAnchored] (Process: `{}`) <Standby>", imageURI );
                process = this.mProcessManager.createLocalHostedProcess(
                        image, feature.getParentProcess(), feature.getStartupArgs(), feature.getContextEnvironmentVars()
                );
            }

            if ( process == null ) {
                instance.getInstanceEntry().setErrorCause( LaunchErrorCauses.LocalProcessCreationFailure );
                this.markProcessCreationFailed( instance, LaunchErrorCauses.LocalProcessCreationFailure );
                throw new InstanceLaunchException( LaunchErrorCauses.LocalProcessCreationFailure );
            }

            this.prepareProcessHandle( process, feature );
            this.afterProcessCreated( instance, process );
            return process;
        }
        catch ( Exception e ) {
            this.markProcessCreationFailedIfNecessary( instance, e );
            throw new InstanceLaunchException( e );
        }
    }

    @Override
    public UProcess createPreparedLocally( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException {
        try {
            URI imageURI = this.evalImageURI( instance, feature );
            ImageLoader imageLoader = this.mProcessManager.getImageLoader();
            ExecutionImage image;
            UProcess process = null;
            if ( imageLoader instanceof URLImageLoader ) {
                URLImageLoader urlImageLoader = (URLImageLoader) imageLoader;
                image = urlImageLoader.queryExecutionImage( imageURI );
            }
            else {
                image = imageLoader.queryExecutionImage( imageURI.getPath() );
            }

            if ( image == null ) {
                instance.getInstanceEntry().setErrorCause( LaunchErrorCauses.NoSuchImage );
                this.markProcessCreationFailed( instance, LaunchErrorCauses.NoSuchImage );
                throw new InstanceLaunchException( LaunchErrorCauses.NoSuchImage );
            }
            else {
                this.recordExecutionImage( instance, imageURI );
                this.mLogger.info( "[TaskLaunchSequence] [PreparedLocalProcessAnchored] (Process: `{}`) <Standby>", imageURI );
                process = this.mProcessManager.createLocalHostedProcess(
                        image, feature.getParentProcess(), feature.getStartupArgs(), feature.getContextEnvironmentVars()
                );
            }

            if ( process == null ) {
                instance.getInstanceEntry().setErrorCause( LaunchErrorCauses.LocalProcessCreationFailure );
                this.markProcessCreationFailed( instance, LaunchErrorCauses.LocalProcessCreationFailure );
                throw new InstanceLaunchException( LaunchErrorCauses.LocalProcessCreationFailure );
            }

            this.prepareProcessHandle( process, feature );
            this.afterProcessCreated( instance, process );
            return process;
        }
        catch ( Exception e ) {
            this.markProcessCreationFailedIfNecessary( instance, e );
            throw new InstanceLaunchException( e );
        }
    }

    @Override
    public UProcess createRemotely( RavenTaskInstance instance, long pmClientId, LaunchFeature feature ) throws InstanceLaunchException {
        try {
            this.initializeInstance( instance, feature );
            URI imageURI = this.evalImageURI( instance, feature );
            RemoteProcess process = null;

            GUID parentPid = null;
            if ( feature.getParentProcess() != null ) {
                parentPid = feature.getParentProcess().getPID();
            }
            else if ( feature.getParentPid() != null ) {
                parentPid = feature.getParentPid();
            }

            instance.getInstanceEntry().setImagePath( imageURI.toString() );
            this.mLogger.info( "[TaskLaunchSequence] [RemoteProcessAnchored] (Process: `{}`, DestinationDeployClient: `{}`) <Standby>", imageURI, pmClientId );
            RemoteProcessManagerServer.RemoteCreationResult result = this.mRemoteProcessManagerServer.createRemoteUProcess(
                    pmClientId, imageURI.toString(), true, parentPid, feature.getStartupArgs(), feature.getContextEnvironmentVars()
            );
            process = result.getProcess();
            if ( result.getResponse().getStatus() != RemoteVitalizationStatus.New.getCode() || process == null ) {
                instance.getInstanceEntry().setErrorCause( LaunchErrorCauses.RemoteProcessCreationFailure );
                this.markProcessCreationFailed( instance, LaunchErrorCauses.RemoteProcessCreationFailure );
                throw new InstanceLaunchException( LaunchErrorCauses.RemoteProcessCreationFailure );
            }

            this.prepareProcessHandle( process, feature );
            this.afterProcessCreated( instance, process );
            return process;
        }
        catch ( Exception e ) {
            this.markProcessCreationFailedIfNecessary( instance, e );
            throw new InstanceLaunchException( e );
        }
    }

    @Override
    public UProcess createPreparedRemotely( RavenTaskInstance instance, long pmClientId, LaunchFeature feature ) throws InstanceLaunchException {
        try {
            URI imageURI = this.evalImageURI( instance, feature );
            RemoteProcess process = null;

            GUID parentPid = null;
            if ( feature.getParentProcess() != null ) {
                parentPid = feature.getParentProcess().getPID();
            }
            else if ( feature.getParentPid() != null ) {
                parentPid = feature.getParentPid();
            }

            this.recordExecutionImage( instance, imageURI );
            this.mLogger.info( "[TaskLaunchSequence] [PreparedRemoteProcessAnchored] (Process: `{}`, DestinationDeployClient: `{}`) <Standby>", imageURI, pmClientId );
            RemoteProcessManagerServer.RemoteCreationResult result = this.mRemoteProcessManagerServer.createRemoteUProcess(
                    pmClientId, imageURI.toString(), true, parentPid, feature.getStartupArgs(), feature.getContextEnvironmentVars()
            );
            process = result.getProcess();
            if ( result.getResponse().getStatus() != RemoteVitalizationStatus.New.getCode() || process == null ) {
                instance.getInstanceEntry().setErrorCause( LaunchErrorCauses.RemoteProcessCreationFailure );
                this.markProcessCreationFailed( instance, LaunchErrorCauses.RemoteProcessCreationFailure );
                throw new InstanceLaunchException( LaunchErrorCauses.RemoteProcessCreationFailure );
            }

            this.prepareProcessHandle( process, feature );
            this.afterProcessCreated( instance, process );
            return process;
        }
        catch ( Exception e ) {
            this.markProcessCreationFailedIfNecessary( instance, e );
            throw new InstanceLaunchException( e );
        }
    }


    protected void afterOwnedProcessTerminated( RavenTaskInstance instance, UProcess process ) {
        try {
            instance.getInstanceEntry().setInstanceStatus( TaskInstanceStatus.Finished );
            instance.getInstanceEntry().setLastEndTime( LocalDateTime.now() );
            instance.update();
        }
        catch ( MetaPersistenceException e ) {
            mLogger.error(
                    "[TaskLaunchSequence] [MetaPersistenceException] (Process: `{}`, PID: `{}`) <Error>", process.getName(), process.getPID()
            );
            mLogger.error( "[TaskLaunchSequence] [MetaPersistenceException: `{}`]", e );
        }
    }

    protected void afterOwnedProcessStarted( RavenTaskInstance instance, UProcess process ) throws InstanceLaunchException {
        try {
            instance.getInstanceEntry().setInstanceStatus( TaskInstanceStatus.Running );
            instance.getInstanceEntry().setLastStartTime( LocalDateTime.now() );
            instance.update();
        }
        catch ( MetaPersistenceException e ) {
            throw new InstanceLaunchException( e );
        }
    }


    @Override
    public UProcess launchLocally( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException {
        this.getLogger().info(
                "[TaskLaunchSequence] [LaunchLocally] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`) <Start>",
                instance.getOwnedTask().getName(),
                instance.getOwnedTask().getFullName(),
                instance.getOwnedTask().getId()
        );

        UProcess process = this.createLocally( instance, feature );
        if ( process == null ) {
            return null;
        }

        this.mLogger.info( "[TaskLaunchSequence] [LocalProcessStandby] (Process: `{}`, PID: `{}`) <LaunchServerAck>", process.getName(), process.getPID() );
        this.mLogger.info( "[TaskLaunchSequence] [ExecutingVitalizationInstruction] (Process: `{}`, PID: `{}`) <Start>", process.getName(), process.getPID() );


        this.mImageModifier.addSystemProcessEventHandler(process.getExecutionImage().getEntryPoint(), new ProcessEventHandler() {
            @Override
            public void fired( EntryPointRunnable runnable, ProcessEvent event ) {
                if ( event == ProcessEvent.Terminated ) {
                    afterOwnedProcessTerminated( instance, process );

                    mLogger.info(
                            "[TaskLaunchSequence] [LocalTaskFinished] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`) <Done>",
                            instance.getOwnedTask().getName(),
                            instance.getOwnedTask().getFullName(),
                            instance.getOwnedTask().getId()
                    );
                }
            }
        });
        process.start();
        this.afterOwnedProcessStarted( instance, process );

        this.mLogger.info(
                "[TaskLaunchSequence] [LocalTaskLaunched] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`, PID: `{}`) <Done>",
                instance.getOwnedTask().getName(),
                instance.getOwnedTask().getFullName(),
                instance.getOwnedTask().getId(),
                process.getPID()
        );
        return process;
    }

    @Override
    public UProcess launchRemotely( RavenTaskInstance instance, long pmClientId, LaunchFeature feature ) throws InstanceLaunchException {
        this.getLogger().info(
                "[TaskLaunchSequence] [LaunchRemotely] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`) <Start>",
                instance.getOwnedTask().getName(),
                instance.getOwnedTask().getFullName(),
                instance.getOwnedTask().getId()
        );

        UProcess process = this.createRemotely( instance, pmClientId, feature );
        if ( process == null ) {
            return null;
        }

        this.mLogger.info( "[TaskLaunchSequence] [RemoteProcessStandby] (Process: `{}`, PID: `{}`) <LaunchServerAck>", process.getName(), process.getPID() );
        this.mLogger.info( "[TaskLaunchSequence] [SendingVitalizationInstruction] (Process: `{}`, PID: `{}`, DestinationClient: `{}`) <Start>", process.getName(), process.getPID(), pmClientId );

        RemoteProcess remoteProcess = (RemoteProcess) process;
        remoteProcess.addRemoteEventHandler(new ProcessRemoteEventHandler() {
            @Override
            public void fired( long pmClientId, ProcessEvent event, Object caused ) {
                if ( event == ProcessEvent.Terminated ) {
                    afterOwnedProcessTerminated( instance, process );

                    mLogger.info(
                            "[TaskLaunchSequence] [RemoteTaskFinished] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`) <Done>",
                            instance.getOwnedTask().getName(),
                            instance.getOwnedTask().getFullName(),
                            instance.getOwnedTask().getId()
                    );
                }
            }
        });
        process.start();
        this.afterOwnedProcessStarted( instance, process );

        this.mLogger.info(
                "[TaskLaunchSequence] [RemoteTaskLaunched] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`, PID: `{}`) <Done>",
                instance.getOwnedTask().getName(),
                instance.getOwnedTask().getFullName(),
                instance.getOwnedTask().getId(),
                process.getPID()
        );
        return process;
    }

}
