package com.pinecone.hydra.task;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.marshal.TaskScheduleCycle;
import com.pinecone.hydra.task.marshal.TaskScheduleType;

import java.time.LocalDateTime;

public abstract class ArchTaskInstance implements TaskInstance {

    protected InstanceEntry             mInstanceEntry;

    protected Task                      mOwnedTask;

    public ArchTaskInstance( InstanceEntry instanceEntry, Task ownedTask ) {
        this.mInstanceEntry = instanceEntry;
        this.mOwnedTask     = ownedTask;
    }

    @Override
    public Task getOwnedTask() {
        return this.mOwnedTask;
    }

    @Override
    public TaskInstrument getTaskInstrument() {
        return this.mInstanceEntry.getTaskInstrument();
    }

    @Override
    public String getRunStatus() {
        return this.mInstanceEntry.getRunStatus();
    }

    @Override
    public GUID getGuid() {
        return this.mInstanceEntry.getGuid();
    }

    @Override
    public GUID getTaskGuid() {
        return this.mInstanceEntry.getTaskGuid();
    }

    @Override
    public String getInstanceName() {
        return this.mInstanceEntry.getInstanceName();
    }

    @Override
    public LocalDateTime getBusinessTime() {
        return this.mInstanceEntry.getBusinessTime();
    }

    @Override
    public short getPriority() {
        return this.mInstanceEntry.getPriority();
    }

    @Override
    public String getImagePath() {
        return this.mInstanceEntry.getImagePath();
    }

    @Override
    public String getExecArch() {
        return this.mInstanceEntry.getExecArch();
    }

    @Override
    public short getActuallyPriority() {
        return this.mInstanceEntry.getActuallyPriority();
    }

    @Override
    public TaskInstanceStatus getInstanceStatus() {
        return this.mInstanceEntry.getInstanceStatus();
    }

    @Override
    public String getTaskType() {
        return this.mInstanceEntry.getTaskType();
    }

    @Override
    public int getRunCount() {
        return this.mInstanceEntry.getRunCount();
    }

    @Override
    public int getRetryCnt() {
        return this.mInstanceEntry.getRetryCnt();
    }

    @Override
    public int getSequenceCnt() {
        return this.mInstanceEntry.getSequenceCnt();
    }

    @Override
    public String getErrorCause() {
        return this.mInstanceEntry.getErrorCause();
    }

    @Override
    public boolean isDryRun() {
        return this.mInstanceEntry.isDryRun();
    }

    @Override
    public TaskScheduleCycle getKernelScheduleCycle() {
        return this.mInstanceEntry.getKernelScheduleCycle();
    }

    @Override
    public TaskScheduleType getKernelScheduleType() {
        return this.mInstanceEntry.getKernelScheduleType();
    }

    @Override
    public LocalDateTime getLastStartTime() {
        return this.mInstanceEntry.getLastStartTime();
    }

    @Override
    public LocalDateTime getLastEndTime() {
        return this.mInstanceEntry.getLastEndTime();
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.mInstanceEntry.getCreateTime();
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.mInstanceEntry.getUpdateTime();
    }

    @Override
    public InstanceEntry getInstanceEntry() {
        return this.mInstanceEntry;
    }

    @Override
    public LocalDateTime getExpectTime() {
        return this.mInstanceEntry.getExpectTime();
    }

    @Override
    public LocalDateTime getFireTime() {
        return this.mInstanceEntry.getFireTime();
    }

    @Override
    public LocalDateTime getStartTime() {
        return this.mInstanceEntry.getStartTime();
    }

    @Override
    public LocalDateTime getFinishTime() {
        return this.mInstanceEntry.getFinishTime();
    }

    @Override
    public LocalDateTime getScheduleHostTime() {
        return this.mInstanceEntry.getScheduleHostTime();
    }

    @Override
    public LocalDateTime getSubmitTime() {
        return this.mInstanceEntry.getSubmitTime();
    }

    @Override
    public LocalDateTime getScheduleTime() {
        return this.mInstanceEntry.getScheduleTime();
    }

    @Override
    public String getAffinityProcessor() {
        return this.mInstanceEntry.getAffinityProcessor();
    }

}
