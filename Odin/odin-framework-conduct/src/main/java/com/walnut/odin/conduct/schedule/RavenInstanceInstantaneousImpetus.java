package com.walnut.odin.conduct.schedule;

import java.time.LocalDateTime;
import java.util.Collection;

import com.pinecone.hydra.system.ko.MetaPersistenceException;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.conduct.schedule.entity.InstanceDepartureResult;
import com.walnut.odin.dispatch.TaskDispatchException;
import com.walnut.odin.dispatch.TaskLaunchContext;
import com.walnut.odin.task.troll.InstanceLaunchException;
import com.walnut.odin.task.troll.LaunchFeature;

public class RavenInstanceInstantaneousImpetus implements InstanceInstantaneousImpetus {

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
                this.mTaskScheduler.taskDispatcher().pipeCreatePrepared( result.getLaunchContexts() );
            }
            return result;
        }
        catch ( MetaPersistenceException e ) {
            throw new TaskDispatchException( e );
        }
    }

}
