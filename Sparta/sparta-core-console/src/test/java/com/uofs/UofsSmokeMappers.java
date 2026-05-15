package com.uofs;

import com.pinecone.hydra.storage.volume.source.VolumeEventManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeExtentManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeMasterManipulator;
import com.pinecone.hydra.storage.volume.source.VolumePhysicalManipulator;

final class UofsSmokeMappers {
    final VolumeManipulator volumeMapper;
    final VolumePhysicalManipulator physicalMapper;
    final VolumeExtentManipulator extentMapper;
    final VolumeEventManipulator eventMapper;

    UofsSmokeMappers( VolumeMasterManipulator masterManipulator ) {
        this.volumeMapper = masterManipulator.getVolumeManipulator();
        this.physicalMapper = masterManipulator.getPhysicalManipulator();
        this.extentMapper = masterManipulator.getExtentManipulator();
        this.eventMapper = masterManipulator.getEventManipulator();
    }
}
