package com.pinecone.hydra.storage.lifecycle.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleRequest;
import com.pinecone.hydra.storage.lifecycle.StorageLifecycleTask;

public interface StorageLifecycleExecutor extends Pinenut {
    StorageLifecycleTask execute( StorageLifecycleRequest request );

    void cancel( GUID taskGuid );
}
