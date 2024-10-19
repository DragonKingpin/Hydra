package com.pinecone.hydra.storage.volume.entity;

import com.pinecone.framework.util.id.GUID;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

public abstract class ArchLogicVolume extends ArchVolume implements LogicVolume{

    private List<Volume>            children;
    private VolumeCapacity          volumeCapacity;


    @Override
    public List<Volume> getChildren() {
        return this.children;
    }

    @Override
    public void setChildren(List<Volume> children) {
        this.children = children;
    }

    @Override
    public VolumeCapacity getVolumeCapacity() {
        return this.volumeCapacity;
    }

    @Override
    public void setVolumeCapacity(VolumeCapacity volumeCapacity) {
        this.volumeCapacity = volumeCapacity;
    }
}
