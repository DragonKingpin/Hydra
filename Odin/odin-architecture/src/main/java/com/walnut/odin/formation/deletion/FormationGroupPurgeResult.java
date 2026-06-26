package com.walnut.odin.formation.deletion;

import com.pinecone.framework.system.prototype.Pinenut;

public class FormationGroupPurgeResult implements Pinenut {
    protected int requestedCount;
    protected int removedFrameCount;
    protected int removedPageCount;
    protected int removedRunCount;
    protected int removedGroupTaskCount;
    protected int removedGroupCount;

    public int getRequestedCount() {
        return this.requestedCount;
    }

    public void setRequestedCount( int requestedCount ) {
        this.requestedCount = requestedCount;
    }

    public int getRemovedFrameCount() {
        return this.removedFrameCount;
    }

    public void setRemovedFrameCount( int removedFrameCount ) {
        this.removedFrameCount = removedFrameCount;
    }

    public int getRemovedPageCount() {
        return this.removedPageCount;
    }

    public void setRemovedPageCount( int removedPageCount ) {
        this.removedPageCount = removedPageCount;
    }

    public int getRemovedRunCount() {
        return this.removedRunCount;
    }

    public void setRemovedRunCount( int removedRunCount ) {
        this.removedRunCount = removedRunCount;
    }

    public int getRemovedGroupTaskCount() {
        return this.removedGroupTaskCount;
    }

    public void setRemovedGroupTaskCount( int removedGroupTaskCount ) {
        this.removedGroupTaskCount = removedGroupTaskCount;
    }

    public int getRemovedGroupCount() {
        return this.removedGroupCount;
    }

    public void setRemovedGroupCount( int removedGroupCount ) {
        this.removedGroupCount = removedGroupCount;
    }
}
