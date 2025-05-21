package com.pinecone.hydra.task.kom.instance.dto;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.slime.entity.ArchEnumIndexableEntity;

public class GenericInstance extends ArchEnumIndexableEntity implements Instance{


    protected  GUID mGuid;

    protected  String mName;

    protected String mBusinessDate;

    protected String mPriority;

    protected String mRunStatus;

    protected String mScheduleCycle;

    protected String mTaskType;

    protected short mRunCount;

    protected String mScheduleType;

    protected String mLastStartTime;

    protected String mLastEndTime;

    protected GUID taskGuid;

    @Override
    public void setGuid( GUID guid ) {
        this.mGuid = guid;
    }

    @Override
    public GUID getGuid() {
        return  mGuid;
    }

    @Override
    public void setBusinessDate( String businessDate ) {
        this.mBusinessDate = businessDate;
    }

    @Override
    public String getBusinessDate() {
        return this.mBusinessDate;
    }

    @Override
    public void setPriority( String priority ) {
        this.mPriority = priority;
    }

    @Override
    public String getPriority() {
        return this.mPriority ;
    }

    @Override
    public void setRunStatus( String runStatus ) {
            this.mRunStatus = runStatus;
    }

    @Override
    public String getRunStatus() {
        return this.mRunStatus;
    }

    @Override
    public void setScheduleCycle( String scheduleCycle ) {
        this.mScheduleCycle = scheduleCycle;
    }

    @Override
    public String getScheduleCycle() {
        return this.mScheduleCycle;
    }

    @Override
    public void setTaskType( String taskType ) {
        this.mTaskType = taskType;
    }

    @Override
    public String getTaskType() {
        return this.mTaskType;
    }


    @Override
    public void setRunCount( String runCount ) {
        this.mRunStatus = runCount;
    }

    @Override
    public short getRunCount() {
        return this.mRunCount;
    }

    @Override
    public void setScheduleType( String scheduleType ) {
        this.mScheduleType = scheduleType;
    }

    @Override
    public String getScheduleType() {
        return this.mScheduleType;
    }

    @Override
    public void setlastStartTime( String lastStartTime ) {
        this.mLastStartTime = lastStartTime;
    }

    @Override
    public String getlastStartTime() {
        return this.mLastStartTime;
    }

    @Override
    public void setlastEndTime( String lastEndTime ) {
        this.mLastEndTime = lastEndTime;
    }

    @Override
    public String getlastEndTime() {
        return this.mLastEndTime;
    }
}
