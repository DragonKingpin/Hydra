package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface LayerNamespace extends LayerTreeNode {
    GUID getGuid();

    void setGuid( GUID guid );

    String getName();

    void setName( String name );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );
}
