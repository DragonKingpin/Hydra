package com.pinecone.hydra.volume.ibatis;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.entity.SpannedVolume;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

@IbatisDataAccessObject
public interface SpannedVolumeMapper extends Pinenut {
    void insert( SpannedVolume spannedVolume );
    void remove( GUID guid );
    SpannedVolume getSpannedVolume(GUID guid);
}
