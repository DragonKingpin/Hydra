package com.walnut.odin.conduct.recovery;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.task.InstanceEventType;
import com.pinecone.hydra.task.TaskInstanceExecState;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.pinecone.slime.meta.TableIndexMeta;
import com.walnut.odin.conduct.entity.GenericInstanceExec;
import com.walnut.odin.conduct.entity.InstanceExec;
import com.walnut.odin.conduct.lifecycle.TaskInstanceLifecycleExaminer;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionReason;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionResult;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.dispatch.TaskExecutionProcessor;
import com.walnut.odin.dispatch.TaskLaunchContext;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.audit.InstanceExecAuditPayloads;
import com.walnut.odin.task.mapper.InstanceExecAuditMapper;
import com.walnut.odin.task.mapper.InstanceEventMapper;
import com.walnut.odin.task.mapper.InstanceExecMapper;
import com.walnut.odin.task.source.ScheduleManipulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performs lightweight scheduler recovery before a scheduler pulse.
 *
 * This component should repair or unblock short-lived scheduler states that may be left behind by a previous
 * interrupted pulse, such as claimed-but-not-created instances. It is not responsible for normal task preparation,
 * dependency checks, resource fitting, or process lifecycle progression.
 */
public class KernelTaskSchedulerReconciler implements TaskSchedulerReconciler, Pinenut {

    protected Logger log = LoggerFactory.getLogger( this.getClass() );

    protected UniformTaskScheduler            mTaskScheduler;
    protected InstanceInstrument              mInstanceInstrument;
    protected RavenTaskConfig                 mRavenTaskConfig;
    protected ScheduleManipulator             mScheduleManipulator;
    protected InstanceExecMapper              mInstanceExecMapper;
    protected InstanceExecAuditMapper         mInstanceExecAuditMapper;
    protected InstanceEventMapper             mInstanceEventMapper;
    protected TaskInstanceLifecycleExaminer mTaskInstanceLifecycleExaminer;

    public KernelTaskSchedulerReconciler( UniformTaskScheduler taskScheduler ) {
        this.mTaskScheduler        = taskScheduler;
        this.mInstanceInstrument   = taskScheduler.instanceInstrument();
        this.mRavenTaskConfig      = taskScheduler.ravenTaskConfig();
        this.mScheduleManipulator  = taskScheduler.taskInstrument().getRavenTaskMasterManipulator().getScheduleManipulator();
        this.mInstanceExecMapper   = this.mScheduleManipulator.getInstanceExecMapper();
        this.mInstanceExecAuditMapper = this.mScheduleManipulator.getInstanceExecAuditMapper();
        this.mInstanceEventMapper  = this.mScheduleManipulator.getInstanceEventMapper();
        this.mTaskInstanceLifecycleExaminer = taskScheduler.taskInstanceLifecycleExaminer();
    }

    protected long resolveStaleMillis() {
        long nRecoveryPulseMillis = this.mRavenTaskConfig.getScheduleCycleEngineRecoveryPulseMillis();
        long nTickMillis = this.mRavenTaskConfig.getScheduleCycleEngineTickMillis();
        long nBaseMillis = Math.max( nRecoveryPulseMillis, nTickMillis );
        return Math.max( 15000L, nBaseMillis * 3L );
    }

    protected boolean isStale( InstanceEntry entry, LocalDateTime targetTime, long staleMillis ) {
        if ( entry == null || targetTime == null ) {
            return false;
        }

        LocalDateTime updateTime = entry.getUpdateTime();
        if ( updateTime == null ) {
            updateTime = entry.getCreateTime();
        }
        if ( updateTime == null ) {
            return true;
        }

        return Duration.between( updateTime, targetTime ).toMillis() >= staleMillis;
    }

    protected boolean isLiveProcess( UProcess process ) {
        if ( process == null ) {
            return false;
        }
        UProcessStatus status = process.getStatus();
        return status != UProcessStatus.Terminated && status != UProcessStatus.Error;
    }

