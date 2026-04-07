package com.pinecone.hydra.unit.vgraph.layer;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

import java.time.LocalDateTime;

public interface LayerTreeNode extends TreeNode {
    GUID getGuid();

    void setUpdateTime( LocalDateTime updateTime );
}
