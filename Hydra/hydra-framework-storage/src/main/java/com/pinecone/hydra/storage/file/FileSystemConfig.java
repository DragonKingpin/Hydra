package com.pinecone.hydra.storage.file;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.KernelObjectConfig;

public interface FileSystemConfig extends KernelObjectConfig {
    String getVersionSignature();

    Number getClusterSize();

    GUID getLocalhostGUID();

    Number getmTinyFileStripSizing();

    String getDefaultVolume();

    long getExpiryTime();

    int getmRedisTimeOut();

}
