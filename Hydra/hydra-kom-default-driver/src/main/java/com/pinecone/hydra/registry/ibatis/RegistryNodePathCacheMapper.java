package com.pinecone.hydra.registry.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.source.TriePathCacheManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;

import org.apache.ibatis.annotations.Param;

@IbatisDataAccessObject
public interface RegistryNodePathCacheMapper extends TriePathCacheManipulator {
    void insert( @Param("guid") GUID guid, @Param("path") String path );

    void remove( @Param("guid") GUID guid );

    String getPath( @Param("guid") GUID guid );

    GUID getNode( @Param("path") String path );

    GUID queryGUIDByPath( @Param("path") String path );
}
