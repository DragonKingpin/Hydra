package com.walnut.odin.conduct.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface InstanceEvent extends Pinenut {
    GUID getGuid();
    void setGuid(GUID guid);

    GUID getTaskGuid();
    void setTaskGuid(GUID taskGuid);

    GUID getInstanceGuid();
    void setInstanceGuid(GUID instanceGuid);

    String getInstanceName();
    void setInstanceName(String instanceName);

    int getRetryTimes();
    void setRetryTimes(int retryTimes);

    int getCurrentRetryNumber();
    void setCurrentRetryNumber(int currentRetryNumber);

    String getEventType();
    void setEventType(String eventType);

    String getState();
    void setState(String state);

    String getEventContext();
    void setEventContext(String eventContext);

    LocalDateTime getExecTime();
    void setExecTime(LocalDateTime execTime);
}