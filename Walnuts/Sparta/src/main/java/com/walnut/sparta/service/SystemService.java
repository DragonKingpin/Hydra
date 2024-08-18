package com.walnut.sparta.service;

import com.pinecone.hydra.unit.udsn.GUIDDistributedScopeNode;

public interface SystemService {
    void saveNode(GUIDDistributedScopeNode node);
     String deleteNode(String uuid);
}
