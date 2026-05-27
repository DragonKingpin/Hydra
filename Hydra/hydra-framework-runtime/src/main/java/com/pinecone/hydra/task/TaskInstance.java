package com.pinecone.hydra.task;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.pinecone.hydra.task.marshal.TaskScheduleType;

public interface TaskInstance extends Pinenut {

    default Identification getId() {
        return this.getGuid();
    }

    Object getProcessObject();

    Task getOwnedTask();

    TaskInstrument getTaskInstrument();

    String getRunStatus ();

    GUID getGuid();

    GUID getTaskGuid();

    String getInstanceName();

    LocalDateTime getBusinessTime ();

    short getPriority();

    String getImagePath();

    String getExecArch();

    short getActuallyPriority();

    TaskInstanceStatus getInstanceStatus ();

    String getTaskType ();

    int getRunCount ();

    int getSequenceCnt();

    int getRetryCnt();

    boolean isDryRun() ;

    String getErrorCause();

    TaskScheduleCycle getKernelScheduleCycle ();

    TaskScheduleType getKernelScheduleType ();

    LocalDateTime getLastStartTime ();

    LocalDateTime getLastEndTime ();

    LocalDateTime getCreateTime ();

    LocalDateTime getUpdateTime ();

    InstanceEntry getInstanceEntry();

    LocalDateTime getExpectTime();

    LocalDateTime getFireTime();

    LocalDateTime getStartTime();

    LocalDateTime getFinishTime();

    LocalDateTime getScheduleHostTime();

    LocalDateTime getSubmitTime();

    LocalDateTime getScheduleTime();

    String getProcessorName();

}
