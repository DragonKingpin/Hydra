package com.pinecone.hydra.device.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.entity.CachePath;
import com.pinecone.hydra.unit.imperium.entity.GenericCachePath;
import com.pinecone.hydra.unit.imperium.source.TriePathCacheManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Param;

@IbatisDataAccessObject
public interface DeviceNodePathCacheMapper extends TriePathCacheManipulator {
    @Override
    void insert( @Param("guid") GUID guid, @Param("path") String path );

    @Override
    void insertLongPath( @Param("guid") GUID guid, @Param("path") String path, @Param("longPath") String longPath );

    @Override
    void remove( @Param("guid") GUID guid );


    default String getPath( GUID guid ) {
        CachePath cachePath = this.getPath0( guid );
        if ( cachePath == null ) {
            return null;
        }

        return cachePath.getResolvedPath();
    }

    GenericCachePath getPath0( @Param("guid") GUID guid );

    GUID getNode( @Param("path") String path );

    GUID queryGUIDByPath( @Param("path") String path );
}
