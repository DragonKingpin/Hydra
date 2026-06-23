package com.walnut.odin.task.mapper;

import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.conduct.entity.InstanceLineageAdjacent;

@IbatisDataAccessObject
public interface InstanceLineageAdjacentMapper {

    void insert( InstanceLineageAdjacent instanceLineageAdjacent );

    void deleteByGuid( @Param( "guid" ) GUID guid );

    long countByGuidAndParentGuid( @Param( "guid" ) GUID guid, @Param( "parentGuid" ) GUID parentGuid );

}
