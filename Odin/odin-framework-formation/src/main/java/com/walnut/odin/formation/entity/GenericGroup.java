package com.walnut.odin.formation.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;

public class GenericGroup implements GroupEntry {
    protected long          mnId;
    protected GUID          mGuid;
    protected String        mszIdentifier;
    protected String        mszTitle;
    protected GUID          mProjectGuid;
    protected String        mszStrategyType;
    protected long          mnPageSize;
    protected long          mnFrameSize;
    protected long          mnWindowSize;
    protected long          mnInflightLimit;
    protected boolean       mbEnable;
    protected String        mszDescription;
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
    public String getIdentifier() {
        return this.mszIdentifier;
    }

    public void setIdentifier( String identifier ) {
        this.mszIdentifier = identifier;
    }

    @Override
    public String getTitle() {
        return this.mszTitle;
    }

    public void setTitle( String title ) {
        this.mszTitle = title;
    }

    @Override
    public GUID getProjectGuid() {
        return this.mProjectGuid;
    }

    public void setProjectGuid( GUID projectGuid ) {
        this.mProjectGuid = projectGuid;
    }

    @Override
    public String getStrategyType() {
        return this.mszStrategyType;
    }

    public void setStrategyType( String strategyType ) {
        this.mszStrategyType = strategyType;
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
    public boolean isEnable() {
        return this.mbEnable;
    }

    public void setEnable( boolean enable ) {
        this.mbEnable = enable;
    }

    public String getDescription() {
        return this.mszDescription;
    }

    public void setDescription( String description ) {
        this.mszDescription = description;
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
