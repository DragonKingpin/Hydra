package com.walnut.odin.conduct.schedule.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.task.RavenTaskInstance;

public class ScheduledTaskInstanceFrame implements Pinenut {

    protected TaskScheduleContext context;
    protected RavenTaskInstance instance;
    protected boolean created;

    public ScheduledTaskInstanceFrame( TaskScheduleContext context, RavenTaskInstance instance ) {
        this( context, instance, true );
    }

    public ScheduledTaskInstanceFrame( TaskScheduleContext context, RavenTaskInstance instance, boolean created ) {
        this.context = context;
        this.instance = instance;
        this.created = created;
    }

    public TaskScheduleContext getContext() {
        return this.context;
    }

    public void setContext( TaskScheduleContext context ) {
        this.context = context;
    }

    public RavenTaskInstance getInstance() {
        return this.instance;
    }

    public void setInstance( RavenTaskInstance instance ) {
        this.instance = instance;
    }

    public boolean isCreated() {
        return this.created;
    }

    public void setCreated( boolean created ) {
        this.created = created;
    }
}
