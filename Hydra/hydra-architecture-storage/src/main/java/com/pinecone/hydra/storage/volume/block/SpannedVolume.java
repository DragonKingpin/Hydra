package com.pinecone.hydra.storage.volume.block;

import com.pinecone.hydra.storage.volume.core.Volume;
import com.pinecone.hydra.storage.volume.core.VolumeExtent;

import java.util.List;

public interface SpannedVolume extends Volume {
    List<VolumeExtent> getExtents();

    void addExtent( VolumeExtent extent );
}
