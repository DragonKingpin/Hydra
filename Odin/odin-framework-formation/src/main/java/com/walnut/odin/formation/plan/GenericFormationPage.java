package com.walnut.odin.formation.plan;

import java.util.Collection;
import java.util.Collections;

import com.pinecone.framework.util.id.GUID;

public class GenericFormationPage implements FormationPage {
    protected long                       mnId;
    protected GUID                       mRunGuid;
    protected GUID                       mFormationGuid;
    protected long                       mnPageNo;
    protected String                     mszPageStatus;
    protected long                       mnFrameStart;
    protected long                       mnFrameEnd;
    protected long                       mnTotalCount;
    protected long                       mnCompletedCount;
    protected Collection<FormationFrame> mFrames = Collections.emptyList();

    @Override
    public long getId() {
        return this.mnId;
    }

    @Override
    public void setId( long id ) {
        this.mnId = id;
    }

    public GUID getRunGuid() {
        return this.mRunGuid;
    }

    public void setRunGuid( GUID runGuid ) {
        this.mRunGuid = runGuid;
    }

    public GUID getFormationGuid() {
        return this.mFormationGuid;
    }

    public void setFormationGuid( GUID formationGuid ) {
        this.mFormationGuid = formationGuid;
    }

    @Override
    public long pageNo() {
        return this.mnPageNo;
    }

    public long getPageNo() {
        return this.mnPageNo;
    }

    public void setPageNo( long pageNo ) {
        this.mnPageNo = pageNo;
    }

    public String getPageStatus() {
        return this.mszPageStatus;
    }

    public void setPageStatus( String pageStatus ) {
        this.mszPageStatus = pageStatus;
    }

    public long getFrameStart() {
        return this.mnFrameStart;
    }

    public void setFrameStart( long frameStart ) {
        this.mnFrameStart = frameStart;
    }

    public long getFrameEnd() {
        return this.mnFrameEnd;
    }

    public void setFrameEnd( long frameEnd ) {
        this.mnFrameEnd = frameEnd;
    }

    public long getTotalCount() {
        return this.mnTotalCount;
    }

    public void setTotalCount( long totalCount ) {
        this.mnTotalCount = totalCount;
    }

    public long getCompletedCount() {
        return this.mnCompletedCount;
    }

    public void setCompletedCount( long completedCount ) {
        this.mnCompletedCount = completedCount;
    }

    public void setFrames( Collection<FormationFrame> frames ) {
        this.mFrames = frames;
    }

    @Override
    public Collection<FormationFrame> frames() {
        return this.mFrames;
    }

    @Override
    public long elementSize() {
        return this.mnTotalCount;
    }
}
