package com.pinecone.hydra.storage;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.KernelObjectConfig;

public interface StorageConfig extends KernelObjectConfig {
    GUID getLocalHostGuid();

    String getDefaultVolumeGuid();

    String getDefaultTempFilePath();
}
