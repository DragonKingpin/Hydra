package com.walnut.odin.task.mapper;

import com.walnut.odin.conduct.entity.InstanceAtlasAdjacent;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InstanceAtlasAdjacentMapper {

    void insert( InstanceAtlasAdjacent instanceAtlasAdjacent );

}
