package com.walnut.odin.conduct.entity;


import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import java.time.LocalDateTime;

public interface InstanceExec extends Pinenut {
    long getId();
    void setId(long id);

    GUID getTaskGuid();
    void setTaskGuid(GUID taskGuid);

    GUID getInstanceGuid();
    void setInstanceGuid(GUID instanceGuid);

    String getTaskName();
    void setTaskName(String taskName);

    String getInstanceName();
    void setInstanceName(String instanceName);

    String getProcessorQueue();
    void setProcessorQueue(String processorQueue);

    String getClusterName();
    void setClusterName(String clusterName);

    String getExecState();
    void setExecState(String execState);

    int getCurrentRetryNumber();
    void setCurrentRetryNumber(int currentRetryNumber);

    int getRetryTimes();
    void setRetryTimes(int retryTimes);

    LocalDateTime getStartTime();
    void setStartTime(LocalDateTime startTime);

    LocalDateTime getRunTime();
    void setRunTime(LocalDateTime runTime);

    LocalDateTime getFinishTime();
    void setFinishTime(LocalDateTime finishTime);
}