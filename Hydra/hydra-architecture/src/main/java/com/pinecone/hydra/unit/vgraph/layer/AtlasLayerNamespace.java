package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class AtlasLayerNamespace implements LayerNamespace {
    protected GUID              mGuid;

    protected String            mszName;

    protected LocalDateTime     mCreateTime;

    protected LocalDateTime     mUpdateTime;

    public AtlasLayerNamespace() {
        this.mCreateTime = LocalDateTime.now();
        this.mUpdateTime = LocalDateTime.now();
    }

    @Override
    public GUID getGuid() {
        return this.mGuid;
    }

    @Override
    public void setGuid(GUID guid) {
        this.mGuid = guid;
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public void setName(String name) {
        this.mszName = name;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.mUpdateTime;
    }

    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.mUpdateTime = updateTime;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.mCreateTime;
    }

    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.mCreateTime = createTime;
    }
}
