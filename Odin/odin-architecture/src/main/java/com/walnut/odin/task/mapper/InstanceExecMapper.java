package com.walnut.odin.task.mapper;

import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.conduct.entity.InstanceExec;
import java.time.LocalDateTime;

@IbatisDataAccessObject
public interface InstanceExecMapper {

    void insert( InstanceExec instanceExec );

    InstanceExec queryByInstanceGuid( @Param( "instanceGuid" ) GUID instanceGuid );

    InstanceExec queryByInstanceGuidAndRetry(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "currentRetryNumber" ) int nCurrentRetryNumber
    );

    void updateStateByInstanceGuid( InstanceExec execUpdate );

    void updateStateByInstanceGuidAndRetry( InstanceExec execUpdate );

    void updateStateByInstanceGuidAndRetryFields(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "currentRetryNumber" ) int nCurrentRetryNumber,
            @Param( "execState" ) String szExecState,
            @Param( "startTime" ) LocalDateTime startTime,
            @Param( "runTime" ) LocalDateTime runTime,
            @Param( "finishTime" ) LocalDateTime finishTime
    );

    void updateImagePathByInstanceGuidAndRetry(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "currentRetryNumber" ) int nCurrentRetryNumber,
            @Param( "imagePath" ) String szImagePath
    );
}
