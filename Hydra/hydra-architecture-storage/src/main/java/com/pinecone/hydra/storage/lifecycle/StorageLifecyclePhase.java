package com.pinecone.hydra.storage.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;

public enum StorageLifecyclePhase implements Pinenut {
    PREPARE,
    CHECKING_DEPENDENCY,
    SCANNING,
    REMOVING,
    RELEASING_DATA,
    DELETING_FAT,
    DELETING_METADATA,
    DELETING_BUCKET,
    RETIRING,
    DONE,
    FAILED,
    CANCELED
}
