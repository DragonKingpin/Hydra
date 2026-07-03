package com.pinecone.hydra.storage.volume.core;

import com.pinecone.framework.system.prototype.Pinenut;

public enum VolumeFreeIntentStatus implements Pinenut {
    PENDING,
    APPLIED,
    MERGED,
    ABORTED
}
