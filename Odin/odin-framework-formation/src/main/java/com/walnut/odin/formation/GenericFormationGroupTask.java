package com.walnut.odin.formation;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class GenericFormationGroupTask implements Pinenut {
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

    public long getId() { return this.mnId; }
    public void setId( long id ) { this.mnId = id; }
    public GUID getGuid() { return this.mGuid; }
    public void setGuid( GUID guid ) { this.mGuid = guid; }
    public GUID getFormationGuid() { return this.mFormationGuid; }
    public void setFormationGuid( GUID formationGuid ) { this.mFormationGuid = formationGuid; }
    public GUID getTaskGuid() { return this.mTaskGuid; }
    public void setTaskGuid( GUID taskGuid ) { this.mTaskGuid = taskGuid; }
    public String getTaskName() { return this.mszTaskName; }
    public void setTaskName( String taskName ) { this.mszTaskName = taskName; }
    public String getScheduleType() { return this.mszScheduleType; }
    public void setScheduleType( String scheduleType ) { this.mszScheduleType = scheduleType; }
    public long getSequenceNo() { return this.mnSequenceNo; }
    public void setSequenceNo( long sequenceNo ) { this.mnSequenceNo = sequenceNo; }
    public short getPriority() { return this.mnPriority; }
    public void setPriority( short priority ) { this.mnPriority = priority; }
    public boolean isEnable() { return this.mbEnable; }
    public void setEnable( boolean enable ) { this.mbEnable = enable; }
    public LocalDateTime getCreateTime() { return this.mCreateTime; }
    public void setCreateTime( LocalDateTime createTime ) { this.mCreateTime = createTime; }
    public LocalDateTime getUpdateTime() { return this.mUpdateTime; }
    public void setUpdateTime( LocalDateTime updateTime ) { this.mUpdateTime = updateTime; }
}
