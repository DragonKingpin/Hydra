package com.pinecone.hydra.deployment.entity.iface;

import com.pinecone.hydra.unit.udsn.entity.TreeNode;

public interface Deploy extends TreeNode {
    String getStatus();
    void setStatus(String status);
}
