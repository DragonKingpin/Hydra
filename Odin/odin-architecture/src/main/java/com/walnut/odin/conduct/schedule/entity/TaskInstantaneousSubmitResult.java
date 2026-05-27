package com.walnut.odin.conduct.schedule.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.task.RavenTaskInstance;

public class TaskInstantaneousSubmitResult implements Pinenut {

    protected TaskInstantaneousSubmitRequest mRequest;
    protected TaskInstantaneousPrepareResult mPrepareResult;
    protected InstanceDepartureResult        mDepartureResult;

    public TaskInstantaneousSubmitResult(
            TaskInstantaneousSubmitRequest request, TaskInstantaneousPrepareResult prepareResult,
            InstanceDepartureResult departureResult
    ) {
        this.mRequest = request;
        this.mPrepareResult = prepareResult;
        this.mDepartureResult = departureResult;
    }

    public RavenTaskInstance getInstance() {
        if ( this.mPrepareResult == null ) {
            return null;
        }
        return this.mPrepareResult.getInstance();
    }

    public TaskInstantaneousSubmitRequest getRequest() {
        return this.mRequest;
    }

    public void setRequest( TaskInstantaneousSubmitRequest request ) {
        this.mRequest = request;
    }

    public TaskInstantaneousPrepareResult getPrepareResult() {
        return this.mPrepareResult;
    }

    public void setPrepareResult( TaskInstantaneousPrepareResult prepareResult ) {
        this.mPrepareResult = prepareResult;
    }

    public InstanceDepartureResult getDepartureResult() {
        return this.mDepartureResult;
    }

    public void setDepartureResult( InstanceDepartureResult departureResult ) {
        this.mDepartureResult = departureResult;
    }

}
