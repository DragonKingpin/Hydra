package com.pinecone.hydra.storage.lifecycle.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.lifecycle.StorageLifecyclePlan;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleRequest;

public interface StorageLifecyclePlanner extends Pinenut {
    StorageLifecyclePlan plan( StorageLifecycleRequest request );
}
