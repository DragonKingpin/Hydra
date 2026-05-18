package com.pinecone.hydra.storage.volume.core;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.io.IOException;

public interface Volume extends Pinenut {
    GUID getGuid();

    String getName();

    VolumeType getVolumeType();

    VolumeMappingMode getMappingMode();

    VolumeAllocationMode getAllocationMode();

    String getObjectRoot();

    VolumeStatus getStatus();

    long getLogicalSize();

    long getCommittedBytes();

    long getAllocationUnit();

    void flush() throws IOException;
}

