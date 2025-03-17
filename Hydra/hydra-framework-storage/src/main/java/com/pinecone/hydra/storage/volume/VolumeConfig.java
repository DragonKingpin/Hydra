package com.pinecone.hydra.storage.volume;

import com.pinecone.hydra.storage.StorageConfig;

public interface VolumeConfig extends StorageConfig {
    String getVersionSignature();

    Number getTinyFileStripSizing() ;

    Number getSmallFileStripSizing() ;

    Number getMegaFileStripSizing() ;

    Number getDefaultStripSize() ;

    int getStripResidentCacheAllotRatio();

    String getStorageObjectExtension();

    String getSqliteFileExtension();

    String getPathSeparator();
}
