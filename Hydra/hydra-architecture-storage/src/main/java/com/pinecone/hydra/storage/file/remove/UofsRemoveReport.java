package com.pinecone.hydra.storage.file.remove;

import com.pinecone.framework.system.prototype.Pinenut;

public class UofsRemoveReport implements Pinenut {
    protected long mnTotalCount;
    protected long mnDoneCount;
    protected boolean mbFailed;
    protected String mszMessage;

    public long getTotalCount() {
        return this.mnTotalCount;
    }

    public void setTotalCount( long totalCount ) {
        this.mnTotalCount = totalCount;
    }

    public long getDoneCount() {
        return this.mnDoneCount;
    }

    public void setDoneCount( long doneCount ) {
        this.mnDoneCount = doneCount;
    }

    public boolean isFailed() {
        return this.mbFailed;
    }

    public void setFailed( boolean failed ) {
        this.mbFailed = failed;
    }

    public String getMessage() {
        return this.mszMessage;
    }

    public void setMessage( String message ) {
        this.mszMessage = message;
    }
}
