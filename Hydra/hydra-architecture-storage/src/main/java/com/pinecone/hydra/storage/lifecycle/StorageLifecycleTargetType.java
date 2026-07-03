package com.pinecone.hydra.storage.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;

public enum StorageLifecycleTargetType implements Pinenut {
    BUCKET,
    LOGICAL_VOLUME,
    PHYSICAL_VOLUME,
    UOFS_PATH,
    UOFS_PATH_BATCH
}
