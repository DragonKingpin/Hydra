package com.pinecone.hydra.storage.volume.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public interface VolumeCapacity extends Pinenut {
    long getDefinitionCapacity();
    void setDefinitionCapacity( long definitionCapacity );
    long getUsedSize();
    void setUsedSize( long usedSize );
    long getQuotaCapacity();
    void setQuotaCapacity( long quotaCapacity );
}
