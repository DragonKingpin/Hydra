package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.entity.MirroredVolume;

public interface MirroredVolumeManipulator extends Pinenut {
    void insert( MirroredVolume mirroredVolume );
    void remove( GUID guid );
    MirroredVolume getMirroredVolume(GUID guid);
}
