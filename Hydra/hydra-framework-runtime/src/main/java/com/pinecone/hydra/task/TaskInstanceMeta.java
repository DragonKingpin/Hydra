package com.pinecone.hydra.task;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleCycle;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleType;

public interface TaskInstanceMeta extends Pinenut {
    GUID getGuid();

    GUID getTaskGuid();

    String getInstanceName();

    LocalDateTime getBusinessTime ();

    short getPriority();

    String getImagePath();

    short getActuallyPriority();

    TaskInstanceStatus getInstanceStatus ();

    String getTaskType ();

    int getRunCount ();

    int getSequenceCnt();

    int getRetryCnt();

    String getErrorCause();

    boolean isDryRun() ;

    KernelTaskScheduleCycle getKernelScheduleCycle ();

    KernelTaskScheduleType getKernelScheduleType ();

    LocalDateTime getLastStartTime ();

    LocalDateTime getLastEndTime ();

    LocalDateTime getCreateTime ();

    LocalDateTime getUpdateTime ();
}
