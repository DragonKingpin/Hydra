package com.pinecone.hydra.scenario.entity;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class GenericScenarioCommonData implements ScenarioCommonData{
    private long enumId;
    private GUID guid;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public GenericScenarioCommonData() {
    }

    public GenericScenarioCommonData(long enumId, GUID guid, LocalDateTime createTime, LocalDateTime updateTime) {
        this.enumId = enumId;
        this.guid = guid;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }


    public long getEnumId() {
        return enumId;
    }


    public void setEnumId(long enumId) {
        this.enumId = enumId;
    }


    public GUID getGuid() {
        return guid;
    }


    public void setGuid(GUID guid) {
        this.guid = guid;
    }


    public LocalDateTime getCreateTime() {
        return createTime;
    }


    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


    public LocalDateTime getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String toString() {
        return "GenericScenarioCommonData{enumId = " + enumId + ", guid = " + guid + ", createTime = " + createTime + ", updateTime = " + updateTime + "}";
    }
}
