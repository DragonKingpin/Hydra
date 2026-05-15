package com.pinecone.hydra.storage.volume;

import com.pinecone.framework.system.prototype.Pinenut;

public interface VolumeConfig extends Pinenut {
    String getTitanHomeDirectory();

    String getVolumeDataDirectory();

    String getBlockBackingFileName();

    String getObjectDataDirectory();

    String getChunkFilePrefix();

    String getChunkFileExtension();

    long getDefaultAllocationUnit();
}
