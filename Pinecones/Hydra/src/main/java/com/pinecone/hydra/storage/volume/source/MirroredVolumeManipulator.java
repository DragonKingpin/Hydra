package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.entity.MirroredVolume;
import com.pinecone.hydra.storage.volume.entity.PhysicalVolume;

import java.util.List;

public interface MirroredVolumeManipulator extends LogicVolumeManipulator {
    void insert( MirroredVolume mirroredVolume );
    void remove( GUID guid );
    MirroredVolume getMirroredVolume(GUID guid);
    void extendLogicalVolume( GUID logicGuid, GUID physicalGuid );
    List<GUID> lsblk(GUID logicGuid );
}
