package com.pinecone.hydra.deploy.entity.iface;

import com.pinecone.hydra.unit.udtt.entity.TreeNode;

public interface Deploy extends TreeNode {
    String getStatus();
    void setStatus(String status);
}
