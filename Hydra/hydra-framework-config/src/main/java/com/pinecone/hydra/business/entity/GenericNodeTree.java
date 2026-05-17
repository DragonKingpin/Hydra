package com.pinecone.hydra.business.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;

public class GenericNodeTree implements NodeTree {

    protected long          mnEnumId;
    protected GUID          mGuid;
    protected GUID          mParentGuid;
    protected LocalDateTime mCreateTime;
    protected LocalDateTime mUpdateTime;

    @Override
    public long getEnumId() {
        return this.mnEnumId;
    }

    @Override
    public void setEnumId( long nEnumId ) {
        this.mnEnumId = nEnumId;
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
    public GUID getParentGuid() {
        return this.mParentGuid;
    }

    @Override
    public void setParentGuid( GUID parentGuid ) {
        this.mParentGuid = parentGuid;
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
