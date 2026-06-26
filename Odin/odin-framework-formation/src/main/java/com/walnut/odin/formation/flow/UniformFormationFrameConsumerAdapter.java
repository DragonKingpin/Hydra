package com.walnut.odin.formation.flow;

import java.time.LocalDateTime;
import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.conduct.schedule.entity.InstanceDepartureResult;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousContext;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousMode;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousPrepareResult;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousSubmitRequest;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousSubmitResult;
import com.walnut.odin.dispatch.TaskDispatchException;
import com.walnut.odin.formation.plan.FormationFrame;
import com.walnut.odin.formation.plan.FormationFrameFeedback;
import com.walnut.odin.formation.plan.GenericFormationFrameFeedback;
import com.walnut.odin.task.RavenTaskInstance;

public class UniformFormationFrameConsumerAdapter implements FormationFrameConsumerAdapter {
    protected UniformTaskScheduler mTaskScheduler;

    public UniformFormationFrameConsumerAdapter( UniformTaskScheduler taskScheduler ) {
        this.mTaskScheduler = taskScheduler;
    }

    @Override
    public FormationFrameFeedback consumeFrame( FormationFrame frame ) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        TaskInstantaneousSubmitRequest request = new TaskInstantaneousSubmitRequest();
        request.setTaskGuid( frame.taskGuid() );
        request.setExpectTime( now );
        request.setFireTime( now );
        request.setBusinessTimeEpoch( now );
        request.setMode( TaskInstantaneousMode.Temporary );
        request.setAllowLineageBypass( true );
        request.setAllowInstantaneousDepartureBypass( true );

        TaskInstantaneousSubmitResult result = this.submitFormationTask( request );
        return new GenericFormationFrameFeedback( frame, result, this.resolveInstanceGuid( result ) != null );
    }

    protected TaskInstantaneousSubmitResult submitFormationTask( TaskInstantaneousSubmitRequest request ) throws Exception {
        TaskInstantaneousContext context = request.toContext();
        TaskInstantaneousPrepareResult prepareResult = this.mTaskScheduler.taskInstantaneousPreparator().prepare( context );
        InstanceDepartureResult departureResult = null;
        try {
            departureResult = this.mTaskScheduler.instanceInstantaneousImpetus().impel(
                    List.of( prepareResult.getInstance().getInstanceEntry() ),
                    request.getFireTime(),
                    context.toLaunchFeature()
            );
        }
        catch ( TaskDispatchException e ) {
            if ( this.resolveInstanceGuid( prepareResult ) == null ) {
                throw e;
            }
        }
        return new TaskInstantaneousSubmitResult( request, prepareResult, departureResult );
    }

    protected GUID resolveInstanceGuid( TaskInstantaneousSubmitResult result ) {
        if ( result == null ) {
            return null;
        }
        RavenTaskInstance instance = result.getInstance();
        if ( instance == null ) {
            return null;
        }
        InstanceEntry entry = instance.getInstanceEntry();
        if ( entry == null ) {
            return null;
        }
        return entry.getGuid();
    }

    protected GUID resolveInstanceGuid( TaskInstantaneousPrepareResult result ) {
        if ( result == null ) {
            return null;
        }
        RavenTaskInstance instance = result.getInstance();
        if ( instance == null ) {
            return null;
        }
        InstanceEntry entry = instance.getInstanceEntry();
        if ( entry == null ) {
            return null;
        }
        return entry.getGuid();
    }
}
