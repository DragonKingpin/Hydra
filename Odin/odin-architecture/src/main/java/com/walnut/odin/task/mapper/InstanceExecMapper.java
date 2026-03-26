package com.walnut.odin.task.mapper;

import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.conduct.entity.InstanceExec;

@IbatisDataAccessObject
public interface InstanceExecMapper {

    void insert( InstanceExec instanceExec );

    void updateStateByInstanceGuid( InstanceExec execUpdate );
}
