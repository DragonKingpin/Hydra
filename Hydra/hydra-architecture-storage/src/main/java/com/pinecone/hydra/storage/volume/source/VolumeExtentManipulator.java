package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumeExtent;

import java.util.List;

public interface VolumeExtentManipulator extends Pinenut {
    void insert( VolumeExtent extent );

    void update( VolumeExtent extent );

    VolumeExtent get( GUID guid );

    List<VolumeExtent> listByParentGuid( GUID parentGuid );

    List<VolumeExtent> listByPhysicalGuid( GUID physicalGuid );

    long countByPhysicalGuid( GUID physicalGuid );

    long countByChildGuid( GUID childGuid );

    void remove( GUID guid );

    void removeByParentGuid( GUID parentGuid );
}

