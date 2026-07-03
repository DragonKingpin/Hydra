package com.walnut.odin.formation.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;

public class GenericRun implements RunEntry {
    protected long          mnId;
    protected GUID          mGuid;
    protected GUID          mFormationGuid;
    protected String        mszStrategyType;
    protected String        mszRunStatus;
    protected long          mnPageSize;
    protected long          mnFrameSize;
    protected long          mnWindowSize;
    protected long          mnInflightLimit;
    protected long          mnTotalCount;
    protected long          mnSubmittedCount;
    protected long          mnCompletedCount;
    protected long          mnFailedCount;
    protected long          mnSuspendedCount;
    protected LocalDateTime mStartTime;
    protected LocalDateTime mFinishTime;
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
    public String getStrategyType() {
        return this.mszStrategyType;
    }

    public void setStrategyType( String strategyType ) {
        this.mszStrategyType = strategyType;
    }

    @Override
    public String getRunStatus() {
        return this.mszRunStatus;
    }

    public void setRunStatus( String runStatus ) {
        this.mszRunStatus = runStatus;
    }

    @Override
    public long getPageSize() {
        return this.mnPageSize;
    }

    public void setPageSize( long pageSize ) {
        this.mnPageSize = pageSize;
    }

    @Override
    public long getFrameSize() {
        return this.mnFrameSize;
    }

    public void setFrameSize( long frameSize ) {
        this.mnFrameSize = frameSize;
    }

    @Override
    public long getWindowSize() {
        return this.mnWindowSize;
    }

    public void setWindowSize( long windowSize ) {
        this.mnWindowSize = windowSize;
    }

    @Override
    public long getInflightLimit() {
        return this.mnInflightLimit;
    }

    public void setInflightLimit( long inflightLimit ) {
        this.mnInflightLimit = inflightLimit;
    }

    @Override
    public long getTotalCount() {
        return this.mnTotalCount;
    }

    public void setTotalCount( long totalCount ) {
        this.mnTotalCount = totalCount;
    }

    @Override
    public long getSubmittedCount() {
        return this.mnSubmittedCount;
    }

    public void setSubmittedCount( long submittedCount ) {
        this.mnSubmittedCount = submittedCount;
    }

    @Override
    public long getCompletedCount() {
        return this.mnCompletedCount;
    }

    public void setCompletedCount( long completedCount ) {
        this.mnCompletedCount = completedCount;
    }

    @Override
    public long getFailedCount() {
        return this.mnFailedCount;
    }

    public void setFailedCount( long failedCount ) {
        this.mnFailedCount = failedCount;
    }

    @Override
    public long getSuspendedCount() {
        return this.mnSuspendedCount;
    }

    public void setSuspendedCount( long suspendedCount ) {
        this.mnSuspendedCount = suspendedCount;
    }

    public LocalDateTime getStartTime() {
        return this.mStartTime;
    }

    public void setStartTime( LocalDateTime startTime ) {
        this.mStartTime = startTime;
    }

    public LocalDateTime getFinishTime() {
        return this.mFinishTime;
    }

    public void setFinishTime( LocalDateTime finishTime ) {
        this.mFinishTime = finishTime;
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
