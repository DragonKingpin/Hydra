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
import com.pinecone.hydra.proc.UProcessStatus;
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
import com.walnut.odin.conduct.lifecycle.TaskInstanceLifecycleExaminer;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionReason;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionResult;
import com.walnut.odin.proc.ProcessRemoteEventHandler;
import com.walnut.odin.proc.RemoteImageResolutionMode;
import com.walnut.odin.proc.RemoteProcess;
import com.walnut.odin.proc.RemoteTerminationStatus;
import com.walnut.odin.proc.RemoteVitalizationStatus;
import com.walnut.odin.proc.entity.RemoteProcessCreationContext;
import com.walnut.odin.proc.entity.RemoteTerminationReport;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.RavenTaskInstance;
import com.walnut.odin.task.audit.InstanceExecAuditPayloads;
import com.walnut.odin.task.mapper.InstanceExecAuditMapper;
import com.walnut.odin.task.mapper.InstanceExecMapper;

public class TrollTaskExecutionLauncher implements TaskExecutionLauncher, Slf4jTraceable {

    protected Logger mLogger;

    protected RemoteProcessManagerServer mRemoteProcessManagerServer;

    protected CollectiveTaskRegiment mCollectiveTaskRegiment;

    protected CentralizedTaskInstrument mTaskInstrument;

    protected InstanceInstrument mInstanceInstrument;

    protected InstanceExecMapper mInstanceExecMapper;

    protected InstanceExecAuditMapper mInstanceExecAuditMapper;

    protected TaskInstanceLifecycleExaminer mTaskInstanceLifecycleExaminer;

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
        this.mInstanceExecAuditMapper     = this.mTaskInstrument.getRavenTaskMasterManipulator().getScheduleManipulator().getInstanceExecAuditMapper();
        this.mTaskInstanceLifecycleExaminer = taskRegiment.taskInstanceLifecycleExaminer();
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

        if ( biz == null ) {
            biz = LocalDateTime.now();
        }

