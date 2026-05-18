package com.pinecone.hydra.business.ibatis;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.ElementNode;
import com.pinecone.hydra.business.entity.GenericElementNode;
import com.pinecone.hydra.business.source.NodeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface NodeMapper extends NodeManipulator {

    @Override
    void insert( ElementNode node );

    @Override
    void remove( @Param( "guid" ) GUID guid );

    @Override
    GenericElementNode get( @Param( "guid" ) GUID guid );

    @Override
    void update( ElementNode node );

    @Override
    List<GenericElementNode > fetchByType( @Param( "type" ) String szType );

    @Override
    List<GUID > getGuidsByName( @Param( "name" ) String szName );

    @Override
    List<GUID > queryGuidsByCode( @Param( "code" ) String szCode );
}
