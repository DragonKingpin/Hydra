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
import com.walnut.odin.conduct.schedule.entity.InstanceDepartureResult;
import com.walnut.odin.dispatch.TaskDispatchException;
import com.walnut.odin.dispatch.TaskLaunchContext;
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
                this.mTaskScheduler.taskDispatcher().pipeLaunchPrepared( result.getLaunchContexts() );
            }
            else if ( launchFeature != null && launchFeature.isAllowInstantaneousDepartureBypass() ) {
                Collection<TaskLaunchContext> bypassed = this.prepareBypassedLaunchContexts( instances, scheduleTime, launchFeature );
                if ( !bypassed.isEmpty() ) {
                    this.mTaskScheduler.taskDispatcher().pipeLaunchPrepared( bypassed );
                    result.getLaunchContexts().addAll( bypassed );
                }
            }
            return result;
        }
        catch ( MetaPersistenceException e ) {
            throw new TaskDispatchException( e );
        }
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
            instance.setScheduleTime( scheduleTime == null ? LocalDateTime.now() : scheduleTime );
            instance.setInstanceStatus( TaskInstanceStatus.DepartureStandby );
            this.mTaskScheduler.instanceInstrument().updateInstance( instance );

            LaunchFeature feature = new LaunchFeature();
            feature.mergeLaunchOptions( launchFeature );
            contexts.add( TaskLaunchContext.of(
                    new GenericRavenTaskInstance( instance, this.mTaskScheduler.taskInstrument() ),
                    feature
            ) );
        }
        return contexts;
    }

}
