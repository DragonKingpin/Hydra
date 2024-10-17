package com.pinecone.hydra.storage.volume.entity;

import java.util.List;

public interface LogicVolume extends Volume {
    String getName();
    void setName( String name );
    List<Volume> getChildren();
    void setChildren( List<Volume> children );
    VolumeCapacity getVolumeCapacity();
    void setVolumeCapacity( VolumeCapacity volumeCapacity );
}
