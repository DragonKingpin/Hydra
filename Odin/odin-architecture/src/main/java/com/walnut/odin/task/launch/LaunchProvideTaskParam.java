package com.walnut.odin.task.launch;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.instance.InstanceEntry;
import com.pinecone.hydra.task.marshal.TaskScheduleType;

public class LaunchProvideTaskParam implements Pinenut {

    protected GUID          mTaskGuid;
    protected GUID          mInstanceGuid;
    protected String        mszTaskName;
    protected String        mszInstanceName;
    protected int           mnSequenceCnt;
    protected int           mnRetryCnt;
    protected String        mszTaskType;
    protected String        mszImagePath;
    protected String        mszExecArch;
    protected String        mszScheduleType;
    protected Long          mTimeoutSeconds;
    protected LocalDateTime mBusinessTime;
    protected LocalDateTime mExpectTime;
    protected LocalDateTime mFireTime;

    public static LaunchProvideTaskParam from( InstanceEntry entry ) {
        LaunchProvideTaskParam param = new LaunchProvideTaskParam();
        if ( entry == null ) {
            return param;
        }

        param.setTaskGuid( entry.getTaskGuid() );
        param.setInstanceGuid( entry.getGuid() );
        param.setTaskName( entry.getTaskName() );
        param.setInstanceName( entry.getInstanceName() );
        param.setSequenceCnt( entry.getSequenceCnt() );
        param.setRetryCnt( entry.getRetryCnt() );
        param.setTaskType( entry.getTaskType() );
        param.setImagePath( entry.getImagePath() );
        param.setExecArch( entry.getExecArch() );
        param.setScheduleType( stringify( entry.getKernelScheduleType() ) );
        param.setTimeoutSeconds( entry.getTimeoutSeconds() );
        param.setBusinessTime( entry.getBusinessTime() );
        param.setExpectTime( entry.getExpectTime() );
        param.setFireTime( entry.getFireTime() );
        return param;
    }

    protected static String stringify( TaskScheduleType scheduleType ) {
        if ( scheduleType == null ) {
            return null;
        }
        return scheduleType.getName();
    }

    public GUID getTaskGuid() {
        return this.mTaskGuid;
    }

    public void setTaskGuid( GUID taskGuid ) {
        this.mTaskGuid = taskGuid;
    }

    public GUID getInstanceGuid() {
        return this.mInstanceGuid;
    }

    public void setInstanceGuid( GUID instanceGuid ) {
        this.mInstanceGuid = instanceGuid;
    }

    public String getTaskName() {
        return this.mszTaskName;
    }

    public void setTaskName( String szTaskName ) {
        this.mszTaskName = szTaskName;
    }

    public String getInstanceName() {
        return this.mszInstanceName;
    }

    public void setInstanceName( String szInstanceName ) {
        this.mszInstanceName = szInstanceName;
    }

    public int getSequenceCnt() {
        return this.mnSequenceCnt;
    }

    public void setSequenceCnt( int nSequenceCnt ) {
        this.mnSequenceCnt = nSequenceCnt;
    }

    public int getRetryCnt() {
        return this.mnRetryCnt;
    }

    public void setRetryCnt( int nRetryCnt ) {
        this.mnRetryCnt = nRetryCnt;
    }

    public String getTaskType() {
        return this.mszTaskType;
    }

    public void setTaskType( String szTaskType ) {
        this.mszTaskType = szTaskType;
    }

    public String getImagePath() {
        return this.mszImagePath;
    }

    public void setImagePath( String szImagePath ) {
        this.mszImagePath = szImagePath;
    }

    public String getExecArch() {
        return this.mszExecArch;
    }

    public void setExecArch( String szExecArch ) {
        this.mszExecArch = szExecArch;
    }

    public String getScheduleType() {
        return this.mszScheduleType;
    }

    public void setScheduleType( String szScheduleType ) {
        this.mszScheduleType = szScheduleType;
    }

    public Long getTimeoutSeconds() {
        return this.mTimeoutSeconds;
    }

    public void setTimeoutSeconds( Long timeoutSeconds ) {
        this.mTimeoutSeconds = timeoutSeconds;
    }

    public LocalDateTime getBusinessTime() {
        return this.mBusinessTime;
    }

    public void setBusinessTime( LocalDateTime businessTime ) {
        this.mBusinessTime = businessTime;
    }

    public LocalDateTime getExpectTime() {
        return this.mExpectTime;
    }

    public void setExpectTime( LocalDateTime expectTime ) {
        this.mExpectTime = expectTime;
    }

    public LocalDateTime getFireTime() {
        return this.mFireTime;
    }

    public void setFireTime( LocalDateTime fireTime ) {
        this.mFireTime = fireTime;
    }

}
