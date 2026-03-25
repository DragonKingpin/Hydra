package com.walnut.odin.task.mapper;

import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.conduct.entity.InstanceEvent;

@IbatisDataAccessObject
public interface InstanceEventMapper {

    void insert( InstanceEvent instanceEvent );
}
