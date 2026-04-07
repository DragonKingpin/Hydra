package com.pinecone.hydra.task;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.pinecone.hydra.task.marshal.TaskScheduleType;

public interface TaskInstanceMeta extends Pinenut {
    GUID getGuid();

    GUID getTaskGuid();

    String getInstanceName();

    String getTaskName();

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

    TaskScheduleCycle getKernelScheduleCycle ();

    TaskScheduleType getKernelScheduleType ();

    LocalDateTime getLastStartTime ();

    LocalDateTime getLastEndTime ();

    LocalDateTime getCreateTime ();

    LocalDateTime getUpdateTime ();


    LocalDateTime getExpectTime();

    LocalDateTime getFireTime();

    LocalDateTime getStartTime();

    LocalDateTime getFinishTime();

    LocalDateTime getScheduleHostTime();

    LocalDateTime getSubmitTime();

    LocalDateTime getScheduleTime();

    String getProcessorName();

    void setExpectTime(LocalDateTime expectTime);

    void setFireTime(LocalDateTime fireTime);

    void setStartTime(LocalDateTime startTime);

    void setFinishTime(LocalDateTime finishTime);

    void setScheduleHostTime(LocalDateTime scheduleHostTime);

    void setSubmitTime(LocalDateTime submitTime);

    void setScheduleTime(LocalDateTime scheduleTime);

    void setProcessorName(String processorName);
}
