package com.walnut.odin.conduct.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;

public class TaskInstanceTransitionResult implements Pinenut {

    protected TaskInstanceTransition mTransition;
    protected int                    mnAffectedRows;

    public TaskInstanceTransitionResult( TaskInstanceTransition transition, int nAffectedRows ) {
        this.mTransition = transition;
        this.mnAffectedRows = nAffectedRows;
    }

    public static TaskInstanceTransitionResult of( TaskInstanceTransition transition, int nAffectedRows ) {
        return new TaskInstanceTransitionResult( transition, nAffectedRows );
    }

    public boolean isSucceeded() {
        return this.mnAffectedRows > 0;
    }

    public TaskInstanceTransition getTransition() {
        return this.mTransition;
    }

    public int getAffectedRows() {
        return this.mnAffectedRows;
    }
}
