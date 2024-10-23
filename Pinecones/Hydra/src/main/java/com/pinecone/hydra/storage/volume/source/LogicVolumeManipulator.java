package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.entity.PhysicalVolume;

import java.util.List;

public interface LogicVolumeManipulator extends Pinenut {
    void extendLogicalVolume( GUID logicGuid, GUID physicalGuid );
    List< GUID > lsblk( GUID logicGuid );
}
