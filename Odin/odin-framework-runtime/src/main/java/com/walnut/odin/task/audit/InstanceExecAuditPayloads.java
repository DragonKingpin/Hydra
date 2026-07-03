package com.walnut.odin.task.audit;

import java.time.LocalDateTime;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.json.JSONEncoder;
import com.pinecone.hydra.task.TaskInstanceExecState;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.walnut.odin.conduct.entity.InstanceExec;

public final class InstanceExecAuditPayloads {

    private InstanceExecAuditPayloads() {
    }

    public static String from(
            TaskInstanceExecState state, InstanceEntry entry, LocalDateTime finishTime
    ) {
        return InstanceExecAuditPayloads.from(
                state,
                entry.getTaskGuid(),
                entry.getGuid(),
                entry.getTaskName(),
                entry.getInstanceName(),
                entry.getSequenceCnt(),
                entry.getRetryCnt(),
                entry.getRetryTimes(),
                entry.getImagePath(),
                entry.getAffinityProcessor(),
                entry.getDesignatedProcessor(),
                finishTime
        );
    }

    public static String from( TaskInstanceExecState state, InstanceExec exec ) {
        return InstanceExecAuditPayloads.from(
                state,
                exec.getTaskGuid(),
                exec.getInstanceGuid(),
                exec.getTaskName(),
                exec.getInstanceName(),
                exec.getSequenceCnt(),
                exec.getCurrentRetryNumber(),
                exec.getRetryTimes(),
                exec.getImagePath(),
                exec.getAffinityProcessor(),
                exec.getDesignatedProcessor(),
                exec.getFinishTime()
        );
    }

    public static String from(
            TaskInstanceExecState state,
            Object taskGuid,
            Object instanceGuid,
            String taskName,
            String instanceName,
            int sequenceCnt,
            int currentRetryNumber,
            int retryTimes,
            String imagePath,
            String affinityProcessor,
            String designatedProcessor,
            LocalDateTime finishTime
    ) {
        return JSONEncoder.stringifyMapFormat( new KeyValue[]{
                new KeyValue<>( "execState"          , state.getName() ),
                new KeyValue<>( "taskGuid"           , taskGuid ),
                new KeyValue<>( "instanceGuid"       , instanceGuid ),
                new KeyValue<>( "taskName"           , taskName ),
                new KeyValue<>( "instanceName"       , instanceName ),
                new KeyValue<>( "sequenceCnt"        , sequenceCnt ),
                new KeyValue<>( "currentRetryNumber" , currentRetryNumber ),
                new KeyValue<>( "retryTimes"         , retryTimes ),
                new KeyValue<>( "imagePath"          , imagePath ),
                new KeyValue<>( "affinityProcessor"  , affinityProcessor ),
                new KeyValue<>( "designatedProcessor", designatedProcessor ),
                new KeyValue<>( "finishTime"         , finishTime )
        } );
    }
}
