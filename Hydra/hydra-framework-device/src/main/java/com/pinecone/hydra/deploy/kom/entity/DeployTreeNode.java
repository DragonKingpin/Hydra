package com.pinecone.hydra.deploy.kom.entity;

import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface DeployTreeNode extends TreeNode {
    String getName();

    default String getMetaType() {
        return this.className().replace("Generic","");
    }

    default DeployTreeNode evinceTreeNode(){
        return this;
    }

    default ElementNode evinceElementNode(){
        return null;
    }
}
