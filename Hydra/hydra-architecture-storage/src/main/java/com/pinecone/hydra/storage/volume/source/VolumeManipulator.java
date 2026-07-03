package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumeMappingMode;
import com.pinecone.hydra.storage.volume.core.VolumeRecord;
import com.pinecone.hydra.storage.volume.core.VolumeStatus;
import com.pinecone.hydra.storage.volume.core.VolumeType;

import java.util.List;

public interface VolumeManipulator extends Pinenut {
    void insert( VolumeRecord volumeRecord );

    void update( VolumeRecord volumeRecord );

    VolumeRecord get( GUID guid );

    List<VolumeRecord> listAll();

    long count( String name, VolumeType volumeType, VolumeMappingMode mappingMode, VolumeStatus status );

    List<VolumeRecord> listPage(
            String name,
            VolumeType volumeType,
            VolumeMappingMode mappingMode,
            VolumeStatus status,
            int offset,
            int limit
    );

    void remove( GUID guid );
}

