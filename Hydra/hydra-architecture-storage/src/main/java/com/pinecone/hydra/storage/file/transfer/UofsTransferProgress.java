package com.pinecone.hydra.storage.file.transfer;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class UofsTransferProgress implements Pinenut {
    protected GUID              mTaskGuid;
    protected GUID              mItemGuid;
    protected UofsTransferPhase mPhase;
    protected long              mnTotalCount;
    protected long              mnDoneCount;
    protected long              mnFailedCount;
    protected long              mnTotalBytes;
    protected long              mnDoneBytes;
    protected String            mszMessage;

    public GUID getTaskGuid() { return this.mTaskGuid; }
    public void setTaskGuid( GUID taskGuid ) { this.mTaskGuid = taskGuid; }
    public GUID getItemGuid() { return this.mItemGuid; }
    public void setItemGuid( GUID itemGuid ) { this.mItemGuid = itemGuid; }
    public UofsTransferPhase getPhase() { return this.mPhase; }
    public void setPhase( UofsTransferPhase phase ) { this.mPhase = phase; }
    public long getTotalCount() { return this.mnTotalCount; }
    public void setTotalCount( long totalCount ) { this.mnTotalCount = totalCount; }
    public long getDoneCount() { return this.mnDoneCount; }
    public void setDoneCount( long doneCount ) { this.mnDoneCount = doneCount; }
    public long getFailedCount() { return this.mnFailedCount; }
    public void setFailedCount( long failedCount ) { this.mnFailedCount = failedCount; }
    public long getTotalBytes() { return this.mnTotalBytes; }
    public void setTotalBytes( long totalBytes ) { this.mnTotalBytes = totalBytes; }
    public long getDoneBytes() { return this.mnDoneBytes; }
    public void setDoneBytes( long doneBytes ) { this.mnDoneBytes = doneBytes; }
    public String getMessage() { return this.mszMessage; }
    public void setMessage( String message ) { this.mszMessage = message; }
}
