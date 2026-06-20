package com.pinecone.hydra.storage.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;

public enum StorageLifecycleTaskType implements Pinenut {
    BUCKET_PURGE,
    BUCKET_FORMAT,
    LOGICAL_VOLUME_RETIRE,
    PHYSICAL_VOLUME_RETIRE
}
