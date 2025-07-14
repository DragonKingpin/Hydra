package com.pinecone.hydra.deploy.kom.entity;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class GenericDeployInsMapping implements DeployInsMapping {
    protected long                      mEnumId;

    protected GUID                      mDeployGuid;

    protected GUID                      mServiceInsGuid;

    protected LocalDateTime             mCreateTime;

    protected LocalDateTime             mUpdateTime;

    @Override
    public void setEnumId(long enumId) {
        this.mEnumId = enumId;
    }

    @Override
    public long getEnumId() {
        return this.mEnumId;
    }

    @Override
    public void setDeployGuid(GUID deployGuid) {
        this.mDeployGuid = deployGuid;
    }

    @Override
    public GUID getDeployGuid() {
        return this.mDeployGuid;
    }

    @Override
    public void setServiceInsGuid(GUID serviceInsGuid) {
        this.mServiceInsGuid = serviceInsGuid;
    }

    @Override
    public GUID getServiceInsGuid() {
        return this.mServiceInsGuid;
    }

    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.mCreateTime = createTime;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.mCreateTime;
    }

    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.mUpdateTime = updateTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.mUpdateTime;
    }
}