    protected boolean hasLiveLaunchContext( InstanceEntry entry ) {
        if ( entry == null || entry.getGuid() == null ) {
            return false;
        }

        String szInstanceGuid = entry.getGuid().toString();
        for ( TaskExecutionProcessor processor : this.mTaskScheduler.taskDispatcher().fetchProcessors() ) {
            Collection<TaskLaunchContext> contexts = this.mTaskScheduler.taskDispatcher().queryAffinityTasks( processor.getName() );
            if ( contexts == null || contexts.isEmpty() ) {
                continue;
            }
            for ( TaskLaunchContext context : contexts ) {
                if ( context == null || context.getTaskInstance() == null ) {
                    continue;
                }
                InstanceEntry contextEntry = context.getTaskInstance().getInstanceEntry();
                if ( contextEntry == null || contextEntry.getGuid() == null ) {
                    continue;
                }
                if ( !szInstanceGuid.equals( contextEntry.getGuid().toString() ) ) {
                    continue;
                }
                if ( this.isLiveProcess( context.getLaunchedProcess() ) ) {
                    return true;
                }
            }
        }

        return false;
    }

    protected void reconcileProcessCreating( InstanceEntry entry ) {
        TaskInstanceTransitionResult result = this.mTaskInstanceLifecycleExaminer.transit(
                entry.getGuid(),
                TaskInstanceStatus.ProcessCreating,
                TaskInstanceStatus.DepartureStandby,
                TaskInstanceTransitionReason.ProcessCreationRecovered
        );
        if ( result.isSucceeded() ) {
            log.warn(
                    "[TaskSchedulerRecovery] Stale ProcessCreating instance returned to DepartureStandby "
                            + "(InstanceGuid: `{}`, TaskGuid: `{}`, TaskName: `{}`) <Recovered>",
                    entry.getGuid(),
                    entry.getTaskGuid(),
                    entry.getTaskName()
            );
        }
    }

    protected void reconcileProcessStandby( InstanceEntry entry, LocalDateTime targetTime ) {
        TaskInstanceTransitionResult result = this.mTaskInstanceLifecycleExaminer.transitCurrentRetryWithRuntimeFields(
                entry.getGuid(),
                entry.getSequenceCnt(),
                entry.getRetryCnt(),
                List.of( TaskInstanceStatus.ProcessStandby ),
                TaskInstanceStatus.Error,
                TaskInstanceTransitionReason.ProcessFailed,
                null,
                targetTime,
                targetTime,
                "Scheduler recovery: process standby lost live launch context."
        );
        if ( !result.isSucceeded() ) {
            return;
        }

        this.mTaskScheduler.instanceScheduleAllocator().reclaimInstance( entry.getGuid() );
        int nAffectedRows = this.mInstanceExecMapper.updateStateRetryMonotonic(
                entry.getGuid(),
                entry.getSequenceCnt(),
                entry.getRetryCnt(),
                TaskInstanceExecState.Fail.getName(),
                null,
                null,
                targetTime
        );
        if ( nAffectedRows > 0 ) {
            this.recordExecutionLoggerAudit( entry, TaskInstanceExecState.Fail, targetTime );
        }
        log.warn(
                "[TaskSchedulerRecovery] Stale ProcessStandby instance marked Error "
                        + "(InstanceGuid: `{}`, TaskGuid: `{}`, TaskName: `{}`) <Recovered>",
                entry.getGuid(),
                entry.getTaskGuid(),
                entry.getTaskName()
        );
    }

    protected void reconcileStatus( TaskInstanceStatus status, LocalDateTime targetTime, long staleMillis ) {
        TableIndexMeta range = this.mInstanceInstrument.querySchedulableIdRange( List.of( status ), targetTime );
        if ( range == null || range.getMinId() <= 0 || range.getMaxId() <= 0 || range.getMaxId() < range.getMinId() ) {
            return;
        }

        long nWindow = Math.max( 1L, this.mRavenTaskConfig.getScheduleScanIdWindow() );
        long cursor = range.getMinId();
        while ( cursor <= range.getMaxId() ) {
            long windowEnd = Math.min( cursor + nWindow - 1L, range.getMaxId() );
            List<InstanceEntry> entries = this.mInstanceInstrument.fetchSchedulableInstances(
                    cursor,
                    windowEnd,
                    List.of( status ),
                    targetTime
            );
            if ( entries != null && !entries.isEmpty() ) {
                for ( InstanceEntry entry : entries ) {
                    if ( !this.isStale( entry, targetTime, staleMillis ) ) {
                        continue;
                    }
                    if ( this.hasLiveLaunchContext( entry ) ) {
                        continue;
                    }
                    if ( status == TaskInstanceStatus.ProcessCreating ) {
                        this.reconcileProcessCreating( entry );
                    }
                    else if ( status == TaskInstanceStatus.ProcessStandby ) {
                        this.reconcileProcessStandby( entry, targetTime );
                    }
                }
            }
            cursor = windowEnd + 1L;
        }
    }

