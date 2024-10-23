package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.entity.PhysicalVolume;
import com.pinecone.hydra.storage.volume.entity.SimpleVolume;

import java.util.List;

public interface SimpleVolumeManipulator extends LogicVolumeManipulator {
    void insert( SimpleVolume simpleVolume );
    void remove( GUID guid );
    SimpleVolume getSimpleVolume(GUID guid);
    void extendLogicalVolume( GUID logicGuid, GUID physicalGuid );
    List<GUID> lsblk(GUID logicGuid );
}
