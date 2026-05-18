package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumePhysical;

import java.util.List;

public interface VolumePhysicalManipulator extends Pinenut {
    void insert( VolumePhysical physical );

    void update( VolumePhysical physical );

    VolumePhysical get( GUID guid );

    List<VolumePhysical> listAll();

    void remove( GUID guid );
}