    protected void reconcileTimedOutRunningInstances( LocalDateTime targetTime ) {
        TableIndexMeta range = this.mInstanceInstrument.queryTimedOutRunningIdRange( targetTime );
        if ( range == null || range.getMinId() <= 0 || range.getMaxId() <= 0 || range.getMaxId() < range.getMinId() ) {
            return;
        }

        long nWindow = Math.max( 1L, this.mRavenTaskConfig.getScheduleScanIdWindow() );
        long cursor = range.getMinId();
        while ( cursor <= range.getMaxId() ) {
            long windowEnd = Math.min( cursor + nWindow - 1L, range.getMaxId() );
            List<InstanceEntry> entries = this.mInstanceInstrument.fetchTimedOutRunningInstances( cursor, windowEnd, targetTime );
            if ( entries != null && !entries.isEmpty() ) {
                for ( InstanceEntry entry : entries ) {
                    this.reconcileTimedOutRunningInstance( entry, targetTime );
                }
            }
            cursor = windowEnd + 1L;
        }
    }

    protected void reconcileTimedOutRunningInstance( InstanceEntry entry, LocalDateTime targetTime ) {
        if ( entry == null || entry.getGuid() == null || entry.getTimeoutSeconds() == null ) {
            return;
        }

        this.killLiveLaunchContext( entry );

        String szCause = "Task execution timeout after " + entry.getTimeoutSeconds() + " seconds.";
        TaskInstanceTransitionResult result = this.mTaskInstanceLifecycleExaminer.transitCurrentRetryWithRuntimeFields(
                entry.getGuid(),
                entry.getSequenceCnt(),
                entry.getRetryCnt(),
                List.of( TaskInstanceStatus.Running ),
                TaskInstanceStatus.Error,
                TaskInstanceTransitionReason.ProcessKilled,
                null,
                targetTime,
                targetTime,
                szCause
        );
        if ( !result.isSucceeded() ) {
            return;
        }

        this.mTaskScheduler.instanceScheduleAllocator().reclaimInstance( entry.getGuid() );
        int nAffectedRows = this.mInstanceExecMapper.updateStateRetryMonotonic(
                entry.getGuid(),
                entry.getSequenceCnt(),
                entry.getRetryCnt(),
                TaskInstanceExecState.Killed.getName(),
                null,
                null,
                targetTime
        );
        if ( nAffectedRows > 0 ) {
            entry.setErrorCause( szCause );
            this.recordExecutionLoggerAudit( entry, TaskInstanceExecState.Killed, targetTime );
        }
        log.warn(
                "[TaskSchedulerRecovery] Running instance timed out "
                        + "(InstanceGuid: `{}`, TaskGuid: `{}`, TaskName: `{}`, TimeoutSeconds: `{}`) <Killed>",
                entry.getGuid(),
                entry.getTaskGuid(),
                entry.getTaskName(),
                entry.getTimeoutSeconds()
        );
    }

    protected void killLiveLaunchContext( InstanceEntry entry ) {
        TaskLaunchContext context = this.queryLiveLaunchContext( entry );
        if ( context == null || context.getLaunchedProcess() == null ) {
            return;
        }
        try {
            context.getLaunchedProcess().kill();
        }
        catch ( RuntimeException e ) {
            log.warn(
                    "[TaskSchedulerRecovery] Failed to kill timed-out process "
                            + "(InstanceGuid: `{}`, TaskGuid: `{}`) <Ignored>",
                    entry.getGuid(),
                    entry.getTaskGuid(),
                    e
            );
        }
    }

    protected TaskLaunchContext queryLiveLaunchContext( InstanceEntry entry ) {
        if ( entry == null || entry.getGuid() == null ) {
            return null;
        }

        String szInstanceGuid = entry.getGuid().toString();
        for ( TaskExecutionProcessor processor : this.mTaskScheduler.taskDispatcher().fetchProcessors() ) {
            Collection<TaskLaunchContext> contexts = this.mTaskScheduler.taskDispatcher().queryAffinityTasks( processor.getName() );
            if ( contexts == null || contexts.isEmpty() ) {
                continue;
            }
            for ( TaskLaunchContext context : contexts ) {
                if ( context == null || context.getTaskInstance() == null ) {
                    continue;
                }
                InstanceEntry contextEntry = context.getTaskInstance().getInstanceEntry();
                if ( contextEntry == null || contextEntry.getGuid() == null ) {
                    continue;
                }
                if ( !szInstanceGuid.equals( contextEntry.getGuid().toString() ) ) {
                    continue;
                }
                if ( this.isLiveProcess( context.getLaunchedProcess() ) ) {
                    return context;
                }
            }
        }
        return null;
    }

