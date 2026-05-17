package com.pinecone.hydra.business.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;

public class GenericNodeCachePath implements NodeCachePath {

    protected long          mnEnumId;
    protected GUID          mGuid;
    protected String        mszPath;
    protected String        mszLongPath;
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
    public String getPath() {
        return this.mszPath;
    }

    @Override
    public void setPath( String szPath ) {
        this.mszPath = szPath;
    }

    @Override
    public String getLongPath() {
        return this.mszLongPath;
    }

    @Override
    public void setLongPath( String szLongPath ) {
        this.mszLongPath = szLongPath;
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
