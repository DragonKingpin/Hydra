package com.walnut.odin.patrol;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface PatrolWatchdogLog extends Pinenut {

    long getId();
    void setId( long id );

    GUID getGuid();
    void setGuid( GUID guid );

    String getWatchdogName();
    void setWatchdogName( String watchdogName );

    String getRuleCode();
    void setRuleCode( String ruleCode );

    String getRuleName();
    void setRuleName( String ruleName );

    String getTargetType();
    void setTargetType( String targetType );

    GUID getTargetGuid();
    void setTargetGuid( GUID targetGuid );

    GUID getTaskGuid();
    void setTaskGuid( GUID taskGuid );

    GUID getInstanceGuid();
    void setInstanceGuid( GUID instanceGuid );

    Long getExecId();
    void setExecId( Long execId );

    GUID getProcessGuid();
    void setProcessGuid( GUID processGuid );

    String getExecutedProcessor();
    void setExecutedProcessor( String executedProcessor );

    String getPatrolState();
    void setPatrolState( String patrolState );

    String getSeverity();
    void setSeverity( String severity );

    String getActionType();
    void setActionType( String actionType );

    String getActionState();
    void setActionState( String actionState );

    String getMessage();
    void setMessage( String message );

    String getPayload();
    void setPayload( String payload );

    LocalDateTime getPatrolTime();
    void setPatrolTime( LocalDateTime patrolTime );

    LocalDateTime getActionTime();
    void setActionTime( LocalDateTime actionTime );
}
