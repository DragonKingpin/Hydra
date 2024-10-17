package com.pinecone.hydra.volume.ibatis;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.entity.MountPoint;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

@IbatisDataAccessObject
public interface MountPointMapper extends Pinenut {
    void insert( MountPoint mountPoint );
    void remove( GUID guid );
    MountPoint getMountPoint(GUID guid);
}
