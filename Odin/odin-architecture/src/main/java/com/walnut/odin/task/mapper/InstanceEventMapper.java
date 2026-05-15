package com.walnut.odin.task.mapper;

import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.conduct.entity.InstanceEvent;

@IbatisDataAccessObject
public interface InstanceEventMapper {

    void insert( InstanceEvent instanceEvent );

    InstanceEvent queryByInstanceGuidAndState(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "state" ) String state
    );
}
