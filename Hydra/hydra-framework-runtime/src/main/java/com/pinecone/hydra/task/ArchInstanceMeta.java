package com.pinecone.hydra.task;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.pinecone.hydra.task.marshal.TaskScheduleType;

public abstract class ArchInstanceMeta implements TaskInstanceMeta {
    protected GUID guid;
    protected GUID taskGuid;
    protected String instanceName;
    protected String taskName;
    protected LocalDateTime businessTime;
    protected short priority;
    protected String imagePath;
    protected short actuallyPriority;
    protected TaskInstanceStatus instanceStatus;
    protected String taskType;
    protected int runCount;
    protected int sequenceCnt;
    protected int retryCnt;
    protected String errorCause;
    protected boolean dryRun;
    protected TaskScheduleCycle scheduleCycle;
    protected TaskScheduleType scheduleType;
    protected LocalDateTime lastStartTime;
    protected LocalDateTime lastEndTime;
    protected LocalDateTime expectTime;
    protected LocalDateTime fireTime;
    protected LocalDateTime startTime;
    protected LocalDateTime finishTime;
    protected LocalDateTime scheduleHostTime;
    protected LocalDateTime submitTime;
    protected LocalDateTime scheduleTime;
    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;

    @Override
    public GUID getGuid() {
        return this.guid;
    }

    @Override
    public GUID getTaskGuid() {
        return this.taskGuid;
    }

    @Override
    public String getInstanceName() {
        return this.instanceName;
    }

    @Override
    public String getTaskName() {
        return this.taskName;
    }

    @Override
    public LocalDateTime getBusinessTime() {
        return this.businessTime;
    }

    @Override
    public short getPriority() {
        return this.priority;
    }

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    @Override
    public short getActuallyPriority() {
        return this.actuallyPriority;
    }

    @Override
    public TaskInstanceStatus getInstanceStatus() {
        return this.instanceStatus;
    }



    @Override
    public String getTaskType() {
        return this.taskType;
    }

    @Override
    public int getRunCount() {
        return this.runCount;
    }

    @Override
    public int getSequenceCnt() {
        return this.sequenceCnt;
    }

    @Override
    public int getRetryCnt() {
        return this.retryCnt;
    }

    @Override
    public String getErrorCause() {
        return this.errorCause;
    }

    @Override
    public boolean isDryRun() {
        return this.dryRun;
    }

    @Override
    public TaskScheduleCycle getKernelScheduleCycle() {
        return this.scheduleCycle;
    }

    @Override
    public TaskScheduleType getKernelScheduleType() {
        return this.scheduleType;
    }

    @Override
    public LocalDateTime getLastStartTime() {
        return this.lastStartTime;
    }

    @Override
    public LocalDateTime getLastEndTime() {
        return this.lastEndTime;
    }

    @Override
    public LocalDateTime getExpectTime() {
        return this.expectTime;
    }

    @Override
    public LocalDateTime getFireTime() {
        return this.fireTime;
    }

    @Override
    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    @Override
    public LocalDateTime getFinishTime() {
        return this.finishTime;
    }

    @Override
    public LocalDateTime getScheduleHostTime() {
        return this.scheduleHostTime;
    }

    @Override
    public LocalDateTime getSubmitTime() {
        return this.submitTime;
    }

    @Override
    public LocalDateTime getScheduleTime() {
        return this.scheduleTime;
    }


    @Override
    public void setExpectTime(LocalDateTime expectTime) {
        this.expectTime = expectTime;
    }

    @Override
    public void setFireTime(LocalDateTime fireTime) {
        this.fireTime = fireTime;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public void setScheduleHostTime(LocalDateTime scheduleHostTime) {
        this.scheduleHostTime = scheduleHostTime;
    }

    @Override
    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }

    @Override
    public void setScheduleTime(LocalDateTime scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

}
