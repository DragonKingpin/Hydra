package com.pinecone.hydra.task;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleCycle;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleType;

import java.time.LocalDateTime;

public abstract class ArchTaskInstance implements TaskInstance {

    protected InstanceEntry             mInstanceEntry;

    public ArchTaskInstance( InstanceEntry instanceEntry ) {
        this.mInstanceEntry = instanceEntry;
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
    public int getKernelScheduleCycleCode() {
        return this.mInstanceEntry.getKernelScheduleCycleCode();
    }

    @Override
    public int getKernelScheduleTypeCode() {
        return this.mInstanceEntry.getKernelScheduleTypeCode();
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
    public boolean isDryRun() {
        return this.mInstanceEntry.isDryRun();
    }

    @Override
    public KernelTaskScheduleCycle getKernelScheduleCycle() {
        return this.mInstanceEntry.getKernelScheduleCycle();
    }

    @Override
    public KernelTaskScheduleType getKernelScheduleType() {
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
}
