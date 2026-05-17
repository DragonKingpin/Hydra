package com.pinecone.hydra.business.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.GenericNodeCachePath;
import com.pinecone.hydra.business.source.PathManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface PathMapper extends PathManipulator {

    @Override
    void insert( @Param( "guid" ) GUID guid, @Param( "path" ) String szPath );

    @Override
    void insertLongPath( @Param( "guid" ) GUID guid, @Param( "path" ) String szPath, @Param( "longPath" ) String szLongPath );

    @Override
    void remove( @Param( "guid" ) GUID guid );

    @Override
    GenericNodeCachePath getPath0( @Param( "guid" ) GUID guid );

    @Override
    GUID queryGuidByPath( @Param( "path" ) String szPath );
}
