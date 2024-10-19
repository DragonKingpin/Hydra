package com.pinecone.hydra.storage.volume.entity;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class ArchVolume implements Volume{
    private long                    enumId;
    private GUID                    guid;
    private LocalDateTime           createTime;
    private LocalDateTime           updateTime;
    private String                  name;
    private String                  type;
    private String                  extConfig;


    @Override
    public long getEnumId() {
        return this.enumId;
    }

    @Override
    public GUID getGuid() {
        return this.guid;
    }

    @Override
    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getExtConfig() {
        return this.extConfig;
    }

    @Override
    public void setExtConfig(String extConfig) {
        this.extConfig = extConfig;
    }
}
