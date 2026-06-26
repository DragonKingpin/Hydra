package com.walnut.odin.patrol;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;

public class GenericPatrolWatchdogLog implements PatrolWatchdogLog {

    protected long          mnId;
    protected GUID          mGuid;
    protected String        mszWatchdogName;
    protected String        mszRuleCode;
    protected String        mszRuleName;
    protected String        mszTargetType;
    protected GUID          mTargetGuid;
    protected GUID          mTaskGuid;
    protected GUID          mInstanceGuid;
    protected Long          mExecId;
    protected GUID          mProcessGuid;
    protected String        mszExecutedProcessor;
    protected String        mszPatrolState;
    protected String        mszSeverity;
    protected String        mszActionType;
    protected String        mszActionState;
    protected String        mszMessage;
    protected String        mszPayload;
    protected LocalDateTime mPatrolTime;
    protected LocalDateTime mActionTime;

    @Override
    public long getId() {
        return this.mnId;
    }

    @Override
    public void setId( long id ) {
        this.mnId = id;
    }

    @Override
    public GUID getGuid() {
        return this.mGuid;
    }

    @Override
    public void setGuid( GUID guid ) {
        this.mGuid = guid;
    }

    @Override
    public String getWatchdogName() {
        return this.mszWatchdogName;
    }

    @Override
    public void setWatchdogName( String watchdogName ) {
        this.mszWatchdogName = watchdogName;
    }

    @Override
    public String getRuleCode() {
        return this.mszRuleCode;
    }

    @Override
    public void setRuleCode( String ruleCode ) {
        this.mszRuleCode = ruleCode;
    }

    @Override
    public String getRuleName() {
        return this.mszRuleName;
    }

    @Override
    public void setRuleName( String ruleName ) {
        this.mszRuleName = ruleName;
    }

    @Override
    public String getTargetType() {
        return this.mszTargetType;
    }

    @Override
    public void setTargetType( String targetType ) {
        this.mszTargetType = targetType;
    }

    @Override
    public GUID getTargetGuid() {
        return this.mTargetGuid;
    }

    @Override
    public void setTargetGuid( GUID targetGuid ) {
        this.mTargetGuid = targetGuid;
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
    public Long getExecId() {
        return this.mExecId;
    }

    @Override
    public void setExecId( Long execId ) {
        this.mExecId = execId;
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
    public String getPatrolState() {
        return this.mszPatrolState;
    }

    @Override
    public void setPatrolState( String patrolState ) {
        this.mszPatrolState = patrolState;
    }

    @Override
    public String getSeverity() {
        return this.mszSeverity;
    }

    @Override
    public void setSeverity( String severity ) {
        this.mszSeverity = severity;
    }

    @Override
    public String getActionType() {
        return this.mszActionType;
    }

    @Override
    public void setActionType( String actionType ) {
        this.mszActionType = actionType;
    }

    @Override
    public String getActionState() {
        return this.mszActionState;
    }

    @Override
    public void setActionState( String actionState ) {
        this.mszActionState = actionState;
    }

    @Override
    public String getMessage() {
        return this.mszMessage;
    }

    @Override
    public void setMessage( String message ) {
        this.mszMessage = message;
    }

    @Override
    public String getPayload() {
        return this.mszPayload;
    }

    @Override
    public void setPayload( String payload ) {
        this.mszPayload = payload;
    }

    @Override
    public LocalDateTime getPatrolTime() {
        return this.mPatrolTime;
    }

    @Override
    public void setPatrolTime( LocalDateTime patrolTime ) {
        this.mPatrolTime = patrolTime;
    }

    @Override
    public LocalDateTime getActionTime() {
        return this.mActionTime;
    }

    @Override
    public void setActionTime( LocalDateTime actionTime ) {
        this.mActionTime = actionTime;
    }
}
