package com.pinecone.hydra.storage.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;

public enum StorageLifecycleOperationMode implements Pinenut {
    SLOW_PURGE,
    SLOW_FORMAT,
    RETIRE
}
