package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumeEvent;

import java.util.List;

public interface VolumeEventManipulator extends Pinenut {
    void insert( VolumeEvent event );

    VolumeEvent get( GUID guid );

    List<VolumeEvent> listByVolumeGuid( GUID volumeGuid );

    List<VolumeEvent> listByPhysicalGuid( GUID physicalGuid );
}

