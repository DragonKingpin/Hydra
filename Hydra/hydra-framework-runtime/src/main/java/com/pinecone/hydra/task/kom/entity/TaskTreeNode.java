package com.pinecone.hydra.task.kom.entity;

import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface TaskTreeNode extends TreeNode {
    String getName();

    default String getMetaType() {
        return this.className().replace("Generic","");
    }

    default TaskTreeNode evinceTreeNode(){
        return this;
    }

    default ElementNode evinceElementNode(){
        return null;
    }
}
