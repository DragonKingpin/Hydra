package com.pinecone.hydra.storage.lifecycle;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class GenericStorageLifecycleTask implements StorageLifecycleTask {
    protected long                          mnEnumId;
    protected GUID                          mGuid;
    protected StorageLifecycleTaskType      mTaskType;
    protected StorageLifecycleTargetType    mTargetType;
    protected GUID                          mTargetGuid;
    protected String                        mszTargetName;
    protected StorageLifecycleOperationMode mOperationMode;
    protected StorageLifecycleTaskStatus    mStatus;
    protected StorageLifecyclePhase         mPhase;
    protected long                          mnTotalCount;
    protected long                          mnDoneCount;
    protected String                        mszLastCursor;
    protected StorageLifecycleRiskLevel     mRiskLevel;
    protected String                        mszMessage;
    protected String                        mszErrorMessage;
    protected String                        mszPlanSnapshot;
    protected String                        mszResultSnapshot;
    protected GUID                          mOperatorGuid;
    protected String                        mszExtConfig;
    protected LocalDateTime                 mCreateTime;
    protected LocalDateTime                 mUpdateTime;

    public long getEnumId() {
        return this.mnEnumId;
    }

    public void setEnumId( long enumId ) {
        this.mnEnumId = enumId;
    }

    public GUID getGuid() {
        return this.mGuid;
    }

    public void setGuid( GUID guid ) {
        this.mGuid = guid;
    }

    public StorageLifecycleTaskType getTaskType() {
        return this.mTaskType;
    }

    public void setTaskType( StorageLifecycleTaskType taskType ) {
        this.mTaskType = taskType;
    }

    public StorageLifecycleTargetType getTargetType() {
        return this.mTargetType;
    }

    public void setTargetType( StorageLifecycleTargetType targetType ) {
        this.mTargetType = targetType;
    }

    public GUID getTargetGuid() {
        return this.mTargetGuid;
    }

    public void setTargetGuid( GUID targetGuid ) {
        this.mTargetGuid = targetGuid;
    }

    public String getTargetName() {
        return this.mszTargetName;
    }

    public void setTargetName( String targetName ) {
        this.mszTargetName = targetName;
    }

    public StorageLifecycleOperationMode getOperationMode() {
        return this.mOperationMode;
    }

    public void setOperationMode( StorageLifecycleOperationMode operationMode ) {
        this.mOperationMode = operationMode;
    }

    public StorageLifecycleTaskStatus getStatus() {
        return this.mStatus;
    }

    public void setStatus( StorageLifecycleTaskStatus status ) {
        this.mStatus = status;
    }

    public StorageLifecyclePhase getPhase() {
        return this.mPhase;
    }

    public void setPhase( StorageLifecyclePhase phase ) {
        this.mPhase = phase;
    }

    public long getTotalCount() {
        return this.mnTotalCount;
    }

    public void setTotalCount( long totalCount ) {
        this.mnTotalCount = totalCount;
    }

    public long getDoneCount() {
        return this.mnDoneCount;
    }

    public void setDoneCount( long doneCount ) {
        this.mnDoneCount = doneCount;
    }

    public String getLastCursor() {
        return this.mszLastCursor;
    }

    public void setLastCursor( String lastCursor ) {
        this.mszLastCursor = lastCursor;
    }

    public StorageLifecycleRiskLevel getRiskLevel() {
        return this.mRiskLevel;
    }

    public void setRiskLevel( StorageLifecycleRiskLevel riskLevel ) {
        this.mRiskLevel = riskLevel;
    }

    public String getMessage() {
        return this.mszMessage;
    }

    public void setMessage( String message ) {
        this.mszMessage = message;
    }

    public String getErrorMessage() {
        return this.mszErrorMessage;
    }

    public void setErrorMessage( String errorMessage ) {
        this.mszErrorMessage = errorMessage;
    }

    public String getPlanSnapshot() {
        return this.mszPlanSnapshot;
    }

    public void setPlanSnapshot( String planSnapshot ) {
        this.mszPlanSnapshot = planSnapshot;
    }

    public String getResultSnapshot() {
        return this.mszResultSnapshot;
    }

    public void setResultSnapshot( String resultSnapshot ) {
        this.mszResultSnapshot = resultSnapshot;
    }

    public GUID getOperatorGuid() {
        return this.mOperatorGuid;
    }

    public void setOperatorGuid( GUID operatorGuid ) {
        this.mOperatorGuid = operatorGuid;
    }

    public String getExtConfig() {
        return this.mszExtConfig;
    }

    public void setExtConfig( String extConfig ) {
        this.mszExtConfig = extConfig;
    }

    public LocalDateTime getCreateTime() {
        return this.mCreateTime;
    }

    public void setCreateTime( LocalDateTime createTime ) {
        this.mCreateTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return this.mUpdateTime;
    }

    public void setUpdateTime( LocalDateTime updateTime ) {
        this.mUpdateTime = updateTime;
    }
}
