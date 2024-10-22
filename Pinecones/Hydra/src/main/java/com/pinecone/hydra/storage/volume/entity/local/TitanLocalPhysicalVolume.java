package com.pinecone.hydra.storage.volume.entity.local;

import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.storage.volume.VolumeTree;
import com.pinecone.hydra.storage.volume.entity.ArchVolume;
import com.pinecone.hydra.storage.volume.entity.VolumeCapacity;
import com.pinecone.hydra.storage.volume.source.PhysicalVolumeManipulator;

public class TitanLocalPhysicalVolume extends ArchVolume implements LocalPhysicalVolume{
    private String                      mountInletPath;
    private VolumeCapacity              volumeCapacity;
    private PhysicalVolumeManipulator   physicalVolumeManipulator;

    public TitanLocalPhysicalVolume(VolumeTree volumeTree, PhysicalVolumeManipulator physicalVolumeManipulator) {
        super(volumeTree);
        this.physicalVolumeManipulator = physicalVolumeManipulator;
    }

    @Override
    public String getMountInletPath() {
        return this.mountInletPath;
    }

    @Override
    public void setMountInletPath(String mountInletPath) {
        this.mountInletPath = mountInletPath;
    }

    @Override
    public VolumeCapacity getVolumeCapacity() {
        return this.volumeCapacity;
    }

    @Override
    public void setVolumeCapacity(VolumeCapacity volumeCapacity) {
        this.volumeCapacity = volumeCapacity;
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
