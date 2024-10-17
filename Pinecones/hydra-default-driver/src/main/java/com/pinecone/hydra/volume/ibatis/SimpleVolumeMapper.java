package com.pinecone.hydra.volume.ibatis;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.entity.SimpleVolume;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

@IbatisDataAccessObject
public interface SimpleVolumeMapper extends Pinenut {
    void insert( SimpleVolume simpleVolume );
    void remove( GUID guid );
    SimpleVolume getSimpleVolume(GUID guid);
}
