package com.pinecone.hydra.task.kom.entity;

import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface ServiceTreeNode extends TreeNode {
    String getName();

    default String getMetaType() {
        return this.className().replace("Generic","");
    }

    default ServiceTreeNode evinceTreeNode(){
        return this;
    }

    default ElementNode evinceElementNode(){
        return null;
    }
}
