package com.walnut.odin.task.mapper;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Collection;

import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.conduct.entity.InstanceExecAudit;

@IbatisDataAccessObject
public interface InstanceExecAuditMapper {

    int upsert( InstanceExecAudit audit );

    int upsertLogger(
            @Param( "taskGuid" ) GUID taskGuid,
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "sequenceCnt" ) int nSequenceCnt,
            @Param( "currentRetryNumber" ) int nCurrentRetryNumber,
            @Param( "message" ) String szMessage,
            @Param( "payload" ) String szPayload,
            @Param( "startTime" ) LocalDateTime startTime,
            @Param( "finishTime" ) LocalDateTime finishTime
    );

    InstanceExecAudit queryByInstanceGuidAndRetryAndType(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "sequenceCnt" ) int nSequenceCnt,
            @Param( "currentRetryNumber" ) int nCurrentRetryNumber,
            @Param( "auditType" ) String szAuditType
    );

    List<InstanceExecAudit> fetchByInstanceGuid( @Param( "instanceGuid" ) GUID instanceGuid );

    int deleteByTaskGuids( @Param( "taskGuids" ) Collection<GUID> taskGuids );
}
