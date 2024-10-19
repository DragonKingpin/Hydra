package com.pinecone.hydra.storage.volume.entity.local;

import com.pinecone.hydra.storage.volume.entity.ArchVolume;
import com.pinecone.hydra.storage.volume.entity.VolumeCapacity;

public class TitanLocalPhysicalVolume extends ArchVolume implements LocalPhysicalVolume{
    @Override
    public String getMountInletPath() {
        return null;
    }

    @Override
    public void setMountInletPath(String mountInletPath) {

    }

    @Override
    public VolumeCapacity getVolumeCapacity() {
        return null;
    }

    @Override
    public void setVolumeCapacity(VolumeCapacity volumeCapacity) {

    }
}
