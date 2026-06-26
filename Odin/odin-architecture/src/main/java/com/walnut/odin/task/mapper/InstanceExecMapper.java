package com.walnut.odin.task.mapper;

import org.apache.ibatis.annotations.Param;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.meta.TableIndexMeta;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import com.walnut.odin.conduct.entity.InstanceExec;
import com.walnut.odin.patrol.RunningExecPatrolEntry;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@IbatisDataAccessObject
public interface InstanceExecMapper {

    void insert( InstanceExec instanceExec );

    InstanceExec queryByInstanceGuid( @Param( "instanceGuid" ) GUID instanceGuid );

    List<InstanceExec> fetchByInstanceGuid( @Param( "instanceGuid" ) GUID instanceGuid );

    List<InstanceExec> fetchByTaskGuids( @Param( "taskGuids" ) Collection<GUID> taskGuids );

    List<InstanceExec> fetchActiveByTaskGuids(
            @Param( "taskGuids" ) Collection<GUID> taskGuids,
            @Param( "execStates" ) Collection<String> execStates
    );

    InstanceExec queryByInstanceGuidAndRetry(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "sequenceCnt" ) int nSequenceCnt,
            @Param( "currentRetryNumber" ) int nCurrentRetryNumber
    );

    List<InstanceExec> fetchTerminalExecsMissingLoggerAudit( @Param( "limit" ) int nLimit );

    TableIndexMeta selectRunningExecIdRange();

    List<RunningExecPatrolEntry> fetchRunningExecPatrolEntries(
            @Param( "idMin" ) long nIdMin,
            @Param( "idMax" ) long nIdMax
    );

    void updateStateByInstanceGuid( InstanceExec execUpdate );

    void updateStateByInstanceGuidAndRetry( InstanceExec execUpdate );

    void updateStateByInstanceGuidAndRetryFields(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "sequenceCnt" ) int nSequenceCnt,
            @Param( "currentRetryNumber" ) int nCurrentRetryNumber,
            @Param( "execState" ) String szExecState,
            @Param( "startTime" ) LocalDateTime startTime,
            @Param( "runTime" ) LocalDateTime runTime,
            @Param( "finishTime" ) LocalDateTime finishTime
    );

    int updateStateRetryMonotonic(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "sequenceCnt" ) int nSequenceCnt,
            @Param( "currentRetryNumber" ) int nCurrentRetryNumber,
            @Param( "execState" ) String szExecState,
            @Param( "startTime" ) LocalDateTime startTime,
            @Param( "runTime" ) LocalDateTime runTime,
            @Param( "finishTime" ) LocalDateTime finishTime
    );

    void updateImagePathByInstanceGuidAndRetry(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "sequenceCnt" ) int nSequenceCnt,
            @Param( "currentRetryNumber" ) int nCurrentRetryNumber,
            @Param( "imagePath" ) String szImagePath
    );

    void updateExecutedProcessorByInstanceGuidAndRetry(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "sequenceCnt" ) int nSequenceCnt,
            @Param( "currentRetryNumber" ) int nCurrentRetryNumber,
            @Param( "executedProcessor" ) String szExecutedProcessor
    );

    void updateProcessGuidByInstanceGuidAndRetry(
            @Param( "instanceGuid" ) GUID instanceGuid,
            @Param( "sequenceCnt" ) int nSequenceCnt,
            @Param( "currentRetryNumber" ) int nCurrentRetryNumber,
            @Param( "processGuid" ) GUID processGuid
    );

    int deleteByTaskGuids( @Param( "taskGuids" ) Collection<GUID> taskGuids );
}
