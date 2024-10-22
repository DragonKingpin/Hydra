package com.pinecone.hydra.storage.volume.entity;

public interface PhysicalVolume extends Volume{
    String getMountInletPath();
    void setMountInletPath( String mountInletPath );
    VolumeCapacity getVolumeCapacity();
    void setVolumeCapacity( VolumeCapacity volumeCapacity );
}
