package com.pinecone.hydra.device.kom.entity;

import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface DeviceTreeNode extends TreeNode {
    String getName();

    default String getMetaType() {
        return this.className().replace("Generic","");
    }

    default DeviceTreeNode evinceTreeNode(){
        return this;
    }

    default ElementNode evinceElementNode(){
        return null;
    }
}
