package com.walnut.odin.conduct.lifecycle;

import java.time.LocalDateTime;
import java.util.Collection;

import com.pinecone.framework.system.regime.Examiner;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;

public interface TaskInstanceLifecycleExaminer extends Examiner {

    TaskInstanceEventHookRegistry eventHookRegistry();

    void recordInstanceEvent(
            InstanceEntry instance,
            TaskInstanceTransitionReason reason,
            String eventContext
    );

    void recordInstanceEvent(
            InstanceEntry instance,
            TaskInstanceTransitionReason reason,
            String state,
            String eventContext
    );

    TaskInstanceTransitionResult transit( TaskInstanceTransition transition );

    TaskInstanceTransitionResult transit(
            GUID instanceGuid, TaskInstanceStatus fromStatus, TaskInstanceStatus toStatus, TaskInstanceTransitionReason reason
    );

    TaskInstanceTransitionResult transitWithScheduleTime(
            GUID instanceGuid, TaskInstanceStatus fromStatus, TaskInstanceStatus toStatus,
            TaskInstanceTransitionReason reason, LocalDateTime scheduleTime
    );

    TaskInstanceTransitionResult transitAnyWithScheduleTime(
            GUID instanceGuid,
            Collection<TaskInstanceStatus> fromStatuses,
            TaskInstanceStatus toStatus,
            TaskInstanceTransitionReason reason,
            LocalDateTime scheduleTime
    );

    TaskInstanceTransitionResult transitAny(
            GUID instanceGuid, Collection<TaskInstanceStatus> fromStatuses, TaskInstanceStatus toStatus, TaskInstanceTransitionReason reason
    );

    TaskInstanceTransitionResult transitAnyWithRuntimeFields(
            GUID instanceGuid,
            Collection<TaskInstanceStatus> fromStatuses,
            TaskInstanceStatus toStatus,
            TaskInstanceTransitionReason reason,
            LocalDateTime latestStartTime,
            LocalDateTime latestEndTime,
            LocalDateTime finishTime,
            String errorCause
    );

    TaskInstanceTransitionResult transitCurrentRetryWithRuntimeFields(
            GUID instanceGuid,
            int sequenceCnt,
            int retryCnt,
            Collection<TaskInstanceStatus> fromStatuses,
            TaskInstanceStatus toStatus,
            TaskInstanceTransitionReason reason,
            LocalDateTime latestStartTime,
            LocalDateTime latestEndTime,
            LocalDateTime finishTime,
            String errorCause
    );

    TaskInstanceTransitionResult transitCurrentRetryWithRuntimeFields(
            GUID instanceGuid,
            int sequenceCnt,
            int retryCnt,
            Collection<TaskInstanceStatus> fromStatuses,
            TaskInstanceStatus toStatus,
            TaskInstanceTransitionReason reason,
            LocalDateTime latestStartTime,
            LocalDateTime latestEndTime,
            LocalDateTime finishTime,
            String errorCause,
            String eventContext
    );

    TaskInstanceTransitionResult transitCurrentRetryWithScheduleAndRuntimeFields(
            GUID instanceGuid,
            int sequenceCnt,
            int retryCnt,
            Collection<TaskInstanceStatus> fromStatuses,
            TaskInstanceStatus toStatus,
            TaskInstanceTransitionReason reason,
            LocalDateTime scheduleTime,
            LocalDateTime latestStartTime,
            LocalDateTime latestEndTime,
            LocalDateTime finishTime,
            String errorCause,
            String eventContext
    );
}
