package com.pinecone.hydra.storage.file;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.KernelObjectConfig;

import java.util.concurrent.TimeUnit;

public interface FileSystemConfig extends KernelObjectConfig {
    String getVersionSignature();

    Number getClusterSize();

    GUID getLocalhostGUID();

    Number getTinyFileStripSizing();

    String getDefaultVolume();

    long getExpiryTime();

    int getRedisTimeOut();

}
