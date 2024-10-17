package com.pinecone.hydra.volume.ibatis;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.entity.StripedVolume;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

@IbatisDataAccessObject
public interface StripedVolumeMapper extends Pinenut {
    void insert( StripedVolume stripedVolume );
    void remove( GUID guid );
    StripedVolume getStripedVolume(GUID guid);
}
