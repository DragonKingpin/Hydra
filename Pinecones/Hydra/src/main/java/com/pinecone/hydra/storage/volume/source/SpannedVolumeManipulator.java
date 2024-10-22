package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.entity.SpannedVolume;

public interface SpannedVolumeManipulator extends Pinenut {
    void insert( SpannedVolume spannedVolume );
    void remove( GUID guid );
    SpannedVolume getSpannedVolume(GUID guid);
}
