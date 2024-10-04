package com.pinecone.hydra.unit.udtt.entity;

import com.pinecone.framework.util.id.GUID;

public interface TreeNode extends EntityNode {
    String getName();

    GUID getGuid();

    default String getMetaType() {
        return this.className().replace("Generic","");
    }

    default TreeNode evinceTreeNode(){
        return null;
    }
}
