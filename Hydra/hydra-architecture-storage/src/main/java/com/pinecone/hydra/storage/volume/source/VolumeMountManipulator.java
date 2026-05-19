package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumeMount;

import java.util.List;

public interface VolumeMountManipulator extends Pinenut {
    void insert( VolumeMount mount );

    void update( VolumeMount mount );

    VolumeMount get( GUID guid );

    VolumeMount getByPath( String mountPath );

    List<VolumeMount> listByVolumeGuid( GUID volumeGuid );

    List<VolumeMount> listPage( String keyword, String status, int offset, int limit );

    long count( String keyword, String status );

    void remove( GUID guid );
}

