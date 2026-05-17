package com.walnut.odin.conduct.lifecycle;

import java.time.LocalDateTime;
import java.util.Collection;

import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.TaskInstanceStatus;

public interface TaskInstanceLifecycleInstrument extends Instrument {

    TaskInstanceTransitionResult transit( TaskInstanceTransition transition );

    TaskInstanceTransitionResult transit(
            GUID instanceGuid, TaskInstanceStatus fromStatus, TaskInstanceStatus toStatus, TaskInstanceTransitionReason reason
    );

    TaskInstanceTransitionResult transitWithScheduleTime(
            GUID instanceGuid, TaskInstanceStatus fromStatus, TaskInstanceStatus toStatus,
            TaskInstanceTransitionReason reason, LocalDateTime scheduleTime
    );

    TaskInstanceTransitionResult transitAny(
            GUID instanceGuid, Collection<TaskInstanceStatus> fromStatuses, TaskInstanceStatus toStatus, TaskInstanceTransitionReason reason
    );
}
