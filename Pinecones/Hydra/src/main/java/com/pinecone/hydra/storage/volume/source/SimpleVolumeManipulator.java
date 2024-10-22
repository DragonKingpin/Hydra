package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.entity.SimpleVolume;

import java.util.List;

public interface SimpleVolumeManipulator extends Pinenut {
    void insert( SimpleVolume simpleVolume );
    void remove( GUID guid );
    SimpleVolume getSimpleVolume(GUID guid);

}
