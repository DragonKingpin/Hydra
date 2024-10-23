package com.pinecone.hydra.storage.volume.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.VolumeTree;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

public abstract class ArchLogicVolume extends ArchVolume implements LogicVolume{

    protected List<Volume>            children;
    protected VolumeCapacity          volumeCapacity;

    public ArchLogicVolume(VolumeTree volumeTree) {
        super(volumeTree);
    }

    public ArchLogicVolume(){}



    @Override
    public List<Volume> getChildren() {
        return null;
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