    @Override
    public void reconcileRetryableTerminalInstances( LocalDateTime targetTime ) {
        if ( targetTime == null ) {
            targetTime = LocalDateTime.now();
        }

        List<TaskInstanceStatus> statuses = List.of(
                TaskInstanceStatus.Error,
                TaskInstanceStatus.AuditFailed
        );
        TableIndexMeta range = this.mInstanceInstrument.queryRetryableTerminalIdRange( statuses, targetTime );
        if ( range == null || range.getMinId() <= 0 || range.getMaxId() <= 0 || range.getMaxId() < range.getMinId() ) {
            return;
        }

        long nWindow = Math.max( 1L, this.mRavenTaskConfig.getScheduleScanIdWindow() );
        long cursor = range.getMinId();
        while ( cursor <= range.getMaxId() ) {
            long windowEnd = Math.min( cursor + nWindow - 1L, range.getMaxId() );
            List<InstanceEntry> entries = this.mInstanceInstrument.fetchRetryableTerminalInstances(
                    cursor,
                    windowEnd,
                    statuses,
                    targetTime
            );
            if ( entries != null && !entries.isEmpty() ) {
                for ( InstanceEntry entry : entries ) {
                    this.reconcileRetryableTerminalInstance( entry, targetTime );
                }
            }
            cursor = windowEnd + 1L;
        }
    }

    protected void reconcileTerminalExecLoggerAudits() {
        int nLimit = (int)Math.min(
                Integer.MAX_VALUE,
                Math.max( 1L, this.mRavenTaskConfig.getScheduleScanIdWindow() * 4L )
        );
        List<InstanceExec> execs = this.mInstanceExecMapper.fetchTerminalExecsMissingLoggerAudit( nLimit );
        if ( execs == null || execs.isEmpty() ) {
            return;
        }
        for ( InstanceExec exec : execs ) {
            this.recordExecutionLoggerAudit( exec );
        }
    }

    protected void reconcileRetryableTerminalInstance( InstanceEntry entry, LocalDateTime targetTime ) {
        if ( entry == null || entry.getGuid() == null || entry.isDryRun() ) {
            return;
        }
        if ( entry.getRetryCnt() >= entry.getRetryTimes() ) {
            return;
        }

        this.ensureCurrentExecTerminalBeforeRetry( entry, targetTime );

        LocalDateTime expectTime = this.resolveRetryExpectTime( entry, targetTime );
        int nCurrentRetryCnt = entry.getRetryCnt();
        this.mTaskScheduler.instanceScheduleAllocator().reclaimInstance( entry.getGuid() );
        int nAffected = this.mInstanceInstrument.resetForRetry(
                entry.getGuid(),
                nCurrentRetryCnt,
                expectTime,
                targetTime,
                null
        );
        if ( nAffected <= 0 ) {
            return;
        }

        InstanceEntry retryEntry = this.mInstanceInstrument.getInstanceEntry( entry.getGuid() );
        if ( retryEntry == null ) {
            return;
        }
        this.ensureRetryExec( retryEntry );
        this.ensureRetryTimeReadyEvent( retryEntry );
        log.warn(
                "[TaskSchedulerRecovery] Terminal instance scheduled for retry "
                        + "(InstanceGuid: `{}`, TaskGuid: `{}`, TaskName: `{}`, Retry: `{}/{}`, ExpectTime: `{}`) <Retry>",
                retryEntry.getGuid(),
                retryEntry.getTaskGuid(),
                retryEntry.getTaskName(),
                retryEntry.getRetryCnt(),
                retryEntry.getRetryTimes(),
                expectTime
        );
    }

