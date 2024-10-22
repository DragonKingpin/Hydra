package com.pinecone.hydra.storage.volume.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.VolumeTree;
import com.pinecone.hydra.storage.volume.entity.VolumeCapacity;
import com.pinecone.hydra.storage.volume.source.VolumeCapacityManipulator;

public class TitanVolumeCapacity implements VolumeCapacity {
    private long                        enumId;
    private GUID                        guid;
    private long                        definitionCapacity;
    private long                        usedSize;
    private long                        quotaCapacity;
    private VolumeTree                  volumeTree;
    private VolumeCapacityManipulator   volumeCapacityManipulator;

    public TitanVolumeCapacity( VolumeTree volumeTree, VolumeCapacityManipulator volumeCapacityManipulator ){
        this.volumeTree = volumeTree;
        this.volumeCapacityManipulator = volumeCapacityManipulator;
        this.guid = volumeTree.getGuidAllocator().nextGUID72();
    }

    @Override
    public long getEnumId() {
        return this.enumId;
    }

    @Override
    public GUID getGuid() {
        return this.guid;
    }

    @Override
    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    @Override
    public long getDefinitionCapacity() {
        return this.definitionCapacity;
    }

    @Override
    public void setDefinitionCapacity(long definitionCapacity) {
        this.definitionCapacity = definitionCapacity;
    }

    @Override
    public long getUsedSize() {
        return this.usedSize;
    }

    @Override
    public void setUsedSize(long usedSize) {
        this.usedSize = usedSize;
    }

    @Override
    public long getQuotaCapacity() {
        return this.quotaCapacity;
    }

    @Override
    public void setQuotaCapacity(long quotaCapacity) {
        this.quotaCapacity = quotaCapacity;
    }
}
