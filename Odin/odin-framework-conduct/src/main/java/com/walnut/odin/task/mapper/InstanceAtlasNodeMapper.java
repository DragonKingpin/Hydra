package com.walnut.odin.task.mapper;

import com.walnut.odin.conduct.entity.InstanceAtlasNode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InstanceAtlasNodeMapper {

    void insert( InstanceAtlasNode instanceAtlasNode);
}
