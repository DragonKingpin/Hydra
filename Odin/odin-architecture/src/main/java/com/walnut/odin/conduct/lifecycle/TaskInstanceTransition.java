package com.walnut.odin.conduct.lifecycle;

import java.util.Collection;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskInstanceStatus;

public class TaskInstanceTransition implements Pinenut {

    protected GUID                         mInstanceGuid;
    protected Collection<TaskInstanceStatus> mFromStatuses;
    protected TaskInstanceStatus           mToStatus;
    protected TaskInstanceTransitionReason mReason;
    protected String                       mszEventContext;

    public TaskInstanceTransition() {
    }

    public TaskInstanceTransition(
            GUID instanceGuid, Collection<TaskInstanceStatus> fromStatuses, TaskInstanceStatus toStatus,
            TaskInstanceTransitionReason reason, String szEventContext
    ) {
        this.mInstanceGuid = instanceGuid;
        this.mFromStatuses = fromStatuses;
        this.mToStatus = toStatus;
        this.mReason = reason;
        this.mszEventContext = szEventContext;
    }

    public static TaskInstanceTransition of(
            GUID instanceGuid, TaskInstanceStatus fromStatus, TaskInstanceStatus toStatus, TaskInstanceTransitionReason reason
    ) {
        return new TaskInstanceTransition( instanceGuid, List.of( fromStatus ), toStatus, reason, "{}" );
    }

    public static TaskInstanceTransition ofAny(
            GUID instanceGuid, Collection<TaskInstanceStatus> fromStatuses, TaskInstanceStatus toStatus, TaskInstanceTransitionReason reason
    ) {
        return new TaskInstanceTransition( instanceGuid, fromStatuses, toStatus, reason, "{}" );
    }

    public GUID getInstanceGuid() {
        return this.mInstanceGuid;
    }

    public void setInstanceGuid( GUID instanceGuid ) {
        this.mInstanceGuid = instanceGuid;
    }

    public Collection<TaskInstanceStatus> getFromStatuses() {
        return this.mFromStatuses;
    }

    public void setFromStatuses( Collection<TaskInstanceStatus> fromStatuses ) {
        this.mFromStatuses = fromStatuses;
    }

    public TaskInstanceStatus getToStatus() {
        return this.mToStatus;
    }

    public void setToStatus( TaskInstanceStatus toStatus ) {
        this.mToStatus = toStatus;
    }

    public TaskInstanceTransitionReason getReason() {
        return this.mReason;
    }

    public void setReason( TaskInstanceTransitionReason reason ) {
        this.mReason = reason;
    }

    public String getEventContext() {
        return this.mszEventContext;
    }

    public void setEventContext( String szEventContext ) {
        this.mszEventContext = szEventContext;
    }
}
