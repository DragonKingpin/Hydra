package com.walnut.odin.formation.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;

public class GenericGroupTask implements GroupTaskEntry {
    protected long          mnId;
    protected GUID          mGuid;
    protected GUID          mFormationGuid;
    protected GUID          mTaskGuid;
    protected String        mszTaskName;
    protected String        mszScheduleType;
    protected long          mnSequenceNo;
    protected short         mnPriority;
    protected boolean       mbEnable;
    protected LocalDateTime mCreateTime;
    protected LocalDateTime mUpdateTime;

    public long getId() {
        return this.mnId;
    }

    public void setId( long id ) {
        this.mnId = id;
    }

    @Override
    public GUID getGuid() {
        return this.mGuid;
    }

    public void setGuid( GUID guid ) {
        this.mGuid = guid;
    }

    @Override
    public GUID getFormationGuid() {
        return this.mFormationGuid;
    }

    public void setFormationGuid( GUID formationGuid ) {
        this.mFormationGuid = formationGuid;
    }

    @Override
    public GUID getTaskGuid() {
        return this.mTaskGuid;
    }

    public void setTaskGuid( GUID taskGuid ) {
        this.mTaskGuid = taskGuid;
    }

    @Override
    public String getTaskName() {
        return this.mszTaskName;
    }

    public void setTaskName( String taskName ) {
        this.mszTaskName = taskName;
    }

    @Override
    public String getScheduleType() {
        return this.mszScheduleType;
    }

    public void setScheduleType( String scheduleType ) {
        this.mszScheduleType = scheduleType;
    }

    @Override
    public long getSequenceNo() {
        return this.mnSequenceNo;
    }

    public void setSequenceNo( long sequenceNo ) {
        this.mnSequenceNo = sequenceNo;
    }

    @Override
    public short getPriority() {
        return this.mnPriority;
    }

    public void setPriority( short priority ) {
        this.mnPriority = priority;
    }

    @Override
    public boolean isEnable() {
        return this.mbEnable;
    }

    public void setEnable( boolean enable ) {
        this.mbEnable = enable;
    }

    public LocalDateTime getCreateTime() {
        return this.mCreateTime;
    }

    public void setCreateTime( LocalDateTime createTime ) {
        this.mCreateTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return this.mUpdateTime;
    }

    public void setUpdateTime( LocalDateTime updateTime ) {
        this.mUpdateTime = updateTime;
    }
}
