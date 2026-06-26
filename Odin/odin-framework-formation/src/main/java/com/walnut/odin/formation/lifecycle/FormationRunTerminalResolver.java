package com.walnut.odin.formation.lifecycle;

import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.formation.FormationRunStatus;
import com.walnut.odin.formation.entity.RunEntry;
import com.walnut.odin.formation.source.RunManipulator;

public class FormationRunTerminalResolver {

    protected RunManipulator mRunManipulator;

    public FormationRunTerminalResolver( RunManipulator runManipulator ) {
        this.mRunManipulator = runManipulator;
    }

    public void resolve( GUID runGuid ) {
        RunEntry run = this.mRunManipulator.selectByGuid( runGuid );
        if ( run == null || !FormationRunStatus.Running.getName().equals( run.getRunStatus() ) ) {
            return;
        }

        long nDone = run.getCompletedCount() + run.getFailedCount();
        if ( nDone < run.getTotalCount() ) {
            return;
        }

        this.mRunManipulator.updateRunningTerminalStatus( runGuid, this.resolveTerminalStatus( run ).getName() );
    }

    protected FormationRunStatus resolveTerminalStatus( RunEntry run ) {
        if ( run.getFailedCount() <= 0L ) {
            return FormationRunStatus.Completed;
        }
        if ( run.getCompletedCount() <= 0L ) {
            return FormationRunStatus.Failed;
        }
        return FormationRunStatus.PartialCompleted;
    }
}
