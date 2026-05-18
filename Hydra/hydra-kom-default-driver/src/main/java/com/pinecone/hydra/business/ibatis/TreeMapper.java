package com.pinecone.hydra.business.ibatis;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.GenericNodeTree;
import com.pinecone.hydra.business.entity.NodeTree;
import com.pinecone.hydra.business.source.TreeManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface TreeMapper extends TreeManipulator {

    @Override
    void insert( NodeTree nodeTree );

    @Override
    void insertOwned( @Param( "guid" ) GUID guid, @Param( "parentGuid" ) GUID parentGuid );

    @Override
    void remove( @Param( "guid" ) GUID guid );

    @Override
    GenericNodeTree get( @Param( "guid" ) GUID guid );

    @Override
    GUID fetchParentGuid( @Param( "guid" ) GUID guid );

    @Override
    List<GUID > fetchChildrenGuids( @Param( "parentGuid" ) GUID parentGuid );

    @Override
    List<GUID > fetchRootGuids();

    @Override
    void updateParentGuid( @Param( "targetGuid" ) GUID targetGuid, @Param( "parentGuid" ) GUID parentGuid );

    @Override
    void removeTreeNodeByParentGuid( @Param( "parentGuid" ) GUID parentGuid );

    @Override
    long countNode( @Param( "guid" ) GUID guid, @Param( "parentGuid" ) GUID parentGuid );
}
