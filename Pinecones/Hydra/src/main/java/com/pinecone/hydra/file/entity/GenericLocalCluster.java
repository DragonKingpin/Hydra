package com.pinecone.hydra.file.entity;

import java.time.LocalDateTime;

public class GenericLocalCluster extends ArchCluster implements LocalCluster{
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String sourceName;

    public GenericLocalCluster() {
    }

    public GenericLocalCluster(LocalDateTime createTime, LocalDateTime updateTime, String sourceName) {
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.sourceName = sourceName;
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


    public String getSourceName() {
        return sourceName;
    }


    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String toString() {
        return "GenericLocalCluster{createTime = " + createTime + ", updateTime = " + updateTime + ", sourceName = " + sourceName + "}";
    }
}