    protected void ensureCurrentExecTerminalBeforeRetry( InstanceEntry entry, LocalDateTime targetTime ) {
        InstanceExec exec = this.mInstanceExecMapper.queryByInstanceGuidAndRetry(
                entry.getGuid(), entry.getSequenceCnt(), entry.getRetryCnt()
        );
        if ( exec == null || this.isTerminalExecState( exec.getExecState() ) ) {
            return;
        }

        TaskInstanceExecState targetState = this.resolveRetrySourceExecState( entry );
        int nAffectedRows = this.mInstanceExecMapper.updateStateRetryMonotonic(
                entry.getGuid(),
                entry.getSequenceCnt(),
                entry.getRetryCnt(),
                targetState.getName(),
                null,
                null,
                targetTime
        );
        if ( nAffectedRows > 0 ) {
            this.recordExecutionLoggerAudit( entry, targetState, targetTime );
        }
        log.warn(
                "[TaskSchedulerRecovery] Retry source exec was not terminal before retry "
                        + "(InstanceGuid: `{}`, TaskGuid: `{}`, SequenceCnt: {}, RetryCnt: {}, "
                        + "ExecState: `{}`, TargetState: `{}`, AffectedRows: {}) <Recovered>",
                entry.getGuid(),
                entry.getTaskGuid(),
                entry.getSequenceCnt(),
                entry.getRetryCnt(),
                exec.getExecState(),
                targetState.getName(),
                nAffectedRows
        );
    }

    protected boolean isTerminalExecState( String szExecState ) {
        return TaskInstanceExecState.Success.getName().equals( szExecState )
                || TaskInstanceExecState.Fail.getName().equals( szExecState )
                || TaskInstanceExecState.Killed.getName().equals( szExecState );
    }

    protected TaskInstanceExecState resolveRetrySourceExecState( InstanceEntry entry ) {
        return TaskInstanceExecState.Fail;
    }

    protected void recordExecutionLoggerAudit( InstanceExec exec ) {
        if ( this.mInstanceExecAuditMapper == null || exec == null || exec.getInstanceGuid() == null ) {
            return;
        }

        TaskInstanceExecState state = this.parseExecState( exec.getExecState() );
        if ( state == null ) {
            return;
        }

        try {
            this.mInstanceExecAuditMapper.upsertLogger(
                    exec.getTaskGuid(),
                    exec.getInstanceGuid(),
                    exec.getSequenceCnt(),
                    exec.getCurrentRetryNumber(),
                    this.executionAuditMessage( state, exec ),
                    this.executionAuditPayload( state, exec ),
                    exec.getStartTime(),
                    exec.getFinishTime()
            );
        }
        catch ( RuntimeException e ) {
            this.log.warn(
                    "[TaskSchedulerRecovery] Failed to reconcile exec logger audit "
                            + "(InstanceGuid: `{}`, SequenceCnt: {}, RetryCnt: {}, ExecState: `{}`) <Ignored>",
                    exec.getInstanceGuid(),
                    exec.getSequenceCnt(),
                    exec.getCurrentRetryNumber(),
                    exec.getExecState(),
                    e
            );
        }
    }

    protected void recordExecutionLoggerAudit(
            InstanceEntry entry, TaskInstanceExecState state, LocalDateTime finishTime
    ) {
        if ( this.mInstanceExecAuditMapper == null || entry == null || entry.getGuid() == null ) {
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
            this.log.warn(
                    "[TaskSchedulerRecovery] Failed to write exec logger audit "
                            + "(InstanceGuid: `{}`, SequenceCnt: {}, RetryCnt: {}, ExecState: `{}`) <Ignored>",
                    entry.getGuid(),
                    entry.getSequenceCnt(),
                    entry.getRetryCnt(),
                    state.getName(),
                    e
            );
        }
    }

    protected TaskInstanceExecState parseExecState( String szExecState ) {
        if ( TaskInstanceExecState.Success.getName().equals( szExecState ) ) {
            return TaskInstanceExecState.Success;
        }
        if ( TaskInstanceExecState.Fail.getName().equals( szExecState ) ) {
            return TaskInstanceExecState.Fail;
        }
        if ( TaskInstanceExecState.Killed.getName().equals( szExecState ) ) {
            return TaskInstanceExecState.Killed;
        }
        return null;
    }

    protected String executionAuditMessage( TaskInstanceExecState state, InstanceEntry entry ) {
        String szCause = entry.getErrorCause();
        if ( state == TaskInstanceExecState.Killed ) {
            if ( szCause == null || szCause.trim().isEmpty() ) {
                return "Execution finished with Killed.";
            }
            return this.truncate( "Execution finished with Killed: " + szCause, 1024 );
        }
        if ( state == TaskInstanceExecState.Success ) {
            return "Execution finished with Success.";
        }
        if ( szCause == null || szCause.trim().isEmpty() ) {
            return "Execution finished with Fail.";
        }
        return this.truncate( "Execution finished with Fail: " + szCause, 1024 );
    }

