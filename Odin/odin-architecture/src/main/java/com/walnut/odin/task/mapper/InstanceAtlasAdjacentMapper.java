package com.walnut.odin.task.mapper;

import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.conduct.entity.InstanceAtlasAdjacent;

@IbatisDataAccessObject
public interface InstanceAtlasAdjacentMapper {

    void insert( InstanceAtlasAdjacent instanceAtlasAdjacent );

}
