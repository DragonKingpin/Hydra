package com.pinecone.hydra.task;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleCycle;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleType;

public abstract class ArchInstanceMeta implements TaskInstanceMeta {
    protected GUID guid;
    protected GUID taskGuid;
    protected String instanceName;
    protected LocalDateTime businessTime;
    protected short priority;
    protected short actuallyPriority;
    protected TaskInstanceStatus instanceStatus;
    protected String taskType;
    protected int runCount;
    protected boolean dryRun;
    protected KernelTaskScheduleCycle kernelScheduleCycle;
    protected KernelTaskScheduleType kernelScheduleType;
    protected LocalDateTime lastStartTime;
    protected LocalDateTime lastEndTime;
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
    public LocalDateTime getBusinessTime() {
        return this.businessTime;
    }

    @Override
    public short getPriority() {
        return this.priority;
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
    public boolean isDryRun() {
        return this.dryRun;
    }

    @Override
    public KernelTaskScheduleCycle getKernelScheduleCycle() {
        return this.kernelScheduleCycle;
    }

    @Override
    public KernelTaskScheduleType getKernelScheduleType() {
        return this.kernelScheduleType;
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
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

}
