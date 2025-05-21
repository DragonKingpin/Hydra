package com.pinecone.hydra.unit.imperium.entity;

import com.pinecone.framework.util.id.GUID;

public interface BranchNode extends EntityNode {

    String getName();

    GUID getGuid();

    default String getMetaType() {
        return this.className().replace("Generic","");
    }

    default BranchNode evinceBranchNode(){
        return this;
    }

}
