package com.walnut.odin.conduct.schedule.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public class TaskSchedulerIdentitySnapshot implements Pinenut {

    protected boolean mSchedulerEnabled;
    protected String  mSchedulerMode;
    protected String  mNodeId;
    protected String  mPartitionName;

    public boolean isSchedulerEnabled() {
        return this.mSchedulerEnabled;
    }

    public void setSchedulerEnabled( boolean schedulerEnabled ) {
        this.mSchedulerEnabled = schedulerEnabled;
    }

    public String getSchedulerMode() {
        return this.mSchedulerMode;
    }

    public void setSchedulerMode( String schedulerMode ) {
        this.mSchedulerMode = schedulerMode;
    }

    public String getNodeId() {
        return this.mNodeId;
    }

    public void setNodeId( String nodeId ) {
        this.mNodeId = nodeId;
    }

    public String getPartitionName() {
        return this.mPartitionName;
    }

    public void setPartitionName( String partitionName ) {
        this.mPartitionName = partitionName;
    }
}
