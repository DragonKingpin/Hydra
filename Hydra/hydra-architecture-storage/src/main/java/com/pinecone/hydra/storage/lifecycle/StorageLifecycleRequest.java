package com.pinecone.hydra.storage.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class StorageLifecycleRequest implements Pinenut {
    protected StorageLifecycleTaskType      mTaskType;
    protected StorageLifecycleTargetType    mTargetType;
    protected GUID                          mTargetGuid;
    protected String                        mszTargetName;
    protected StorageLifecycleOperationMode mOperationMode;
    protected GUID                          mOperatorGuid;
    protected String                        mszExtConfig;

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
}
