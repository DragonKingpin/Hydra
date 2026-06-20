package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumeFreeIntent;
import com.pinecone.hydra.storage.volume.core.VolumeFreeIntentStatus;

import java.util.List;

public interface VolumeFreeIntentManipulator extends Pinenut {
    void insert( VolumeFreeIntent intent );

    void updateStatus( GUID guid, VolumeFreeIntentStatus status, String message );

    VolumeFreeIntent get( GUID guid );

    VolumeFreeIntent getBySourceLocationGuid( GUID sourceLocationGuid );

    long countAll();

    List<VolumeFreeIntent> listPage( int offset, int limit );

    List<VolumeFreeIntent> listByVolumeGuid( GUID volumeGuid );

    List<VolumeFreeIntent> listByVolumeGuidAndStatus( GUID volumeGuid, VolumeFreeIntentStatus status );
}
