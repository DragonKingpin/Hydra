package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalSupportTrait;

import java.util.List;

public interface VolumePhysicalSupportTraitManipulator extends Pinenut {
    void insert( VolumePhysicalSupportTrait trait );

    void update( VolumePhysicalSupportTrait trait );

    VolumePhysicalSupportTrait get( GUID guid );

    VolumePhysicalSupportTrait getByPhysicalGuid( GUID physicalGuid );

    List<VolumePhysicalSupportTrait> listAll();

    List<VolumePhysicalSupportTrait> listByCode( String code );
}
