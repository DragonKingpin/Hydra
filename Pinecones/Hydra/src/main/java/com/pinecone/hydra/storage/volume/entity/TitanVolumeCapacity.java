package com.pinecone.hydra.storage.volume.entity;

import com.pinecone.hydra.storage.volume.entity.VolumeCapacity;

public class TitanVolumeCapacity implements VolumeCapacity {
    private long definitionCapacity;
    private long usedSize;
    private long quotaCapacity;

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
