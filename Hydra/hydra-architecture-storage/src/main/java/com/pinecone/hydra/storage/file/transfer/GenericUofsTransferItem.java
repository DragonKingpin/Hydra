package com.pinecone.hydra.storage.file.transfer;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class GenericUofsTransferItem implements UofsTransferItem {
    protected long                   mnEnumId;
    protected GUID                   mGuid;
    protected GUID                   mTaskGuid;
    protected long                   mnItemIndex;
    protected int                    mnDepth;
    protected UofsTransferSourceType mSourceType;
    protected GUID                   mSourceBucketGuid;
    protected GUID                   mSourceGuid;
    protected GUID                   mSourceParentGuid;
    protected String                 mszSourcePath;
    protected String                 mszSourcePathHash;
    protected GUID                   mTargetBucketGuid;
    protected GUID                   mTargetGuid;
    protected GUID                   mTargetParentGuid;
    protected String                 mszTargetPath;
    protected String                 mszTargetPathHash;
    protected UofsTransferItemStatus mStatus;
    protected UofsTransferPhase      mPhase;
    protected long                   mnTotalBytes;
    protected long                   mnDoneBytes;
    protected int                    mnRetryCount;
    protected String                 mszMessage;
    protected String                 mszErrorMessage;
    protected String                 mszExtConfig;
    protected LocalDateTime          mCreateTime;
    protected LocalDateTime          mUpdateTime;

    public long getEnumId() { return this.mnEnumId; }
    public void setEnumId( long enumId ) { this.mnEnumId = enumId; }
    public GUID getGuid() { return this.mGuid; }
    public void setGuid( GUID guid ) { this.mGuid = guid; }
    public GUID getTaskGuid() { return this.mTaskGuid; }
    public void setTaskGuid( GUID taskGuid ) { this.mTaskGuid = taskGuid; }
    public long getItemIndex() { return this.mnItemIndex; }
    public void setItemIndex( long itemIndex ) { this.mnItemIndex = itemIndex; }
    public int getDepth() { return this.mnDepth; }
    public void setDepth( int depth ) { this.mnDepth = depth; }
    public UofsTransferSourceType getSourceType() { return this.mSourceType; }
    public void setSourceType( UofsTransferSourceType sourceType ) { this.mSourceType = sourceType; }
    public GUID getSourceBucketGuid() { return this.mSourceBucketGuid; }
    public void setSourceBucketGuid( GUID sourceBucketGuid ) { this.mSourceBucketGuid = sourceBucketGuid; }
    public GUID getSourceGuid() { return this.mSourceGuid; }
    public void setSourceGuid( GUID sourceGuid ) { this.mSourceGuid = sourceGuid; }
    public GUID getSourceParentGuid() { return this.mSourceParentGuid; }
    public void setSourceParentGuid( GUID sourceParentGuid ) { this.mSourceParentGuid = sourceParentGuid; }
    public String getSourcePath() { return this.mszSourcePath; }
    public void setSourcePath( String sourcePath ) { this.mszSourcePath = sourcePath; }
    public String getSourcePathHash() { return this.mszSourcePathHash; }
    public void setSourcePathHash( String sourcePathHash ) { this.mszSourcePathHash = sourcePathHash; }
    public GUID getTargetBucketGuid() { return this.mTargetBucketGuid; }
    public void setTargetBucketGuid( GUID targetBucketGuid ) { this.mTargetBucketGuid = targetBucketGuid; }
    public GUID getTargetGuid() { return this.mTargetGuid; }
    public void setTargetGuid( GUID targetGuid ) { this.mTargetGuid = targetGuid; }
    public GUID getTargetParentGuid() { return this.mTargetParentGuid; }
    public void setTargetParentGuid( GUID targetParentGuid ) { this.mTargetParentGuid = targetParentGuid; }
    public String getTargetPath() { return this.mszTargetPath; }
    public void setTargetPath( String targetPath ) { this.mszTargetPath = targetPath; }
    public String getTargetPathHash() { return this.mszTargetPathHash; }
    public void setTargetPathHash( String targetPathHash ) { this.mszTargetPathHash = targetPathHash; }
    public UofsTransferItemStatus getStatus() { return this.mStatus; }
    public void setStatus( UofsTransferItemStatus status ) { this.mStatus = status; }
    public UofsTransferPhase getPhase() { return this.mPhase; }
    public void setPhase( UofsTransferPhase phase ) { this.mPhase = phase; }
    public long getTotalBytes() { return this.mnTotalBytes; }
    public void setTotalBytes( long totalBytes ) { this.mnTotalBytes = totalBytes; }
    public long getDoneBytes() { return this.mnDoneBytes; }
    public void setDoneBytes( long doneBytes ) { this.mnDoneBytes = doneBytes; }
    public int getRetryCount() { return this.mnRetryCount; }
    public void setRetryCount( int retryCount ) { this.mnRetryCount = retryCount; }
    public String getMessage() { return this.mszMessage; }
    public void setMessage( String message ) { this.mszMessage = message; }
    public String getErrorMessage() { return this.mszErrorMessage; }
    public void setErrorMessage( String errorMessage ) { this.mszErrorMessage = errorMessage; }
    public String getExtConfig() { return this.mszExtConfig; }
    public void setExtConfig( String extConfig ) { this.mszExtConfig = extConfig; }
    public LocalDateTime getCreateTime() { return this.mCreateTime; }
    public void setCreateTime( LocalDateTime createTime ) { this.mCreateTime = createTime; }
    public LocalDateTime getUpdateTime() { return this.mUpdateTime; }
    public void setUpdateTime( LocalDateTime updateTime ) { this.mUpdateTime = updateTime; }
}
