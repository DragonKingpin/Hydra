package com.walnut.odin.conduct.recovery;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.task.TaskInstanceExecState;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.kom.instance.InstanceInstrument;
import com.pinecone.slime.meta.TableIndexMeta;
import com.walnut.odin.conduct.lifecycle.KernelTaskInstanceLifecycleInstrument;
import com.walnut.odin.conduct.lifecycle.TaskInstanceLifecycleInstrument;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionReason;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionResult;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.dispatch.TaskExecutionProcessor;
import com.walnut.odin.dispatch.TaskLaunchContext;
import com.walnut.odin.task.RavenTaskConfig;
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
    protected TaskInstanceLifecycleInstrument mTaskInstanceLifecycleInstrument;

    public KernelTaskSchedulerReconciler( UniformTaskScheduler taskScheduler ) {
        this.mTaskScheduler        = taskScheduler;
        this.mInstanceInstrument   = taskScheduler.instanceInstrument();
        this.mRavenTaskConfig      = taskScheduler.ravenTaskConfig();
        this.mScheduleManipulator  = taskScheduler.taskInstrument().getRavenTaskMasterManipulator().getScheduleManipulator();
        this.mInstanceExecMapper   = this.mScheduleManipulator.getInstanceExecMapper();
        this.mTaskInstanceLifecycleInstrument = new KernelTaskInstanceLifecycleInstrument(
                this.mInstanceInstrument,
                this.mScheduleManipulator.getInstanceEventMapper()
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

        this.mInstanceExecMapper.updateStateRetryMonotonic(
                entry.getGuid(),
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

    @Override
    public void reconcileLightweight( LocalDateTime targetTime ) {
        if ( targetTime == null ) {
            targetTime = LocalDateTime.now();
        }

        long nStaleMillis = this.resolveStaleMillis();
        this.reconcileStatus( TaskInstanceStatus.ProcessCreating, targetTime, nStaleMillis );
        this.reconcileStatus( TaskInstanceStatus.ProcessStandby, targetTime, nStaleMillis );
    }
}
