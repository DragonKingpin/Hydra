package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumePhysical;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalStatus;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalType;

import java.util.List;

public interface VolumePhysicalManipulator extends Pinenut {
    void insert( VolumePhysical physical );

    void update( VolumePhysical physical );

    VolumePhysical get( GUID guid );

    List<VolumePhysical> listAll();

    long count( String name, VolumePhysicalType physicalType, VolumePhysicalStatus status, GUID deviceGuid );

    List<VolumePhysical> listPage(
            String name,
            VolumePhysicalType physicalType,
            VolumePhysicalStatus status,
            GUID deviceGuid,
            int offset,
            int limit
    );

    void remove( GUID guid );
}

