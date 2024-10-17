package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.entity.StripedVolume;

public interface StripedVolumeManipulator extends Pinenut {
    void insert( StripedVolume stripedVolume );
    void remove( GUID guid );
    StripedVolume getStripedVolume(GUID guid);
}
