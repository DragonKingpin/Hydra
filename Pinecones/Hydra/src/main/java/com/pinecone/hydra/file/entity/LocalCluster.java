package com.pinecone.hydra.file.entity;

import java.time.LocalDateTime;

public interface LocalCluster extends Cluster{
    LocalDateTime getCreateTime();
    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();
    void setUpdateTime(LocalDateTime updateTime);

    String getSourceName();
    void setSourceName(String sourceName);

}
