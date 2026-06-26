package com.walnut.odin.task.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.conduct.entity.TaskInstanceOperationLog;

@IbatisDataAccessObject
public interface TaskInstanceOperationLogMapper extends Pinenut {

    int insert( TaskInstanceOperationLog log );

    List<TaskInstanceOperationLog> fetchByInstanceGuid( @Param( "instanceGuid" ) GUID instanceGuid );

    int deleteByTaskGuids( @Param( "taskGuids" ) Collection<GUID> taskGuids );
}
