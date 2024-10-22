package com.pinecone.hydra.storage.volume.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.volume.VolumeTree;
import com.pinecone.hydra.storage.volume.entity.local.LocalPhysicalVolume;
import com.pinecone.hydra.storage.volume.entity.local.LocalSimpleVolume;
import com.pinecone.hydra.storage.volume.entity.local.LocalSpannedVolume;
import com.pinecone.hydra.storage.volume.entity.local.LocalStripedVolume;
import com.pinecone.hydra.storage.volume.source.PhysicalVolumeManipulator;
import com.pinecone.hydra.storage.volume.source.SimpleVolumeManipulator;
import com.pinecone.hydra.storage.volume.source.SpannedVolumeManipulator;
import com.pinecone.hydra.storage.volume.source.StripedVolumeManipulator;
import com.pinecone.hydra.storage.volume.source.VolumeCapacityManipulator;

public interface VolumeAllotment extends Pinenut {
    VolumeCapacity          newVolumeCapacity();
    LocalStripedVolume      newLocalStripedVolume();
    LocalSpannedVolume      newLocalSpannedVolume();
    LocalSimpleVolume       newLocalSimpleVolume();
    LocalPhysicalVolume     newLocalPhysicalVolume();
    MountPoint              newMountPoint();
}
