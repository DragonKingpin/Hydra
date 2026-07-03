package com.pinecone.hydra.storage.file.delete;

import com.pinecone.framework.system.prototype.Pinenut;

public enum UofsDeleteTargetType implements Pinenut {
    PATH_CACHE,
    FILE_DATA,
    NATIVE_EXTERNAL_TARGET,
    HARDLINK_EDGE,
    INTERNAL_SYMBOLIC_METADATA,
    EXTERNAL_SYMBOLIC_METADATA,
    FILE_METADATA,
    FOLDER_METADATA
}
