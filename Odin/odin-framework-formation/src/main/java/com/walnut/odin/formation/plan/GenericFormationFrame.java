package com.walnut.odin.formation.plan;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;

public class GenericFormationFrame implements FormationFrame {
    protected long          mnId;
    protected GUID          mGuid;
    protected GUID          mRunGuid;
    protected GUID          mFormationGuid;
    protected GUID          mGroupGuid;
    protected GUID          mTaskGuid;
    protected String        mszTaskName;
    protected long          mnPageNo;
    protected long          mnFrameNo;
    protected String        mszQueueType;
    protected String        mszFrameStatus;
    protected short         mnPriority;
    protected GUID          mInstanceGuid;
    protected String        mszClaimOwner;
    protected GUID          mClaimToken;
    protected LocalDateTime mLeaseExpireTime;
    protected LocalDateTime mSubmitTime;
    protected LocalDateTime mFinishTime;
    protected int           mnAttemptCount;
    protected String        mszErrorCause;

    @Override
    public long getId() { return this.mnId; }
    public void setId( long id ) { this.mnId = id; }
    public GUID getGuid() { return this.mGuid; }
    public void setGuid( GUID guid ) { this.mGuid = guid; }
    public GUID getRunGuid() { return this.mRunGuid; }
    public void setRunGuid( GUID runGuid ) { this.mRunGuid = runGuid; }
    @Override
    public GUID formationGuid() { return this.mFormationGuid; }
    public GUID getFormationGuid() { return this.mFormationGuid; }
    public void setFormationGuid( GUID formationGuid ) { this.mFormationGuid = formationGuid; }
    @Override
    public GUID groupGuid() { return this.mGroupGuid; }
    public GUID getGroupGuid() { return this.mGroupGuid; }
    public void setGroupGuid( GUID groupGuid ) { this.mGroupGuid = groupGuid; }
    @Override
    public GUID taskGuid() { return this.mTaskGuid; }
    public GUID getTaskGuid() { return this.mTaskGuid; }
    public void setTaskGuid( GUID taskGuid ) { this.mTaskGuid = taskGuid; }
    public String getTaskName() { return this.mszTaskName; }
    public void setTaskName( String taskName ) { this.mszTaskName = taskName; }
    @Override
    public long pageNo() { return this.mnPageNo; }
    public long getPageNo() { return this.mnPageNo; }
    public void setPageNo( long pageNo ) { this.mnPageNo = pageNo; }
    @Override
    public long frameNo() { return this.mnFrameNo; }
    public long getFrameNo() { return this.mnFrameNo; }
    public void setFrameNo( long frameNo ) { this.mnFrameNo = frameNo; }
    public String getQueueType() { return this.mszQueueType; }
    public void setQueueType( String queueType ) { this.mszQueueType = queueType; }
    public String getFrameStatus() { return this.mszFrameStatus; }
    public void setFrameStatus( String frameStatus ) { this.mszFrameStatus = frameStatus; }
    public short getPriority() { return this.mnPriority; }
    public void setPriority( short priority ) { this.mnPriority = priority; }
    public GUID getInstanceGuid() { return this.mInstanceGuid; }
    public void setInstanceGuid( GUID instanceGuid ) { this.mInstanceGuid = instanceGuid; }
    public String getClaimOwner() { return this.mszClaimOwner; }
    public void setClaimOwner( String claimOwner ) { this.mszClaimOwner = claimOwner; }
    public GUID getClaimToken() { return this.mClaimToken; }
    public void setClaimToken( GUID claimToken ) { this.mClaimToken = claimToken; }
    public LocalDateTime getLeaseExpireTime() { return this.mLeaseExpireTime; }
    public void setLeaseExpireTime( LocalDateTime leaseExpireTime ) { this.mLeaseExpireTime = leaseExpireTime; }
    public LocalDateTime getSubmitTime() { return this.mSubmitTime; }
    public void setSubmitTime( LocalDateTime submitTime ) { this.mSubmitTime = submitTime; }
    public LocalDateTime getFinishTime() { return this.mFinishTime; }
    public void setFinishTime( LocalDateTime finishTime ) { this.mFinishTime = finishTime; }
    public int getAttemptCount() { return this.mnAttemptCount; }
    public void setAttemptCount( int attemptCount ) { this.mnAttemptCount = attemptCount; }
    public String getErrorCause() { return this.mszErrorCause; }
    public void setErrorCause( String errorCause ) { this.mszErrorCause = errorCause; }
}
