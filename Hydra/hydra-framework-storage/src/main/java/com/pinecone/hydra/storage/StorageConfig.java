package com.pinecone.hydra.storage;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface StorageConfig extends Pinenut {
    GUID getLocalHostGuid();

    String getDefaultVolumeGuid();

    String getDefaultTempFilePath();
}
