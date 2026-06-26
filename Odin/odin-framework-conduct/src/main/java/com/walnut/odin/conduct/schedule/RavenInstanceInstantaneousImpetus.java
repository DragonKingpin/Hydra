package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.hydra.system.ko.MetaPersistenceException;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionReason;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionResult;
import com.walnut.odin.conduct.schedule.entity.InstanceDepartureResult;
import com.walnut.odin.dispatch.PipelineLaunchReport;
import com.walnut.odin.dispatch.TaskDispatchException;
import com.walnut.odin.dispatch.TaskLaunchContext;
import com.walnut.odin.task.launch.LaunchProvideTaskParam;
import com.walnut.odin.task.troll.GenericRavenTaskInstance;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.LaunchFeature;

public class RavenInstanceInstantaneousImpetus implements InstanceInstantaneousImpetus {

    protected Logger log = LoggerFactory.getLogger( this.getClass() );

    protected UniformTaskScheduler mTaskScheduler;
    protected InstanceDepartureGate mInstanceDepartureGate;

    public RavenInstanceInstantaneousImpetus( UniformTaskScheduler taskScheduler ) {
        this.mTaskScheduler = taskScheduler;
        this.mInstanceDepartureGate = taskScheduler.instanceDepartureGate();
    }

    @Override
    public UniformTaskScheduler taskScheduler() {
        return this.mTaskScheduler;
    }

    @Override
    public InstanceDepartureResult impel(
            Collection<InstanceEntry> instances, LocalDateTime scheduleTime, LaunchFeature launchFeature
    ) throws InstanceLaunchException, TaskDispatchException {
        try {
            InstanceDepartureResult result = this.mInstanceDepartureGate.prepareDeparture( instances, scheduleTime );
            for ( TaskLaunchContext context : result.getLaunchContexts() ) {
                context.getLaunchFeature().mergeLaunchOptions( launchFeature );
            }
            if ( !result.getLaunchContexts().isEmpty() ) {
                this.pipeLaunchPreparedOrWait( result.getLaunchContexts() );
            }
            else if ( launchFeature != null && launchFeature.isAllowInstantaneousDepartureBypass() ) {
                Collection<TaskLaunchContext> bypassed = this.prepareBypassedLaunchContexts( instances, scheduleTime, launchFeature );
                if ( !bypassed.isEmpty() ) {
                    this.pipeLaunchPreparedOrWait( bypassed );
                    result.getLaunchContexts().addAll( bypassed );
                }
            }
            return result;
        }
        catch ( MetaPersistenceException e ) {
            throw new TaskDispatchException( e );
        }
    }

    protected void pipeLaunchPreparedOrWait( Collection<TaskLaunchContext> contexts )
            throws InstanceLaunchException, TaskDispatchException, MetaPersistenceException {
        try {
            PipelineLaunchReport report = this.mTaskScheduler.taskDispatcher().pipeLaunchPrepared( contexts );
            this.returnIdleContextsToResourceWait( contexts, report );
        }
        catch ( TaskDispatchException e ) {
            this.returnWaitingContextsToResourceWait( contexts );
            throw e;
        }
    }

    protected void returnIdleContextsToResourceWait(
            Collection<TaskLaunchContext> contexts, PipelineLaunchReport report
    ) throws MetaPersistenceException {
        if ( report == null ) {
            this.returnWaitingContextsToResourceWait( contexts );
            return;
        }

        this.returnWaitingContextsToResourceWait( report.waitingContext() );
        if ( this.hasLaunchDisposition( report ) ) {
            return;
        }

        this.returnWaitingContextsToResourceWait( contexts );
    }

    protected boolean hasLaunchDisposition( PipelineLaunchReport report ) {
        if ( report.launchedContext() != null && !report.launchedContext().isEmpty() ) {
            return true;
        }
        if ( report.waitingContext() != null && !report.waitingContext().isEmpty() ) {
            return true;
        }
        if ( report.launchedProcesses() != null && !report.launchedProcesses().isEmpty() ) {
            return true;
        }
        return false;
    }

