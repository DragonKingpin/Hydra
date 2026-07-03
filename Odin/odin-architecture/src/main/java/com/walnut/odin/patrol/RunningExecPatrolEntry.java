package com.walnut.odin.patrol;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface RunningExecPatrolEntry extends Pinenut {

    long getExecId();
    void setExecId( long execId );

    GUID getTaskGuid();
    void setTaskGuid( GUID taskGuid );

    GUID getInstanceGuid();
    void setInstanceGuid( GUID instanceGuid );

    String getTaskName();
    void setTaskName( String taskName );

    String getInstanceName();
    void setInstanceName( String instanceName );

    GUID getProcessGuid();
    void setProcessGuid( GUID processGuid );

    String getExecutedProcessor();
    void setExecutedProcessor( String executedProcessor );

    String getExecState();
    void setExecState( String execState );

    int getSequenceCnt();
    void setSequenceCnt( int sequenceCnt );

    int getCurrentRetryNumber();
    void setCurrentRetryNumber( int currentRetryNumber );

    LocalDateTime getExecStartTime();
    void setExecStartTime( LocalDateTime execStartTime );

    LocalDateTime getExecRunTime();
    void setExecRunTime( LocalDateTime execRunTime );

    LocalDateTime getExecUpdateTime();
    void setExecUpdateTime( LocalDateTime execUpdateTime );

    String getInstanceStatus();
    void setInstanceStatus( String instanceStatus );

    int getInstanceSequenceCnt();
    void setInstanceSequenceCnt( int instanceSequenceCnt );

    int getInstanceRetryCnt();
    void setInstanceRetryCnt( int instanceRetryCnt );

    Integer getDryRun();
    void setDryRun( Integer dryRun );

    LocalDateTime getInstanceUpdateTime();
    void setInstanceUpdateTime( LocalDateTime instanceUpdateTime );
}