        if ( cycle == null ) {
            return biz;
        }

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
        String szInstanceName = String.format(
                "%s_%s",
                instance.getOwnedTask().getName(),
                bizTimeLab
        );
        return szInstanceName;
    }

    @Override
    public String evalInstanceName( RavenTaskInstance instance, LocalDateTime bizTimeEpoch ) {
        return this.evalInstanceName( instance, LocalDateTime.now(), bizTimeEpoch );
    }

    protected String evalInstanceName( RavenTaskInstance instance, LocalDateTime now, LaunchFeature feature ) {
        String qualifier = feature == null ? null : feature.getInstanceNameQualifier();
        if ( qualifier != null && !qualifier.isBlank() ) {
            String execTimeLab = now.format( this.mInstanceTitleTimeFormat );
            return String.format(
                    "%s_%s_%s",
                    instance.getOwnedTask().getName(),
                    qualifier,
                    execTimeLab
            );
        }
        LocalDateTime bizTimeEpoch = feature == null ? null : feature.getBizTimeEpoch();
        return this.evalInstanceName( instance, now, bizTimeEpoch );
    }

    protected boolean shouldRetryInstanceNameCollision( LaunchFeature feature ) {
        String qualifier = feature == null ? null : feature.getInstanceNameQualifier();
        return qualifier != null && !qualifier.isBlank();
    }

    protected boolean isDuplicateInstanceName( RuntimeException cause ) {
        Throwable cursor = cause;
        while ( cursor != null ) {
            String message = cursor.getMessage();
            if ( message != null
                    && message.contains( "Duplicate entry" )
                    && ( message.contains( "uk_name" ) || message.contains( "name" ) ) ) {
                return true;
            }
            cursor = cursor.getCause();
        }
        return false;
    }

    protected void addInstanceWithNameCollisionRetry(
            RavenTaskInstance instance, LaunchFeature feature, LocalDateTime baseTime
    ) {
        InstanceEntry entry = instance.getInstanceEntry();
        RuntimeException last = null;
        int nMaxAttempts = this.shouldRetryInstanceNameCollision( feature ) ? 64 : 1;

        for ( int i = 0; i < nMaxAttempts; i++ ) {
            LocalDateTime attemptTime = baseTime.plusSeconds( i );
            String szInstanceName = this.evalInstanceName( instance, attemptTime, feature );
            entry.setInstanceName( szInstanceName );
            try {
                this.mTaskInstrument.getInstanceInstrument().addInstance( entry );
                if ( i > 0 ) {
                    this.getLogger().warn(
                            "[TaskLaunchSequence] [InstanceNameCollision] "
                                    + "(Task: `{}`, InstanceName: `{}`, OffsetSeconds: {}) <Resolved>",
                            instance.getOwnedTask().getName(),
                            szInstanceName,
                            i
                    );
                }
                return;
            }
            catch ( RuntimeException e ) {
                if ( !this.shouldRetryInstanceNameCollision( feature ) || !this.isDuplicateInstanceName( e ) ) {
                    throw e;
                }
                last = e;
            }
        }

        throw last;
    }




    @Override
    public void initializeInstance( RavenTaskInstance instance, LaunchFeature feature ) {
        if ( feature == null ) {
            feature = new LaunchFeature();
        }
        LocalDateTime now = LocalDateTime.now();
        this.getLogger().info(
                "[TaskLaunchSequence] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`, Time: `{}`) <Start>",
                instance.getOwnedTask().getName(),
                instance.getOwnedTask().getFullName(),
                instance.getOwnedTask().getId(),
                now.format( this.mDefaultDateTimeFormat )
        );

        InstanceEntry entry   = instance.getInstanceEntry();

        int runCount    = Math.max( 1, entry.getRunCount() );
        int sequenceCnt = Math.max( 1, entry.getSequenceCnt() );
        int retryCnt    = feature.isRetry() ? Math.max( 0, entry.getRetryCnt() ) : 0;

        LocalDateTime bizTime = feature.isBusinessTimeVisible()
                ? this.evalBusinessTime( instance, feature.getBizTimeEpoch() )
                : null;
        entry.setRunCount( runCount );
        entry.setSequenceCnt( sequenceCnt );
        entry.setRetryCnt( retryCnt );
        if ( entry.getGuid() == null ) {
            entry.setGuid( this.mGuidAllocator.nextGUID() );
        }
        entry.setInstanceStatus( TaskInstanceStatus.New );
        entry.setBusinessTime( bizTime );

        this.addInstanceWithNameCollisionRetry( instance, feature, now );
        this.getLogger().info(
                "[TaskLaunchSequence] [Schema] (Task: `{}`, InstanceName: `{}`, InsGuid: `{}`, RunCount: {}, SequenceCnt: {}, RetryCnt: {}, RetryMode: {}, BusinessTime: {}) <Ready to elevate>",
                instance.getOwnedTask().getName(),
                entry.getInstanceName(),
                entry.getGuid(),
                runCount,
                sequenceCnt,
                retryCnt,
                feature.isRetry(),
                bizTime
        );
    }

    protected void updateExecutionState(
            RavenTaskInstance instance, TaskInstanceExecState state,
            LocalDateTime startTime, LocalDateTime runTime, LocalDateTime finishTime
    ) {
        InstanceEntry entry = instance.getInstanceEntry();
        int nAffectedRows = this.mInstanceExecMapper.updateStateRetryMonotonic(
                entry.getGuid(), entry.getSequenceCnt(), entry.getRetryCnt(), state.getName(), startTime, runTime, finishTime
        );
        if ( nAffectedRows <= 0 ) {
            this.mLogger.warn(
                    "[TaskLaunchSequence] [ExecStateUpdateMissed] "
                            + "(InstanceGuid: `{}`, SequenceCnt: {}, RetryCnt: {}, TargetState: `{}`, InstanceStatus: `{}`) <Ignored>",
                    entry.getGuid(),
                    entry.getSequenceCnt(),
                    entry.getRetryCnt(),
                    state.getName(),
                    entry.getRunStatus()
            );
        }
        else if ( this.isTerminalExecutionState( state ) ) {
            this.recordExecutionLoggerAudit( instance, state, finishTime );
        }
    }

    protected TaskInstanceTransitionResult transitCurrentRetryWithRuntimeFields(
            RavenTaskInstance instance,
            List<TaskInstanceStatus> fromStatuses,
            TaskInstanceStatus toStatus,
            TaskInstanceTransitionReason reason,
            LocalDateTime latestStartTime,
            LocalDateTime latestEndTime,
            LocalDateTime finishTime,
            String errorCause
    ) {
        InstanceEntry entry = instance.getInstanceEntry();
        return this.mTaskInstanceLifecycleExaminer.transitCurrentRetryWithRuntimeFields(
                entry.getGuid(),
                entry.getSequenceCnt(),
                entry.getRetryCnt(),
                fromStatuses,
                toStatus,
                reason,
                latestStartTime,
                latestEndTime,
                finishTime,
                errorCause
        );
    }

    protected boolean isTerminalExecutionState( TaskInstanceExecState state ) {
        return state == TaskInstanceExecState.Success
                || state == TaskInstanceExecState.Fail
                || state == TaskInstanceExecState.Killed;
    }

    protected void recordExecutionLoggerAudit(
            RavenTaskInstance instance, TaskInstanceExecState state, LocalDateTime finishTime
    ) {
        if ( this.mInstanceExecAuditMapper == null || instance == null || instance.getInstanceEntry() == null ) {
            return;
        }

        InstanceEntry entry = instance.getInstanceEntry();
        if ( entry.getGuid() == null ) {
            return;
        }

        try {
            this.mInstanceExecAuditMapper.upsertLogger(
                    entry.getTaskGuid(),
                    entry.getGuid(),
                    entry.getSequenceCnt(),
                    entry.getRetryCnt(),
                    this.executionAuditMessage( state, entry ),
                    this.executionAuditPayload( state, entry, finishTime ),
                    entry.getLastStartTime(),
                    finishTime
            );
        }
        catch ( RuntimeException e ) {
            this.mLogger.warn(
                    "[TaskLaunchSequence] [ExecAuditLoggerFailed] "
                            + "(InstanceGuid: `{}`, SequenceCnt: {}, RetryCnt: {}, ExecState: `{}`) <Ignored>",
                    entry.getGuid(),
                    entry.getSequenceCnt(),
                    entry.getRetryCnt(),
                    state.getName(),
                    e
            );
        }
    }

    protected String executionAuditMessage( TaskInstanceExecState state, InstanceEntry entry ) {
        if ( state == TaskInstanceExecState.Success ) {
            return "Execution finished with Success.";
        }
        if ( state == TaskInstanceExecState.Killed ) {
            String szCause = entry.getErrorCause();
            if ( szCause == null || szCause.trim().isEmpty() ) {
                return "Execution finished with Killed.";
            }
            return this.truncate( "Execution finished with Killed: " + szCause, 1024 );
        }
        String szCause = entry.getErrorCause();
        if ( szCause == null || szCause.trim().isEmpty() ) {
            return "Execution finished with Fail.";
        }
        return this.truncate( "Execution finished with Fail: " + szCause, 1024 );
    }

    protected String executionAuditPayload(
            TaskInstanceExecState state, InstanceEntry entry, LocalDateTime finishTime
    ) {
        return InstanceExecAuditPayloads.from( state, entry, finishTime );
    }

    protected String truncate( String value, int maxLength ) {
        if ( value == null || value.length() <= maxLength ) {
            return value;
        }
        return value.substring( 0, Math.max( 0, maxLength ) );
    }

    protected void afterProcessCreated( RavenTaskInstance instance, UProcess process ) throws MetaPersistenceException {
        if ( process == null ) {
            this.markProcessCreationFailed( instance, LaunchErrorCauses.ProcessCreationFailure );
            return;
        }

        InstanceEntry entry = instance.getInstanceEntry();
        instance.update();
        this.updateExecutionState( instance, TaskInstanceExecState.Submitted, LocalDateTime.now(), null, null );

        TaskInstanceTransitionResult result = this.transitCurrentRetryWithRuntimeFields(
                instance,
                List.of( TaskInstanceStatus.ProcessCreating, TaskInstanceStatus.New, TaskInstanceStatus.DepartureStandby ),
                TaskInstanceStatus.ProcessStandby,
                TaskInstanceTransitionReason.ProcessCreated,
                null,
                null,
                null,
                null
        );
        if ( result.isSucceeded() ) {
            entry.setInstanceStatus( TaskInstanceStatus.ProcessStandby );
        }
    }

    protected void markProcessCreationFailed( RavenTaskInstance instance, String szCause ) {
        InstanceEntry entry = instance.getInstanceEntry();
        if ( entry.getGuid() == null ) {
            entry.setErrorCause( szCause );
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        entry.setErrorCause( szCause );
        this.updateExecutionState( instance, TaskInstanceExecState.Fail, null, null, now );
        TaskInstanceTransitionResult result = this.transitCurrentRetryWithRuntimeFields(
                instance,
                List.of( TaskInstanceStatus.ProcessCreating, TaskInstanceStatus.New, TaskInstanceStatus.DepartureStandby ),
                TaskInstanceStatus.Error,
                TaskInstanceTransitionReason.ProcessCreationFailed,
                null,
                now,
                now,
                szCause
        );
        if ( result.isSucceeded() ) {
            entry.setInstanceStatus( TaskInstanceStatus.Error );
            entry.setErrorCause( szCause );
            entry.setLastEndTime( now );
            entry.setFinishTime( now );
        }
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
                entry.getGuid(), entry.getSequenceCnt(), entry.getRetryCnt(), szImagePath
        );
    }

    protected RemoteImageResolutionMode evalRemoteImageResolutionMode( LaunchFeature feature ) {
        if ( feature != null && feature.isAllowAsymmetricImage() ) {
            return RemoteImageResolutionMode.REMOTE_CLIENT_IMAGE;
        }
        return RemoteImageResolutionMode.REQUIRE_SERVER_IMAGE;
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
                process = this.mProcessManager.createLocalHostedProcessPrototypically(
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
                process = this.mProcessManager.createLocalHostedProcessPrototypically(
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
            RemoteProcessCreationContext context = RemoteProcessCreationContext.of(
                    imageURI, parentPid, feature.getStartupArgs(), feature.getContextEnvironmentVars()
            ).withImageResolutionMode( this.evalRemoteImageResolutionMode( feature ) );
            RemoteProcessManagerServer.RemoteCreationResult result = this.mRemoteProcessManagerServer.createRemoteUProcess(
                    pmClientId, context
            );
            RemoteVitalizationResponse response = result == null ? null : result.getResponse();
            process = result == null ? null : result.getProcess();
            if ( response == null || response.getStatus() != RemoteVitalizationStatus.New.getCode() || process == null ) {
                String failureCause = this.describeRemoteProcessCreationFailure( result, imageURI );
                instance.getInstanceEntry().setErrorCause( failureCause );
                this.markProcessCreationFailed( instance, failureCause );
                throw new InstanceLaunchException( failureCause );
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
            RemoteProcessCreationContext context = RemoteProcessCreationContext.of(
                    imageURI, parentPid, feature.getStartupArgs(), feature.getContextEnvironmentVars()
            ).withImageResolutionMode( this.evalRemoteImageResolutionMode( feature ) );
            RemoteProcessManagerServer.RemoteCreationResult result = this.mRemoteProcessManagerServer.createRemoteUProcess(
                    pmClientId, context
            );
            RemoteVitalizationResponse response = result == null ? null : result.getResponse();
            process = result == null ? null : result.getProcess();
            if ( response == null || response.getStatus() != RemoteVitalizationStatus.New.getCode() || process == null ) {
                String failureCause = this.describeRemoteProcessCreationFailure( result, imageURI );
                instance.getInstanceEntry().setErrorCause( failureCause );
                this.markProcessCreationFailed( instance, failureCause );
                throw new InstanceLaunchException( failureCause );
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

    protected String describeRemoteProcessCreationFailure( RemoteProcessManagerServer.RemoteCreationResult result, URI imageURI ) {
        if ( result == null ) {
            return LaunchErrorCauses.RemoteProcessCreationFailure + ": no remote creation result, image=`" + imageURI + "`";
        }

        RemoteVitalizationResponse response = result.getResponse();
        if ( response == null ) {
            return LaunchErrorCauses.RemoteProcessCreationFailure + ": no remote vitalization response, image=`" + imageURI + "`";
        }

        RemoteVitalizationStatus status = response.optStatus();
        String statusName = status == null ? String.valueOf( response.getStatus() ) : status.name();
        String responseImage = response.getImageAddress();
        if ( responseImage == null ) {
            responseImage = imageURI == null ? null : imageURI.toString();
        }

        String reason = response.getErrorMsg();
        if ( reason == null || reason.trim().isEmpty() ) {
            reason = result.getProcess() == null ? "Remote process mirror was not created." : "Remote process was rejected.";
        }

        return LaunchErrorCauses.RemoteProcessCreationFailure
                + ": status=" + statusName
                + ", image=`" + responseImage + "`"
                + ", reason=" + reason;
    }


    protected void markProcessLaunchFailed( RavenTaskInstance instance, UProcess process, Exception cause ) {
        InstanceEntry entry = instance.getInstanceEntry();
        String szCause = cause.getMessage();
        if ( szCause == null ) {
            szCause = cause.getClass().getName();
        }
        entry.setErrorCause( szCause );

        LocalDateTime now = LocalDateTime.now();
        this.updateExecutionState( instance, TaskInstanceExecState.Fail, null, null, now );
        TaskInstanceTransitionResult result = this.transitCurrentRetryWithRuntimeFields(
                instance,
                List.of( TaskInstanceStatus.ProcessStandby, TaskInstanceStatus.ProcessCreating, TaskInstanceStatus.New, TaskInstanceStatus.DepartureStandby ),
                TaskInstanceStatus.Error,
                TaskInstanceTransitionReason.ProcessFailed,
                null,
                now,
                now,
                szCause
        );
        if ( result.isSucceeded() ) {
            entry.setInstanceStatus( TaskInstanceStatus.Error );
            entry.setErrorCause( szCause );
            entry.setLastEndTime( now );
            entry.setFinishTime( now );
        }
        this.mLogger.error(
                "[TaskLaunchSequence] [ProcessStartFailure] (Process: `{}`, PID: `{}`, Instance: `{}`) <Error>",
                process == null ? null : process.getName(),
                process == null ? null : process.getPID(),
                entry.getGuid(),
                cause
        );
    }

    protected void afterOwnedProcessFinished( RavenTaskInstance instance, UProcess process ) {
        LocalDateTime now = LocalDateTime.now();
        this.updateExecutionState( instance, TaskInstanceExecState.Success, null, null, now );
        if ( instance.getInstanceEntry().getInstanceStatus() == TaskInstanceStatus.Finished ) {
            return;
        }
        TaskInstanceTransitionResult result = this.transitCurrentRetryWithRuntimeFields(
                instance,
                List.of(
                        TaskInstanceStatus.Running,
                        TaskInstanceStatus.ProcessStandby,
                        TaskInstanceStatus.ProcessCreating
                ),
                TaskInstanceStatus.Finished,
                TaskInstanceTransitionReason.ProcessSucceeded,
                null,
                now,
                now,
                null
        );
        if ( result.isSucceeded() ) {
            instance.getInstanceEntry().setInstanceStatus( TaskInstanceStatus.Finished );
            instance.getInstanceEntry().setLastEndTime( now );
            instance.getInstanceEntry().setFinishTime( now );
        }
    }

    protected void afterOwnedProcessFailed( RavenTaskInstance instance, UProcess process, Object caused ) {
        if ( this.isSignalTerminationReport( caused ) ) {
            this.afterOwnedProcessKilled( instance, process, caused );
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        String szCause = caused == null ? null : String.valueOf( caused );
        instance.getInstanceEntry().setErrorCause( szCause );
        this.updateExecutionState( instance, TaskInstanceExecState.Fail, null, null, now );
        if ( instance.getInstanceEntry().getInstanceStatus() == TaskInstanceStatus.Error ) {
            this.mLogger.warn(
                    "[TaskLaunchSequence] [LateProcessFailure] "
                            + "(Process: `{}`, PID: `{}`, Instance: `{}`, Cause: `{}`) <ExecStateRecorded>",
                    process == null ? null : process.getName(),
                    process == null ? null : process.getPID(),
                    instance.getInstanceEntry().getGuid(),
                    szCause
            );
            return;
        }
        TaskInstanceTransitionResult result = this.transitCurrentRetryWithRuntimeFields(
                instance,
                List.of(
                        TaskInstanceStatus.Running,
                        TaskInstanceStatus.ProcessStandby,
                        TaskInstanceStatus.ProcessCreating
                ),
                TaskInstanceStatus.Error,
                TaskInstanceTransitionReason.ProcessFailed,
                null,
                now,
                now,
                szCause
        );
        if ( result.isSucceeded() ) {
            instance.getInstanceEntry().setInstanceStatus( TaskInstanceStatus.Error );
            instance.getInstanceEntry().setLastEndTime( now );
            instance.getInstanceEntry().setFinishTime( now );
            if ( szCause != null ) {
                instance.getInstanceEntry().setErrorCause( szCause );
            }
        }
    }

    protected void afterOwnedProcessKilled( RavenTaskInstance instance, UProcess process, Object caused ) {
        LocalDateTime now = LocalDateTime.now();
        String szCause = caused == null ? "Remote process killed by signal." : String.valueOf( caused );
        instance.getInstanceEntry().setErrorCause( szCause );
        this.updateExecutionState( instance, TaskInstanceExecState.Killed, null, null, now );
        if ( instance.getInstanceEntry().getInstanceStatus() == TaskInstanceStatus.Killed ) {
            this.mLogger.warn(
                    "[TaskLaunchSequence] [LateProcessKilled] "
                            + "(Process: `{}`, PID: `{}`, Instance: `{}`, Cause: `{}`) <ExecStateRecorded>",
                    process == null ? null : process.getName(),
                    process == null ? null : process.getPID(),
                    instance.getInstanceEntry().getGuid(),
                    szCause
            );
            return;
        }
        TaskInstanceTransitionResult result = this.transitCurrentRetryWithRuntimeFields(
                instance,
                List.of(
                        TaskInstanceStatus.Running,
                        TaskInstanceStatus.ProcessStandby,
                        TaskInstanceStatus.ProcessCreating
                ),
                TaskInstanceStatus.Killed,
                TaskInstanceTransitionReason.ProcessKilled,
                null,
                now,
                now,
                szCause
        );
        if ( result.isSucceeded() ) {
            instance.getInstanceEntry().setInstanceStatus( TaskInstanceStatus.Killed );
            instance.getInstanceEntry().setLastEndTime( now );
            instance.getInstanceEntry().setFinishTime( now );
            instance.getInstanceEntry().setErrorCause( szCause );
        }
    }

    protected boolean isSignalTerminationReport( Object caused ) {
        if ( !( caused instanceof RemoteTerminationReport ) ) {
            return false;
        }
        RemoteTerminationStatus status = ( (RemoteTerminationReport)caused ).optStatus();
        return status == RemoteTerminationStatus.SignalInterrupted
                || status == RemoteTerminationStatus.SignalApoptosis
                || status == RemoteTerminationStatus.SignalElimination;
    }

    protected void afterOwnedProcessStarted( RavenTaskInstance instance, UProcess process ) throws InstanceLaunchException {
        LocalDateTime now = LocalDateTime.now();
        this.updateExecutionState( instance, TaskInstanceExecState.Running, null, now, null );
        TaskInstanceTransitionResult result = this.transitCurrentRetryWithRuntimeFields(
                instance,
                List.of( TaskInstanceStatus.ProcessStandby, TaskInstanceStatus.ProcessCreating ),
                TaskInstanceStatus.Running,
                TaskInstanceTransitionReason.ProcessStarted,
                now,
                null,
                null,
                null
        );
        if ( result.isSucceeded() ) {
            instance.getInstanceEntry().setInstanceStatus( TaskInstanceStatus.Running );
            instance.getInstanceEntry().setLastStartTime( now );
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

        return this.startLocally( instance, process, feature );
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

        return this.startRemotely( instance, process, pmClientId, feature );
    }

    @Override
    public UProcess launchPreparedLocally( RavenTaskInstance instance, LaunchFeature feature ) throws InstanceLaunchException {
        this.getLogger().info(
                "[TaskLaunchSequence] [LaunchPreparedLocally] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`, InstanceGuid: `{}`) <Start>",
                instance.getOwnedTask().getName(),
                instance.getOwnedTask().getFullName(),
                instance.getOwnedTask().getId(),
                instance.getInstanceEntry().getGuid()
        );

        UProcess process = this.createPreparedLocally( instance, feature );
        if ( process == null ) {
            return null;
        }

        return this.startLocally( instance, process, feature );
    }

    @Override
    public UProcess launchPreparedRemotely( RavenTaskInstance instance, long pmClientId, LaunchFeature feature ) throws InstanceLaunchException {
        this.getLogger().info(
                "[TaskLaunchSequence] [LaunchPreparedRemotely] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`, InstanceGuid: `{}`) <Start>",
                instance.getOwnedTask().getName(),
                instance.getOwnedTask().getFullName(),
                instance.getOwnedTask().getId(),
                instance.getInstanceEntry().getGuid()
        );

        UProcess process = this.createPreparedRemotely( instance, pmClientId, feature );
        if ( process == null ) {
            return null;
        }

        return this.startRemotely( instance, process, pmClientId, feature );
    }

    @Override
    public UProcess startLocally( RavenTaskInstance instance, UProcess process, LaunchFeature feature ) throws InstanceLaunchException {
        if ( process == null ) {
            return null;
        }

        this.mLogger.info( "[TaskLaunchSequence] [LocalProcessStandby] (Process: `{}`, PID: `{}`) <LaunchServerAck>", process.getName(), process.getPID() );
        this.mLogger.info( "[TaskLaunchSequence] [ExecutingVitalizationInstruction] (Process: `{}`, PID: `{}`) <Start>", process.getName(), process.getPID() );

        this.mImageModifier.addSystemProcessEventHandler(process.getExecutionImage().getEntryPoint(), new ProcessEventHandler() {
            @Override
            public void fired( EntryPointRunnable runnable, UProcessStatus event ) {
                if ( event == UProcessStatus.Terminated ) {
                    afterOwnedProcessFinished( instance, process );

                    mLogger.info(
                            "[TaskLaunchSequence] [LocalTaskFinished] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`, InstanceGuid: `{}`) <Done>",
                            instance.getOwnedTask().getName(),
                            instance.getOwnedTask().getFullName(),
                            instance.getOwnedTask().getId(),
                            instance.getInstanceEntry().getGuid()
                    );
                }
                else if ( event == UProcessStatus.Error ) {
                    afterOwnedProcessFailed( instance, process, event );
                }
            }
        });
        try {
            process.start();
            if ( process.getStatus() == UProcessStatus.Terminated ) {
                this.afterOwnedProcessFinished( instance, process );
            }
            else if ( process.getStatus() == UProcessStatus.Error ) {
                this.afterOwnedProcessFailed( instance, process, process.getStatus() );
            }
            else {
                this.afterOwnedProcessStarted( instance, process );
            }
        }
        catch ( Exception e ) {
            this.markProcessLaunchFailed( instance, process, e );
            throw new InstanceLaunchException( e );
        }

        this.mLogger.info(
                "[TaskLaunchSequence] [LocalTaskLaunched] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`, InstanceGuid: `{}`, PID: `{}`) <Done>",
                instance.getOwnedTask().getName(),
                instance.getOwnedTask().getFullName(),
                instance.getOwnedTask().getId(),
                instance.getInstanceEntry().getGuid(),
                process.getPID()
        );
        return process;
    }

    @Override
    public UProcess startRemotely( RavenTaskInstance instance, UProcess process, long pmClientId, LaunchFeature feature ) throws InstanceLaunchException {
        if ( process == null ) {
            return null;
        }

        this.mLogger.info( "[TaskLaunchSequence] [RemoteProcessStandby] (Process: `{}`, PID: `{}`) <LaunchServerAck>", process.getName(), process.getPID() );
        this.mLogger.info( "[TaskLaunchSequence] [SendingVitalizationInstruction] (Process: `{}`, PID: `{}`, DestinationClient: `{}`) <Start>", process.getName(), process.getPID(), pmClientId );

        RemoteProcess remoteProcess = (RemoteProcess) process;
        remoteProcess.addRemoteEventHandler(new ProcessRemoteEventHandler() {
            @Override
            public void fired( long pmClientId, UProcessStatus event, Object caused ) {
                if ( event == UProcessStatus.Terminated ) {
                    afterOwnedProcessFinished( instance, process );

                    mLogger.info(
                            "[TaskLaunchSequence] [RemoteTaskFinished] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`, InstanceGuid: `{}`) <Done>",
                            instance.getOwnedTask().getName(),
                            instance.getOwnedTask().getFullName(),
                            instance.getOwnedTask().getId(),
                            instance.getInstanceEntry().getGuid()
                    );
                }
                else if ( event == UProcessStatus.Error ) {
                    afterOwnedProcessFailed( instance, process, caused );
                }
            }
        });
        try {
            process.start();
            if ( process.getStatus() == UProcessStatus.Terminated ) {
                this.afterOwnedProcessFinished( instance, process );
            }
            else if ( process.getStatus() == UProcessStatus.Error ) {
                this.afterOwnedProcessFailed( instance, process, process.getStatus() );
            }
            else {
                this.afterOwnedProcessStarted( instance, process );
            }
        }
        catch ( Exception e ) {
            this.markProcessLaunchFailed( instance, process, e );
            throw new InstanceLaunchException( e );
        }

        this.mLogger.info(
                "[TaskLaunchSequence] [RemoteTaskLaunched] (TaskName: `{}`, KernelHandleName: `/{}`, TaskGuid: `{}`, InstanceGuid: `{}`, PID: `{}`) <Done>",
                instance.getOwnedTask().getName(),
                instance.getOwnedTask().getFullName(),
                instance.getOwnedTask().getId(),
                instance.getInstanceEntry().getGuid(),
                process.getPID()
        );
        return process;
    }

}
