package com.walnut.odin.conduct.recovery;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.task.InstanceEventType;
import com.pinecone.hydra.task.TaskInstanceExecState;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.pinecone.slime.meta.TableIndexMeta;
import com.walnut.odin.conduct.entity.GenericInstanceEvent;
import com.walnut.odin.conduct.entity.GenericInstanceExec;
import com.walnut.odin.conduct.entity.InstanceEvent;
import com.walnut.odin.conduct.entity.InstanceExec;
import com.walnut.odin.conduct.lifecycle.KernelTaskInstanceLifecycleInstrument;
import com.walnut.odin.conduct.lifecycle.TaskInstanceLifecycleInstrument;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionReason;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionResult;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.dispatch.TaskExecutionProcessor;
import com.walnut.odin.dispatch.TaskLaunchContext;
import com.walnut.odin.task.RavenTaskConfig;
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
    protected InstanceEventMapper             mInstanceEventMapper;
    protected GuidAllocator                   mGuidAllocator;
    protected TaskInstanceLifecycleInstrument mTaskInstanceLifecycleInstrument;

    public KernelTaskSchedulerReconciler( UniformTaskScheduler taskScheduler ) {
        this.mTaskScheduler        = taskScheduler;
        this.mInstanceInstrument   = taskScheduler.instanceInstrument();
        this.mRavenTaskConfig      = taskScheduler.ravenTaskConfig();
        this.mScheduleManipulator  = taskScheduler.taskInstrument().getRavenTaskMasterManipulator().getScheduleManipulator();
        this.mInstanceExecMapper   = this.mScheduleManipulator.getInstanceExecMapper();
        this.mInstanceEventMapper  = this.mScheduleManipulator.getInstanceEventMapper();
        this.mGuidAllocator        = this.mInstanceInstrument.getTaskInstrument().getGuidAllocator();
        this.mTaskInstanceLifecycleInstrument = new KernelTaskInstanceLifecycleInstrument(
                this.mInstanceInstrument,
                this.mInstanceEventMapper
        );
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
        int nAffected = this.mInstanceInstrument.transitStatusIn(
                entry.getGuid(),
                List.of( TaskInstanceStatus.ProcessCreating ),
                TaskInstanceStatus.DepartureStandby
        );
        if ( nAffected > 0 ) {
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
        TaskInstanceTransitionResult result = this.mTaskInstanceLifecycleInstrument.transitAnyWithRuntimeFields(
                entry.getGuid(),
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
        this.mInstanceExecMapper.updateStateRetryMonotonic(
                entry.getGuid(),
                entry.getSequenceCnt(),
                entry.getRetryCnt(),
                TaskInstanceExecState.Fail.getName(),
                null,
                null,
                targetTime
        );
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
        TaskInstanceTransitionResult result = this.mTaskInstanceLifecycleInstrument.transitAnyWithRuntimeFields(
                entry.getGuid(),
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
        this.mInstanceExecMapper.updateStateRetryMonotonic(
                entry.getGuid(),
                entry.getSequenceCnt(),
                entry.getRetryCnt(),
                TaskInstanceExecState.Killed.getName(),
                null,
                null,
                targetTime
        );
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

    protected void reconcileRetryableTerminalInstances( LocalDateTime targetTime ) {
        List<TaskInstanceStatus> statuses = List.of(
                TaskInstanceStatus.Killed,
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

    protected void reconcileRetryableTerminalInstance( InstanceEntry entry, LocalDateTime targetTime ) {
        if ( entry == null || entry.getGuid() == null || entry.isDryRun() ) {
            return;
        }
        if ( entry.getRetryCnt() >= entry.getRetryTimes() ) {
            return;
        }

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

        InstanceEvent event = new GenericInstanceEvent();
        event.setGuid( this.mGuidAllocator.nextGUID() );
        event.setTaskGuid( entry.getTaskGuid() );
        event.setInstanceGuid( instanceGuid );
        event.setInstanceName( entry.getInstanceName() );
        event.setRetryTimes( entry.getRetryTimes() );
        event.setSequenceCnt( nSequenceCnt );
        event.setCurrentRetryNumber( nRetryCnt );
        event.setEventType( entry.getTaskType() );
        event.setState( szEventState );
        event.setExecTime( LocalDateTime.now() );
        event.setEventContext( "{\"message\":\"Retry execution scheduled.\"}" );
        this.mInstanceEventMapper.insert( event );
    }

    @Override
    public void reconcileLightweight( LocalDateTime targetTime ) {
        if ( targetTime == null ) {
            targetTime = LocalDateTime.now();
        }

        long nStaleMillis = this.resolveStaleMillis();
        this.reconcileTimedOutRunningInstances( targetTime );
        this.reconcileRetryableTerminalInstances( targetTime );
        this.reconcileStatus( TaskInstanceStatus.ProcessCreating, targetTime, nStaleMillis );
        this.reconcileStatus( TaskInstanceStatus.ProcessStandby, targetTime, nStaleMillis );
        this.reconcileRetryableTerminalInstances( targetTime );
    }
}
