package com.pinecone.hydra.storage.bucket.purge;

import com.pinecone.framework.system.prototype.Pinenut;

public enum BucketPurgePhase implements Pinenut {
    LOCKING_BUCKET,
    SCANNING,
    RELEASING_FILE_DATA,
    PURGING_FAT,
    PURGING_JOURNAL,
    PURGING_SYMBOLIC,
    PURGING_PATH_CACHE,
    PURGING_TREE,
    PURGING_ENTITY,
    FINALIZING,
    DONE,
    FAILED
}
