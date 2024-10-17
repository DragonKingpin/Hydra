package com.pinecone.hydra.volume.ibatis;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.entity.MirroredVolume;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

@IbatisDataAccessObject
public interface MirroredVolumeMapper extends Pinenut {
    void insert( MirroredVolume mirroredVolume );
    void remove( GUID guid );
    MirroredVolume getMirroredVolume(GUID guid);
}
