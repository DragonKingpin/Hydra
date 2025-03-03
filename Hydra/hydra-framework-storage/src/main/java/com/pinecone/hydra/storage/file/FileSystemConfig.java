package com.pinecone.hydra.storage.file;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.StorageConfig;
import com.pinecone.hydra.system.ko.KernelObjectConfig;

public interface FileSystemConfig extends StorageConfig {
    String getVersionSignature();

    Number getClusterSize();

    GUID getLocalhostGUID();

    Number getmTinyFileStripSizing();

    long getPathQueryExpiryTimeHotMil();

}
