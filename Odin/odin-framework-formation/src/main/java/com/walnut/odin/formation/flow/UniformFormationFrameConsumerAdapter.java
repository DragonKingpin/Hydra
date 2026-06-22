package com.walnut.odin.formation.flow;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousMode;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousSubmitRequest;
import com.walnut.odin.conduct.schedule.entity.TaskInstantaneousSubmitResult;
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
        request.setMode( TaskInstantaneousMode.Immediate );
        request.setAllowLineageBypass( true );
        request.setAllowInstantaneousDepartureBypass( true );

        TaskInstantaneousSubmitResult result = this.mTaskScheduler.submitInstantaneousTask( request );
        return new GenericFormationFrameFeedback( frame, result, this.resolveInstanceGuid( result ) != null );
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
}
