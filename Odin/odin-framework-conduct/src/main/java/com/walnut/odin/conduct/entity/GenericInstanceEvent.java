package com.walnut.odin.conduct.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import java.time.LocalDateTime;
import java.util.Map;

public class GenericInstanceEvent implements InstanceEvent {
    protected GUID          guid;
    protected GUID          taskGuid;
    protected GUID          instanceGuid;
    protected String        instanceName;
    protected int           retryTimes;
    protected int           sequenceCnt;
    protected int           currentRetryNumber;
    protected String        eventType;
    protected String        state;
    protected String        eventContext;
    protected LocalDateTime execTime;

    public GenericInstanceEvent() {
    }

    public GenericInstanceEvent(Map<String, Object> joEntity) {
        BeanMapDecoder.BasicDecoder.decode(this, joEntity);
    }

    @Override
    public GUID getGuid() {
        return this.guid;
    }

    @Override
    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    @Override
    public GUID getTaskGuid() {
        return this.taskGuid;
    }

    @Override
    public void setTaskGuid(GUID taskGuid) {
        this.taskGuid = taskGuid;
    }

    @Override
    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    @Override
    public void setInstanceGuid(GUID instanceGuid) {
        this.instanceGuid = instanceGuid;
    }

    @Override
    public String getInstanceName() {
        return this.instanceName;
    }

    @Override
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    @Override
    public int getRetryTimes() {
        return this.retryTimes;
    }

    @Override
    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    @Override
    public int getSequenceCnt() {
        return this.sequenceCnt;
    }

    @Override
    public void setSequenceCnt(int sequenceCnt) {
        this.sequenceCnt = sequenceCnt;
    }

    @Override
    public int getCurrentRetryNumber() {
        return this.currentRetryNumber;
    }

    @Override
    public void setCurrentRetryNumber(int currentRetryNumber) {
        this.currentRetryNumber = currentRetryNumber;
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    @Override
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String getState() {
        return this.state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String getEventContext() {
        return this.eventContext;
    }

    @Override
    public void setEventContext(String eventContext) {
        this.eventContext = eventContext;
    }

    @Override
    public LocalDateTime getExecTime() {
        return this.execTime;
    }

    @Override
    public void setExecTime(LocalDateTime execTime) {
        this.execTime = execTime;
    }
}