    protected String executionAuditMessage( TaskInstanceExecState state, InstanceExec exec ) {
        if ( state == TaskInstanceExecState.Killed ) {
            return "Execution finished with Killed.";
        }
        if ( state == TaskInstanceExecState.Success ) {
            return "Execution finished with Success.";
        }
        return "Execution finished with Fail.";
    }

    protected String executionAuditPayload(
            TaskInstanceExecState state, InstanceEntry entry, LocalDateTime finishTime
    ) {
        return InstanceExecAuditPayloads.from( state, entry, finishTime );
    }

    protected String executionAuditPayload( TaskInstanceExecState state, InstanceExec exec ) {
        return InstanceExecAuditPayloads.from( state, exec );
    }

    protected String truncate( String value, int maxLength ) {
        if ( value == null || value.length() <= maxLength ) {
            return value;
        }
        return value.substring( 0, Math.max( 0, maxLength ) );
    }

    protected LocalDateTime resolveRetryExpectTime( InstanceEntry entry, LocalDateTime targetTime ) {
        Long nRetryIntervalSeconds = entry.getRetryIntervalSeconds();
        if ( nRetryIntervalSeconds == null || nRetryIntervalSeconds <= 0L ) {
            return targetTime;
        }
        return targetTime.plusSeconds( nRetryIntervalSeconds );
    }

    protected void ensureRetryExec( InstanceEntry entry ) {
        GUID instanceGuid = entry.getGuid();
        int nSequenceCnt = entry.getSequenceCnt();
        int nRetryCnt = entry.getRetryCnt();
        if ( this.mInstanceExecMapper.queryByInstanceGuidAndRetry( instanceGuid, nSequenceCnt, nRetryCnt ) != null ) {
            return;
        }

        InstanceExec exec = new GenericInstanceExec();
        exec.setTaskGuid( entry.getTaskGuid() );
        exec.setInstanceGuid( instanceGuid );
        exec.setTaskName( entry.getTaskName() );
        exec.setInstanceName( entry.getInstanceName() );
        exec.setProcessorQueue( "default" );
        exec.setAffinityProcessor( entry.getAffinityProcessor() );
        exec.setDesignatedProcessor( entry.getDesignatedProcessor() );
        exec.setExecutedProcessor( null );
        exec.setImagePath( entry.getImagePath() );
        exec.setClusterName( "local_cluster" );
        exec.setExecState( TaskInstanceExecState.Submitted.getName() );
        exec.setSequenceCnt( nSequenceCnt );
        exec.setCurrentRetryNumber( nRetryCnt );
        exec.setRetryTimes( entry.getRetryTimes() );
        this.mInstanceExecMapper.insert( exec );
    }

    protected void ensureRetryTimeReadyEvent( InstanceEntry entry ) {
        GUID instanceGuid = entry.getGuid();
        int nSequenceCnt = entry.getSequenceCnt();
        int nRetryCnt = entry.getRetryCnt();
        String szEventState = InstanceEventType.TaskTimeReady.getName();
        if ( this.mInstanceEventMapper.queryByInstanceGuidAndState( instanceGuid, nSequenceCnt, nRetryCnt, szEventState ) != null ) {
            return;
        }

        this.mTaskInstanceLifecycleExaminer.recordInstanceEvent(
                entry,
                TaskInstanceTransitionReason.TimeReady,
                szEventState,
                "{\"message\":\"Retry execution scheduled.\"}"
        );
    }

    @Override
    public void reconcileLightweight( LocalDateTime targetTime ) {
        if ( targetTime == null ) {
            targetTime = LocalDateTime.now();
        }

        long nStaleMillis = this.resolveStaleMillis();
        this.reconcileTerminalExecLoggerAudits();
        this.reconcileTimedOutRunningInstances( targetTime );
        this.reconcileRetryableTerminalInstances( targetTime );
        this.reconcileStatus( TaskInstanceStatus.ProcessCreating, targetTime, nStaleMillis );
        this.reconcileStatus( TaskInstanceStatus.ProcessStandby, targetTime, nStaleMillis );
        this.reconcileRetryableTerminalInstances( targetTime );
        this.reconcileTerminalExecLoggerAudits();
    }
}
