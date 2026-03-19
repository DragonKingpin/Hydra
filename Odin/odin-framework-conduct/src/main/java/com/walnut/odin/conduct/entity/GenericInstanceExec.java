package com.walnut.odin.conduct.entity;



import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import java.time.LocalDateTime;
import java.util.Map;

public class GenericInstanceExec implements InstanceExec {
    protected GUID          taskGuid;
    protected GUID          instanceGuid;
    protected String        taskName;
    protected String        instanceName;
    protected String        processorQueue;
    protected String        clusterName;
    protected String        execState;
    protected int           currentRetryNumber;
    protected int           retryTimes;
    protected LocalDateTime startTime;
    protected LocalDateTime runTime;
    protected LocalDateTime finishTime;

    public GenericInstanceExec() {
    }

    public GenericInstanceExec(Map<String, Object> joEntity) {
        BeanMapDecoder.BasicDecoder.decode(this, joEntity);
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
    public String getTaskName() {
        return this.taskName;
    }

    @Override
    public void setTaskName(String taskName) {
        this.taskName = taskName;
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
    public String getProcessorQueue() {
        return this.processorQueue;
    }

    @Override
    public void setProcessorQueue(String processorQueue) {
        this.processorQueue = processorQueue;
    }

    @Override
    public String getClusterName() {
        return this.clusterName;
    }

    @Override
    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    @Override
    public String getExecState() {
        return this.execState;
    }

    @Override
    public void setExecState(String execState) {
        this.execState = execState;
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
    public int getRetryTimes() {
        return this.retryTimes;
    }

    @Override
    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    @Override
    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public LocalDateTime getRunTime() {
        return this.runTime;
    }

    @Override
    public void setRunTime(LocalDateTime runTime) {
        this.runTime = runTime;
    }

    @Override
    public LocalDateTime getFinishTime() {
        return this.finishTime;
    }

    @Override
    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }
}