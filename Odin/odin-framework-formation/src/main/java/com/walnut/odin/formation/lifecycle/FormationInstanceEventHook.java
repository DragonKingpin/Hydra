package com.walnut.odin.formation.lifecycle;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.walnut.odin.conduct.lifecycle.TaskInstanceEventHook;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransition;
import com.walnut.odin.conduct.lifecycle.TaskInstanceTransitionResult;
import com.walnut.odin.formation.source.FrameManipulator;
import com.walnut.odin.formation.source.RunManipulator;

public class FormationInstanceEventHook implements TaskInstanceEventHook {

    protected FrameManipulator mFrameManipulator;
    protected RunManipulator   mRunManipulator;
    protected FormationRunTerminalResolver mTerminalResolver;

    public FormationInstanceEventHook(
            FrameManipulator frameManipulator,
            RunManipulator runManipulator
    ) {
        this.mFrameManipulator = frameManipulator;
        this.mRunManipulator = runManipulator;
        this.mTerminalResolver = new FormationRunTerminalResolver( runManipulator );
    }

    @Override
    public void onTaskInstanceTransition( TaskInstanceTransitionResult result ) {
        if ( result == null || !result.isSucceeded() || result.getTransition() == null ) {
            return;
        }

        TaskInstanceTransition transition = result.getTransition();
        GUID instanceGuid = transition.getInstanceGuid();
        TaskInstanceStatus status = transition.getToStatus();
        if ( instanceGuid == null || status == null ) {
            return;
        }

        if ( this.isCompletedStatus( status ) ) {
            this.completeFrame( instanceGuid );
        }
        else if ( this.isFailedStatus( status ) ) {
            this.failFrame( instanceGuid, transition.getEventContext() );
        }
    }

    protected void completeFrame( GUID instanceGuid ) {
        int nAffected = this.mFrameManipulator.markCompletedByInstanceGuid( instanceGuid );
        if ( nAffected <= 0 ) {
            return;
        }

        GUID runGuid = this.resolveRunGuid( instanceGuid );
        if ( runGuid == null ) {
            return;
        }
        this.mRunManipulator.increaseCompleted( runGuid );
        this.mTerminalResolver.resolve( runGuid );
    }

    protected void failFrame( GUID instanceGuid, String errorCause ) {
        int nAffected = this.mFrameManipulator.markFailedByInstanceGuid( instanceGuid, errorCause );
        if ( nAffected <= 0 ) {
            return;
        }

        GUID runGuid = this.resolveRunGuid( instanceGuid );
        if ( runGuid == null ) {
            return;
        }
        this.mRunManipulator.increaseFailed( runGuid );
        this.mTerminalResolver.resolve( runGuid );
    }

    protected GUID resolveRunGuid( GUID instanceGuid ) {
        com.walnut.odin.formation.dto.FormationFrameQuery query = new com.walnut.odin.formation.dto.FormationFrameQuery();
        query.setInstanceGuid( instanceGuid.toString() );
        java.util.List<com.walnut.odin.formation.plan.FormationFrame> frames = this.mFrameManipulator.pageFrames( query, 0, 1 );
        if ( frames == null || frames.isEmpty() || frames.get( 0 ) == null ) {
            return null;
        }
        return frames.get( 0 ).getRunGuid();
    }

    protected boolean isCompletedStatus( TaskInstanceStatus status ) {
        return status == TaskInstanceStatus.Finished;
    }

    protected boolean isFailedStatus( TaskInstanceStatus status ) {
        return status == TaskInstanceStatus.Error
                || status == TaskInstanceStatus.Killed
                || status == TaskInstanceStatus.AuditFailed;
    }
}
