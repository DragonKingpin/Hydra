package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;

import java.time.LocalDateTime;
import java.util.List;

public interface Layer extends LayerTreeNode {
    void setName( String name );

    void setGuid( GUID guid );

    void setParentGuid( GUID parentGuid );

    GUID getParentGuid();

    List<GUID> getHandleGuids();

    void setHandleGuids( List<GUID> handleGuids );

    GUID addHandleGuid( GUID handleGuid );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );

    LocalDateTime getCreateTime();

    void setCreateTime(LocalDateTime startTime );
}
