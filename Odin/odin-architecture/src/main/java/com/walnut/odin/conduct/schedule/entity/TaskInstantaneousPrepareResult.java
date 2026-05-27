package com.walnut.odin.conduct.schedule.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.task.RavenTaskInstance;

public class TaskInstantaneousPrepareResult implements Pinenut {

    protected TaskInstantaneousContext mContext;
    protected RavenTaskInstance        mInstance;

    public TaskInstantaneousPrepareResult( TaskInstantaneousContext context, RavenTaskInstance instance ) {
        this.mContext = context;
        this.mInstance = instance;
    }

    public TaskInstantaneousContext getContext() {
        return this.mContext;
    }

    public void setContext( TaskInstantaneousContext context ) {
        this.mContext = context;
    }

    public RavenTaskInstance getInstance() {
        return this.mInstance;
    }

    public void setInstance( RavenTaskInstance instance ) {
        this.mInstance = instance;
    }

}
