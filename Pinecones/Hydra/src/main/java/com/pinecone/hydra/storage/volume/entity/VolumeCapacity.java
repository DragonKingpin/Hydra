package com.pinecone.hydra.storage.volume.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface VolumeCapacity extends Pinenut {
    GUID getVolumeGuid();
    void setVolumeGuid( GUID volumeGuid );
    long getDefinitionCapacity();
    void setDefinitionCapacity( long definitionCapacity );
    long getUsedSize();
    void setUsedSize( long usedSize );
    long getQuotaCapacity();
    void setQuotaCapacity( long quotaCapacity );
}
