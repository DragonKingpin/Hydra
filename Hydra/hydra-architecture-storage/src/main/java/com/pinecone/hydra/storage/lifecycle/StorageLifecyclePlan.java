package com.pinecone.hydra.storage.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.ArrayList;
import java.util.List;

public class StorageLifecyclePlan implements Pinenut {
    protected StorageLifecycleTaskType      mTaskType;
    protected StorageLifecycleTargetType    mTargetType;
    protected GUID                          mTargetGuid;
    protected String                        mszTargetName;
    protected StorageLifecycleOperationMode mOperationMode;
    protected StorageLifecycleTaskStatus    mStatus = StorageLifecycleTaskStatus.PREPARED;
    protected StorageLifecycleRiskLevel     mRiskLevel = StorageLifecycleRiskLevel.NORMAL;
    protected long                          mnEstimatedTotalCount;
    protected final List<StorageLifecycleBlocker> mBlockers = new ArrayList<>();
    protected final List<StorageLifecycleWarning> mWarnings = new ArrayList<>();
    protected final List<StorageLifecycleAction>  mActions = new ArrayList<>();

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

    public StorageLifecycleRiskLevel getRiskLevel() {
        return this.mRiskLevel;
    }

    public void setRiskLevel( StorageLifecycleRiskLevel riskLevel ) {
        this.mRiskLevel = riskLevel;
    }

    public long getEstimatedTotalCount() {
        return this.mnEstimatedTotalCount;
    }

    public void setEstimatedTotalCount( long estimatedTotalCount ) {
        this.mnEstimatedTotalCount = estimatedTotalCount;
    }

    public List<StorageLifecycleBlocker> getBlockers() {
        return this.mBlockers;
    }

    public List<StorageLifecycleWarning> getWarnings() {
        return this.mWarnings;
    }

    public List<StorageLifecycleAction> getActions() {
        return this.mActions;
    }

    public boolean isExecutable() {
        return this.mBlockers.isEmpty();
    }

    public StorageLifecycleBlocker addBlocker( String code, String message, long count ) {
        StorageLifecycleBlocker blocker = new StorageLifecycleBlocker( code, message, count );
        this.mBlockers.add( blocker );
        this.mStatus = StorageLifecycleTaskStatus.BLOCKED;
        return blocker;
    }

    public StorageLifecycleWarning addWarning( String code, String message, long count ) {
        StorageLifecycleWarning warning = new StorageLifecycleWarning( code, message, count );
        this.mWarnings.add( warning );
        return warning;
    }

    public StorageLifecycleAction addAction( String code, String message, long count ) {
        StorageLifecycleAction action = new StorageLifecycleAction( code, message, count );
        this.mActions.add( action );
        return action;
    }
}
