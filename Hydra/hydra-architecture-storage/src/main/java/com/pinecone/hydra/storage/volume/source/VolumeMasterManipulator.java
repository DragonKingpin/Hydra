package com.pinecone.hydra.storage.volume.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

public interface VolumeMasterManipulator extends KOIMasterManipulator {
    VolumeManipulator getVolumeManipulator();

    VolumePhysicalManipulator getPhysicalManipulator();

    VolumeExtentManipulator getExtentManipulator();

    VolumeMountManipulator getMountManipulator();

    VolumeEventManipulator getEventManipulator();
}
