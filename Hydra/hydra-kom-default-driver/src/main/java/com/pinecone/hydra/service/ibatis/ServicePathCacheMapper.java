package com.pinecone.hydra.service.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.source.TriePathCacheManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
@IbatisDataAccessObject
public interface ServicePathCacheMapper extends TriePathCacheManipulator {
    void insert(@Param("guid") GUID guid, @Param("path") String path );

    void insertLongPath( @Param("guid") GUID guid, @Param("path") String path, @Param("longPath") String longPath );

    void remove( @Param("guid") GUID guid );


    default String getPath( GUID guid ){
        String longPath = this.getLongPath(guid);
        if ( longPath != null ){
            return this.getPath0( guid )+this.getLongPath( guid );
        }
        return this.getPath0( guid );
    };
    String getLongPath( @Param("guid") GUID guid );

    String getPath0( @Param("guid") GUID guid );

    GUID getNode( @Param("path") String path );

    GUID queryGUIDByPath( @Param("path") String path );
}
