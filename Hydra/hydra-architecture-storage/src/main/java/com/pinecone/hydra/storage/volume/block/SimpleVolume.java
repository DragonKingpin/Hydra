package com.pinecone.hydra.storage.volume.block;

import com.pinecone.hydra.storage.volume.core.Volume;
import com.pinecone.hydra.storage.volume.core.VolumeExtent;

public interface SimpleVolume extends Volume {
    VolumeExtent getBackingExtent();

    void setBackingExtent( VolumeExtent backingExtent );
}
