package com.pinecone.hydra.storage.lifecycle.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.lifecycle.StorageLifecyclePhase;

public interface StorageLifecycleProgressListener extends Pinenut {
    void onProgress( GUID taskGuid, StorageLifecyclePhase phase, long totalCount, long doneCount, String lastCursor, String message );
}
