package com.pinecone.hydra.storage.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;

public enum StorageLifecycleTaskStatus implements Pinenut {
    PREPARED,
    RUNNING,
    BLOCKED,
    FAILED,
    DONE,
    CANCELED
}
