package com.pinecone.hydra.task.kom.instance;

import java.time.LocalDateTime;
import java.util.Map;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.hydra.task.ArchInstanceMeta;
import com.pinecone.hydra.task.TaskInstanceStatus;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.EntryNode;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleCycle;
import com.pinecone.hydra.task.marshal.KernelTaskScheduleType;

public class GenericInstanceEntry extends ArchInstanceMeta implements InstanceEntry, EntryNode {

    protected long enumId;

    protected TaskInstrument taskInstrument;

    protected TaskElement taskElement;

    public GenericInstanceEntry() {
        super();

        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public GenericInstanceEntry( Map<String, Object > joEntity ) {
        this();
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericInstanceEntry( Map<String, Object > joEntity, TaskInstrument taskInstrument ) {
        this.apply(taskInstrument);
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    public GenericInstanceEntry( TaskInstrument taskInstrument ) {
        this( taskInstrument, null );
    }

    public GenericInstanceEntry( TaskInstrument taskInstrument, @Nullable TaskElement taskElement ) {
        this.taskElement = taskElement;
        this.apply(taskInstrument);
    }

    public void apply( TaskInstrument taskInstrument ) {
        this.taskInstrument = taskInstrument;
        GuidAllocator guidAllocator = this.taskInstrument.getGuidAllocator();
        this.setGuid( guidAllocator.nextGUID() );
        if ( this.createTime == null ) {
            this.createTime = LocalDateTime.now();
            this.updateTime = LocalDateTime.now();
        }

        if ( this.taskElement == null && this.getTaskGuid() != null ) {
            this.taskElement = (TaskElement) this.taskInstrument.get( this.getTaskGuid() );
        }
    }

    @Override
    public long getEnumId() {
        return this.enumId;
    }

    @Override
    public String getTaskName() {
        return this.taskElement.getName();
    }

    @Override
    public TaskElement taskElement() {
        return this.taskElement;
    }

    @Override
    public void setGuid ( GUID guid ) {
        this.guid = guid;
    }

    @Override
    public void setTaskGuid ( GUID taskGuid ) {
        this.taskGuid = taskGuid;
    }

    @Override
    public void setInstanceName ( String instanceName ) {
        this.instanceName = instanceName;
    }

    @Override
    public void setBusinessTime ( LocalDateTime businessTime ) {
        this.businessTime = businessTime;
    }

    @Override
    public void setPriority ( int priority ) {
        this.priority = (short) priority;
    }

    @Override
    public void setActuallyPriority ( int actuallyPriority ) {
        this.actuallyPriority = (short) actuallyPriority;
    }

    @Override
    public void setInstanceStatus ( TaskInstanceStatus instanceStatus ) {
        this.instanceStatus = instanceStatus;
    }

    @Override
    public void setTaskType ( String taskType ) {
        this.taskType = taskType;
    }

    @Override
    public void setRunCount ( int runCount ) {
        this.runCount = runCount;
    }

    @Override
    public void setDryRun ( boolean dryRun ) {
        this.dryRun = dryRun;
    }

    @Override
    public void setKernelScheduleCycle ( KernelTaskScheduleCycle kernelScheduleCycle ) {
        this.kernelScheduleCycle = kernelScheduleCycle;
    }

    @Override
    public void setKernelScheduleType ( KernelTaskScheduleType kernelScheduleType ) {
        this.kernelScheduleType = kernelScheduleType;
    }

    @Override
    public void setLastStartTime ( LocalDateTime lastStartTime ) {
        this.lastStartTime = lastStartTime;
    }

    @Override
    public void setLastEndTime ( LocalDateTime lastEndTime ) {
        this.lastEndTime = lastEndTime;
    }

    @Override
    public void setCreateTime ( LocalDateTime createTime ) {
        this.createTime = createTime;
    }

    @Override
    public void setUpdateTime ( LocalDateTime updateTime ) {
        this.updateTime = updateTime;
    }

    @Override
    public TaskInstrument getTaskInstrument() {
        return this.taskInstrument;
    }


    @Override
    public String getRunStatus() {
        if ( this.instanceStatus == null ) {
            return null;
        }
        return this.instanceStatus.getName();
    }

    public void setRunStatus ( String status ) {
        this.instanceStatus = TaskInstanceStatus.getByName( status );
    }

    public void setKernelScheduleCycleCode ( int code ) {
        this.kernelScheduleCycle = KernelTaskScheduleCycle.getByCode( code );
    }

    @Override
    public int getKernelScheduleCycleCode() {
        if ( this.kernelScheduleCycle == null ) {
            return KernelTaskScheduleCycle.Undefined.getCode();
        }

        return this.kernelScheduleCycle.getCode();
    }

    public void setKernelScheduleTypeCode ( int code ) {
        this.kernelScheduleType = KernelTaskScheduleType.getByCode( code );
    }

    @Override
    public int getKernelScheduleTypeCode() {
        if ( this.kernelScheduleType == null ) {
            return KernelTaskScheduleType.Undefined.getCode();
        }

        return this.kernelScheduleType.getCode();
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

}
