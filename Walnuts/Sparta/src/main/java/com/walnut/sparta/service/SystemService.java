package com.walnut.sparta.service;

import com.pinecone.hydra.unit.udsn.UUIDDistributedScopeNode;

public interface SystemService {
    void saveNode(UUIDDistributedScopeNode node);
     String deleteNode(String uuid);
}
