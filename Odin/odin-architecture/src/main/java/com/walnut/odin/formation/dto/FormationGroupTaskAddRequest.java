package com.walnut.odin.formation.dto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class FormationGroupTaskAddRequest implements Pinenut {
    protected GUID    mGuid;
    protected GUID    mFormationGuid;
    protected GUID    mTaskGuid;
    protected String  mszTaskName;
    protected String  mszScheduleType;
    protected long    mnSequenceNo;
    protected short   mnPriority;
    protected boolean mbEnable;

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
}
