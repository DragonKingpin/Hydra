package com.pinecone.hydra.task.kom.instance;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class TaskInstanceQuery implements Pinenut {

    public static final long DEFAULT_LIMIT = 20;

    public static final long MAX_LIMIT = 1000;

    protected long mOffset = 0;

    protected long mLimit = DEFAULT_LIMIT;

    protected String mszKeyword;

    protected GUID mTaskGuid;

    protected String mszRunStatus;

    protected String mszTaskType;

    protected String mszScheduleCycle;

    protected String mszScheduleType;

    protected String mszProcessorName;

    protected LocalDateTime mBusinessTimeStart;

    protected LocalDateTime mBusinessTimeEnd;

    protected LocalDateTime mExpectTimeStart;

    protected LocalDateTime mExpectTimeEnd;

    protected LocalDateTime mStartTimeStart;

    protected LocalDateTime mStartTimeEnd;

    protected LocalDateTime mFinishTimeStart;

    protected LocalDateTime mFinishTimeEnd;

    public long getOffset() {
        return this.mOffset;
    }

    public void setOffset( long nOffset ) {
        this.mOffset = Math.max( 0, nOffset );
    }

    public long getLimit() {
        return this.mLimit;
    }

    public void setLimit( long nLimit ) {
        if ( nLimit <= 0 ) {
            this.mLimit = DEFAULT_LIMIT;
            return;
        }
        this.mLimit = Math.min( nLimit, MAX_LIMIT );
    }

    public String getKeyword() {
        return this.mszKeyword;
    }

    public void setKeyword( String szKeyword ) {
        this.mszKeyword = szKeyword;
    }

    public GUID getTaskGuid() {
        return this.mTaskGuid;
    }

    public void setTaskGuid( GUID taskGuid ) {
        this.mTaskGuid = taskGuid;
    }

    public String getRunStatus() {
        return this.mszRunStatus;
    }

    public void setRunStatus( String szRunStatus ) {
        this.mszRunStatus = szRunStatus;
    }

    public String getTaskType() {
        return this.mszTaskType;
    }

    public void setTaskType( String szTaskType ) {
        this.mszTaskType = szTaskType;
    }

    public String getScheduleCycle() {
        return this.mszScheduleCycle;
    }

    public void setScheduleCycle( String szScheduleCycle ) {
        this.mszScheduleCycle = szScheduleCycle;
    }

    public String getScheduleType() {
        return this.mszScheduleType;
    }

    public void setScheduleType( String szScheduleType ) {
        this.mszScheduleType = szScheduleType;
    }

    public String getProcessorName() {
        return this.mszProcessorName;
    }

    public void setProcessorName( String szProcessorName ) {
        this.mszProcessorName = szProcessorName;
    }

    public LocalDateTime getBusinessTimeStart() {
        return this.mBusinessTimeStart;
    }

    public void setBusinessTimeStart( LocalDateTime businessTimeStart ) {
        this.mBusinessTimeStart = businessTimeStart;
    }

    public LocalDateTime getBusinessTimeEnd() {
        return this.mBusinessTimeEnd;
    }

    public void setBusinessTimeEnd( LocalDateTime businessTimeEnd ) {
        this.mBusinessTimeEnd = businessTimeEnd;
    }

    public LocalDateTime getExpectTimeStart() {
        return this.mExpectTimeStart;
    }

    public void setExpectTimeStart( LocalDateTime expectTimeStart ) {
        this.mExpectTimeStart = expectTimeStart;
    }

    public LocalDateTime getExpectTimeEnd() {
        return this.mExpectTimeEnd;
    }

    public void setExpectTimeEnd( LocalDateTime expectTimeEnd ) {
        this.mExpectTimeEnd = expectTimeEnd;
    }

    public LocalDateTime getStartTimeStart() {
        return this.mStartTimeStart;
    }

    public void setStartTimeStart( LocalDateTime startTimeStart ) {
        this.mStartTimeStart = startTimeStart;
    }

    public LocalDateTime getStartTimeEnd() {
        return this.mStartTimeEnd;
    }

    public void setStartTimeEnd( LocalDateTime startTimeEnd ) {
        this.mStartTimeEnd = startTimeEnd;
    }

    public LocalDateTime getFinishTimeStart() {
        return this.mFinishTimeStart;
    }

    public void setFinishTimeStart( LocalDateTime finishTimeStart ) {
        this.mFinishTimeStart = finishTimeStart;
    }

    public LocalDateTime getFinishTimeEnd() {
        return this.mFinishTimeEnd;
    }

    public void setFinishTimeEnd( LocalDateTime finishTimeEnd ) {
        this.mFinishTimeEnd = finishTimeEnd;
    }
}