    protected void returnWaitingContextsToResourceWait( Collection<TaskLaunchContext> contexts )
            throws MetaPersistenceException {
        if ( contexts == null || contexts.isEmpty() ) {
            return;
        }

        for ( TaskLaunchContext context : contexts ) {
            if ( context == null || context.getTaskInstance() == null ) {
                continue;
            }

            InstanceEntry instance = context.getTaskInstance().getInstanceEntry();
            this.returnInstanceToResourceWait( instance );
        }
    }

    protected void returnInstanceToResourceWait( InstanceEntry instance ) throws MetaPersistenceException {
        if ( instance == null || instance.getGuid() == null ) {
            return;
        }

        TaskInstanceTransitionResult result = this.mTaskScheduler.taskInstanceLifecycleExaminer().transitAny(
                instance.getGuid(),
                List.of( TaskInstanceStatus.ProcessCreating, TaskInstanceStatus.DepartureStandby ),
                TaskInstanceStatus.ResourceWait,
                TaskInstanceTransitionReason.ResourceWait
        );
        if ( !result.isSucceeded() ) {
            return;
        }

        instance.setInstanceStatus( TaskInstanceStatus.ResourceWait );
        this.mTaskScheduler.instanceScheduleAllocator().reclaimInstance( instance.getGuid() );
        this.log.warn(
                "[InstantaneousDepartureWait] Instance `{}` was not launched and returned to ResourceWait.",
                instance.getGuid()
        );
    }

    protected Collection<TaskLaunchContext> prepareBypassedLaunchContexts(
            Collection<InstanceEntry> instances, LocalDateTime scheduleTime, LaunchFeature launchFeature
    ) throws MetaPersistenceException {
        if ( instances == null || instances.isEmpty() ) {
            return List.of();
        }

        Collection<TaskLaunchContext> contexts = new ArrayList<>();
        for ( InstanceEntry instance : instances ) {
            if ( instance == null || instance.getGuid() == null ) {
                continue;
            }

            TaskInstanceStatus status = instance.getInstanceStatus();
            if ( status == TaskInstanceStatus.Running
                    || status == TaskInstanceStatus.ProcessCreating
                    || status == TaskInstanceStatus.ProcessStandby
                    || status == TaskInstanceStatus.Finished
                    || status == TaskInstanceStatus.Error
                    || status == TaskInstanceStatus.Killed ) {
                continue;
            }

            // Temporary manual/debug execution shortcut:
            // instantaneous execution has no dedicated waiting pool yet. Keep the departure
            // checks for visibility, but do not block manual/debug execution here. Remove this
            // block when a dedicated debug cluster or instantaneous pool is introduced.
            this.log.warn(
                    "[InstantaneousDebugDepartureBypass] Instance `{}` did not pass departure gate, continue launching for manual/debug execution.",
                    instance.getGuid()
            );
            LocalDateTime actualScheduleTime = scheduleTime;
            if ( actualScheduleTime == null ) {
                actualScheduleTime = LocalDateTime.now();
            }
            TaskInstanceTransitionResult result = this.mTaskScheduler.taskInstanceLifecycleExaminer().transitAnyWithScheduleTime(
                    instance.getGuid(),
                    List.of(
                            TaskInstanceStatus.New,
                            TaskInstanceStatus.DependencyWait,
                            TaskInstanceStatus.ResourceWait,
                            TaskInstanceStatus.DepartureStandby
                    ),
                    TaskInstanceStatus.DepartureStandby,
                    TaskInstanceTransitionReason.InstantaneousBypass,
                    actualScheduleTime
            );
            if ( !result.isSucceeded() ) {
                continue;
            }

            instance.setScheduleTime( actualScheduleTime );
            instance.setInstanceStatus( TaskInstanceStatus.DepartureStandby );

            LaunchFeature feature = new LaunchFeature();
            feature.mergeLaunchOptions( launchFeature );
            feature = this.provideLaunchFeature( instance, feature );
            contexts.add( TaskLaunchContext.of(
                    new GenericRavenTaskInstance( instance, this.mTaskScheduler.taskInstrument() ),
                    feature
            ) );
        }
        return contexts;
    }

    protected LaunchFeature provideLaunchFeature( InstanceEntry instance, LaunchFeature launchFeature ) {
        return this.mTaskScheduler.taskLaunchFeatureProviderRegistry().apply(
                LaunchProvideTaskParam.from( instance ),
                launchFeature
        );
    }

}
