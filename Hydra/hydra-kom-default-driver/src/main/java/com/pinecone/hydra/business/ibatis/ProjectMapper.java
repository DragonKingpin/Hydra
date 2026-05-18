package com.pinecone.hydra.business.ibatis;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.GenericProjectElement;
import com.pinecone.hydra.business.entity.ProjectElement;
import com.pinecone.hydra.business.source.ProjectManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface ProjectMapper extends ProjectManipulator {

    @Override
    void insert( ProjectElement projectElement );

    @Override
    void remove( @Param( "guid" ) GUID guid );

    @Override
    GenericProjectElement get( @Param( "guid" ) GUID guid );

    @Override
    void update( ProjectElement projectElement );

    @Override
    List<GenericProjectElement > queryByProjectCode( @Param( "projectCode" ) String szProjectCode );
}
