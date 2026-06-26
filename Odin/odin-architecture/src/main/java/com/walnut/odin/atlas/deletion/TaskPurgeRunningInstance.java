package com.walnut.odin.atlas.deletion;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class TaskPurgeRunningInstance implements Pinenut {

    protected GUID   taskGuid;
    protected GUID   instanceGuid;
    protected String instanceName;
    protected String runStatus;
    protected long   execId;
    protected GUID   processGuid;
    protected String executedProcessor;
    protected String execState;
    protected int    sequenceCnt;
    protected int    currentRetryNumber;

    public GUID getTaskGuid() {
        return this.taskGuid;
    }

    public void setTaskGuid( GUID taskGuid ) {
        this.taskGuid = taskGuid;
    }

    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    public void setInstanceGuid( GUID instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }

    public String getInstanceName() {
        return this.instanceName;
    }

    public void setInstanceName( String instanceName ) {
        this.instanceName = instanceName;
    }

    public String getRunStatus() {
        return this.runStatus;
    }

    public void setRunStatus( String runStatus ) {
        this.runStatus = runStatus;
    }

    public long getExecId() {
        return this.execId;
    }

    public void setExecId( long execId ) {
        this.execId = execId;
    }

    public GUID getProcessGuid() {
        return this.processGuid;
    }

    public void setProcessGuid( GUID processGuid ) {
        this.processGuid = processGuid;
    }

    public String getExecutedProcessor() {
        return this.executedProcessor;
    }

    public void setExecutedProcessor( String executedProcessor ) {
        this.executedProcessor = executedProcessor;
    }

    public String getExecState() {
        return this.execState;
    }

    public void setExecState( String execState ) {
        this.execState = execState;
    }

    public int getSequenceCnt() {
        return this.sequenceCnt;
    }

    public void setSequenceCnt( int sequenceCnt ) {
        this.sequenceCnt = sequenceCnt;
    }

    public int getCurrentRetryNumber() {
        return this.currentRetryNumber;
    }

    public void setCurrentRetryNumber( int currentRetryNumber ) {
        this.currentRetryNumber = currentRetryNumber;
    }
}
