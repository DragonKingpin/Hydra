package com.pinecone.hydra.storage.bucket.purge;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class BucketPurgeReport implements Pinenut {
    protected GUID                  mBucketGuid;
    protected BucketPurgeOperation mOperation;
    protected BucketPurgePhase     mPhase;
    protected long                  mnTotalCount;
    protected long                  mnDoneCount;
    protected boolean               mbFailed;
    protected String                mszMessage;

    public GUID getBucketGuid() {
        return this.mBucketGuid;
    }

    public void setBucketGuid( GUID bucketGuid ) {
        this.mBucketGuid = bucketGuid;
    }

    public BucketPurgeOperation getOperation() {
        return this.mOperation;
    }

    public void setOperation( BucketPurgeOperation operation ) {
        this.mOperation = operation;
    }

    public BucketPurgePhase getPhase() {
        return this.mPhase;
    }

    public void setPhase( BucketPurgePhase phase ) {
        this.mPhase = phase;
    }

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

    public boolean getFailed() {
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
