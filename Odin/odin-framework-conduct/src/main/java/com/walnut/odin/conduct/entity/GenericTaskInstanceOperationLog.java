package com.walnut.odin.conduct.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;

public class GenericTaskInstanceOperationLog implements TaskInstanceOperationLog {

    protected long          mnId;
    protected GUID          mGuid;
    protected GUID          mTaskGuid;
    protected GUID          mInstanceGuid;
    protected String        mszInstanceName;
    protected String        mszOperationType;
    protected String        mszOperationSource;
    protected String        mszUserIdentifier;
    protected String        mszUserName;
    protected String        mszMessage;
    protected String        mszPayload;
    protected LocalDateTime mOperationTime;
    protected LocalDateTime mCreateTime;
    protected LocalDateTime mUpdateTime;

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
    public String getInstanceName() {
        return this.mszInstanceName;
    }

    @Override
    public void setInstanceName( String instanceName ) {
        this.mszInstanceName = instanceName;
    }

    @Override
    public String getOperationType() {
        return this.mszOperationType;
    }

    @Override
    public void setOperationType( String operationType ) {
        this.mszOperationType = operationType;
    }

    @Override
    public String getOperationSource() {
        return this.mszOperationSource;
    }

    @Override
    public void setOperationSource( String operationSource ) {
        this.mszOperationSource = operationSource;
    }

    @Override
    public String getUserIdentifier() {
        return this.mszUserIdentifier;
    }

    @Override
    public void setUserIdentifier( String userIdentifier ) {
        this.mszUserIdentifier = userIdentifier;
    }

    @Override
    public String getUserName() {
        return this.mszUserName;
    }

    @Override
    public void setUserName( String userName ) {
        this.mszUserName = userName;
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
    public LocalDateTime getOperationTime() {
        return this.mOperationTime;
    }

    @Override
    public void setOperationTime( LocalDateTime operationTime ) {
        this.mOperationTime = operationTime;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.mCreateTime;
    }

    @Override
    public void setCreateTime( LocalDateTime createTime ) {
        this.mCreateTime = createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.mUpdateTime;
    }

    @Override
    public void setUpdateTime( LocalDateTime updateTime ) {
        this.mUpdateTime = updateTime;
    }
}
