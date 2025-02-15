package com.pinecone.hydra.storage.file.entity;

import com.pinecone.hydra.storage.file.source.LocalClusterManipulator;

import java.time.LocalDateTime;

public interface LocalCluster extends Cluster {
    LocalDateTime getCreateTime();
    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();
    void setUpdateTime(LocalDateTime updateTime);

    String getSourceName();
    void setSourceName(String sourceName);

    @Override
    default LocalCluster evinceLocalCluster() {
        return this;
    }

    void setLocalClusterManipulator(LocalClusterManipulator localClusterManipulator);

    long getDefinitionSize();
    void setDefinitionSize( long definitionSize );

    long getFileStartOffset();
    void setFileStartOffset( long fileStartOffset );
}
