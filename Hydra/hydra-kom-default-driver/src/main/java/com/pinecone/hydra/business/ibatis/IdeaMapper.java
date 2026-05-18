package com.pinecone.hydra.business.ibatis;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.GenericIdeaElement;
import com.pinecone.hydra.business.entity.IdeaElement;
import com.pinecone.hydra.business.source.IdeaManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface IdeaMapper extends IdeaManipulator {

    @Override
    void insert( IdeaElement ideaElement );

    @Override
    void remove( @Param( "guid" ) GUID guid );

    @Override
    GenericIdeaElement get( @Param( "guid" ) GUID guid );

    @Override
    void update( IdeaElement ideaElement );

    @Override
    List<GenericIdeaElement > queryByIdeaCode( @Param( "ideaCode" ) String szIdeaCode );

    @Override
    List<GenericIdeaElement > queryByRedMineGuid( @Param( "redMineGuid" ) GUID redMineGuid );
}
