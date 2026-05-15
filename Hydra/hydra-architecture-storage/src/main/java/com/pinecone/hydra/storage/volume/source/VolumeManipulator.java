package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumeRecord;

import java.util.List;

public interface VolumeManipulator extends Pinenut {
    void insert( VolumeRecord volumeRecord );

    void update( VolumeRecord volumeRecord );

    VolumeRecord get( GUID guid );

    List<VolumeRecord> listAll();

    void remove( GUID guid );
}

