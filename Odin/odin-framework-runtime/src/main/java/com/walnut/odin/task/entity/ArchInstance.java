package com.walnut.odin.task.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleCycle;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleType;
import com.walnut.odin.ups.Instance;

import java.time.LocalDateTime;
import java.util.Map;

public class ArchInstance implements Instance {
    protected Identification            mInstanceId;

    protected InstanceEntry             mInstanceEntry;

    protected Map<String, Object>       mMetaDataScope;

    public ArchInstance( Identification instanceId, InstanceEntry instanceEntry, Map<String, Object> metaDataScope ) {
        this.mInstanceId = instanceId;
        this.mInstanceEntry = instanceEntry;
        this.mMetaDataScope = metaDataScope;
    }

    public ArchInstance( Identification instanceId, InstanceEntry instanceEntry ) {
        this( instanceId, instanceEntry, null );
    }

    @Override
    public void setGuid(GUID guid) {
        this.mInstanceEntry.setGuid( guid );
    }

    @Override
    public void setAffiliatedTaskGuid(GUID affiliatedTaskGuid) {
        this.mInstanceEntry.setAffiliatedTaskGuid( affiliatedTaskGuid );
    }

    @Override
    public void setInstanceName(String instanceName) {
        this.mInstanceEntry.setInstanceName( instanceName );
    }

    @Override
    public void setBusinessTime(LocalDateTime businessTime) {
        this.mInstanceEntry.setBusinessTime( businessTime );
    }

    @Override
    public void setPriority(int priority) {
        this.mInstanceEntry.setPriority( priority );
    }

    @Override
    public void setActuallyPriority(int actuallyPriority) {
        this.mInstanceEntry.setActuallyPriority( actuallyPriority );
    }

    @Override
    public void setInstanceStatus(TaskInstanceStatus instanceStatus) {
        this.mInstanceEntry.setInstanceStatus( instanceStatus );
    }

    @Override
    public void setTaskType(String taskType) {
        this.mInstanceEntry.setTaskType( taskType );
    }

    @Override
    public void setRunCount(int runCount) {
        this.mInstanceEntry.setRunCount( runCount );
    }

    @Override
    public void setDryRun(boolean dryRun) {
        this.mInstanceEntry.setDryRun( dryRun );
    }

    @Override
    public void setKernelScheduleCycle(KernelTaskScheduleCycle kernelScheduleCycle) {
        this.mInstanceEntry.setKernelScheduleCycle( kernelScheduleCycle );
    }

    @Override
    public void setKernelScheduleType(KernelTaskScheduleType kernelScheduleType) {
        this.mInstanceEntry.setKernelScheduleType( kernelScheduleType );
    }

    @Override
    public void setLastStartTime(LocalDateTime lastStartTime) {
        this.mInstanceEntry.setLastStartTime( lastStartTime );
    }

    @Override
    public void setLastEndTime(LocalDateTime lastEndTime) {
        this.mInstanceEntry.setLastEndTime( lastEndTime );
    }

    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.mInstanceEntry.setCreateTime( createTime );
    }

    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.mInstanceEntry.setUpdateTime( updateTime );
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
    public GUID getAffiliatedTaskGuid() {
        return this.mInstanceEntry.getAffiliatedTaskGuid();
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
}
