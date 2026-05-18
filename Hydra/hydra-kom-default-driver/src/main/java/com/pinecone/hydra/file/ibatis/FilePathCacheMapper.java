package com.pinecone.hydra.file.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.source.TriePathCacheManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
@IbatisDataAccessObject
public interface FilePathCacheMapper extends TriePathCacheManipulator {
    void insert(@Param("guid") GUID guid, @Param("path") String path );

    void insertLongPath( @Param("guid") GUID guid, @Param("path") String path, @Param("longPath") String longPath );

    void remove( GUID guid );


    default String getPath( GUID guid ){
        String longPath = this.getLongPath(guid);
        if( longPath != null ){
            return this.getPath0( guid )+longPath;
        }
        return this.getPath0( guid );
    };
    String getLongPath( GUID guid );
    String getPath0( GUID guid );
    GUID getNode( String path );

    GUID queryGUIDByPath( String path );

}
