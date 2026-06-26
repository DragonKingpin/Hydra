package com.walnut.odin.patrol;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;

public class GenericRunningExecPatrolEntry implements RunningExecPatrolEntry {

    protected long          mnExecId;
    protected GUID          mTaskGuid;
    protected GUID          mInstanceGuid;
    protected String        mszTaskName;
    protected String        mszInstanceName;
    protected GUID          mProcessGuid;
    protected String        mszExecutedProcessor;
    protected String        mszExecState;
    protected int           mnSequenceCnt;
    protected int           mnCurrentRetryNumber;
    protected LocalDateTime mExecStartTime;
    protected LocalDateTime mExecRunTime;
    protected LocalDateTime mExecUpdateTime;
    protected String        mszInstanceStatus;
    protected int           mnInstanceSequenceCnt;
    protected int           mnInstanceRetryCnt;
    protected Integer       mDryRun;
    protected LocalDateTime mInstanceUpdateTime;

    @Override
    public long getExecId() {
        return this.mnExecId;
    }

    @Override
    public void setExecId( long execId ) {
        this.mnExecId = execId;
    }

    @Override
    public GUID getTaskGuid() {
        return this.mTaskGuid;
    }

    @Override
    public void setTaskGuid( GUID taskGuid ) {
        this.mTaskGuid = taskGuid;
    }

    @Override
    public GUID getInstanceGuid() {
        return this.mInstanceGuid;
    }

    @Override
    public void setInstanceGuid( GUID instanceGuid ) {
        this.mInstanceGuid = instanceGuid;
    }

    @Override
    public String getTaskName() {
        return this.mszTaskName;
    }

    @Override
    public void setTaskName( String taskName ) {
        this.mszTaskName = taskName;
    }

    @Override
    public String getInstanceName() {
        return this.mszInstanceName;
    }

    @Override
    public void setInstanceName( String instanceName ) {
        this.mszInstanceName = instanceName;
    }

    @Override
    public GUID getProcessGuid() {
        return this.mProcessGuid;
    }

    @Override
    public void setProcessGuid( GUID processGuid ) {
        this.mProcessGuid = processGuid;
    }

    @Override
    public String getExecutedProcessor() {
        return this.mszExecutedProcessor;
    }

    @Override
    public void setExecutedProcessor( String executedProcessor ) {
        this.mszExecutedProcessor = executedProcessor;
    }

    @Override
    public String getExecState() {
        return this.mszExecState;
    }

    @Override
    public void setExecState( String execState ) {
        this.mszExecState = execState;
    }

    @Override
    public int getSequenceCnt() {
        return this.mnSequenceCnt;
    }

    @Override
    public void setSequenceCnt( int sequenceCnt ) {
        this.mnSequenceCnt = sequenceCnt;
    }

    @Override
    public int getCurrentRetryNumber() {
        return this.mnCurrentRetryNumber;
    }

    @Override
    public void setCurrentRetryNumber( int currentRetryNumber ) {
        this.mnCurrentRetryNumber = currentRetryNumber;
    }

    @Override
    public LocalDateTime getExecStartTime() {
        return this.mExecStartTime;
    }

    @Override
    public void setExecStartTime( LocalDateTime execStartTime ) {
        this.mExecStartTime = execStartTime;
    }

    @Override
    public LocalDateTime getExecRunTime() {
        return this.mExecRunTime;
    }

    @Override
    public void setExecRunTime( LocalDateTime execRunTime ) {
        this.mExecRunTime = execRunTime;
    }

    @Override
    public LocalDateTime getExecUpdateTime() {
        return this.mExecUpdateTime;
    }

    @Override
    public void setExecUpdateTime( LocalDateTime execUpdateTime ) {
        this.mExecUpdateTime = execUpdateTime;
    }

    @Override
    public String getInstanceStatus() {
        return this.mszInstanceStatus;
    }

    @Override
    public void setInstanceStatus( String instanceStatus ) {
        this.mszInstanceStatus = instanceStatus;
    }

    @Override
    public int getInstanceSequenceCnt() {
        return this.mnInstanceSequenceCnt;
    }

    @Override
    public void setInstanceSequenceCnt( int instanceSequenceCnt ) {
        this.mnInstanceSequenceCnt = instanceSequenceCnt;
    }

    @Override
    public int getInstanceRetryCnt() {
        return this.mnInstanceRetryCnt;
    }

    @Override
    public void setInstanceRetryCnt( int instanceRetryCnt ) {
        this.mnInstanceRetryCnt = instanceRetryCnt;
    }

    @Override
    public Integer getDryRun() {
        return this.mDryRun;
    }

    @Override
    public void setDryRun( Integer dryRun ) {
        this.mDryRun = dryRun;
    }

    @Override
    public LocalDateTime getInstanceUpdateTime() {
        return this.mInstanceUpdateTime;
    }

    @Override
    public void setInstanceUpdateTime( LocalDateTime instanceUpdateTime ) {
        this.mInstanceUpdateTime = instanceUpdateTime;
    }
}
