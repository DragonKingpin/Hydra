package com.pinecone.hydra.storage.file.transfer;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class GenericUofsTransferTask implements UofsTransferTask {
    protected long                       mnEnumId;
    protected GUID                       mGuid;
    protected UofsTransferOperation      mOperation;
    protected int                        mnSourceCount = 1;
    protected UofsTransferStatus         mStatus;
    protected UofsTransferPhase          mPhase;
    protected String                     mszSourcePath;
    protected String                     mszSourcePathHash;
    protected GUID                       mSourceBucketGuid;
    protected GUID                       mSourceGuid;
    protected UofsTransferSourceType     mSourceType;
    protected String                     mszTargetPath;
    protected String                     mszTargetPathHash;
    protected GUID                       mTargetBucketGuid;
    protected GUID                       mTargetParentGuid;
    protected GUID                       mTargetGuid;
    protected UofsTransferConflictPolicy mConflictPolicy;
    protected UofsTransferLinkPolicy     mLinkPolicy;
    protected long                       mnTotalCount;
    protected long                       mnDoneCount;
    protected long                       mnFailedCount;
    protected long                       mnTotalBytes;
    protected long                       mnDoneBytes;
    protected GUID                       mLastItemGuid;
    protected GUID                       mLastSourceGuid;
    protected GUID                       mLastTargetGuid;
    protected String                     mszMessage;
    protected String                     mszErrorMessage;
    protected String                     mszPlanSnapshot;
    protected String                     mszResultSnapshot;
    protected GUID                       mOperatorGuid;
    protected String                     mszExtConfig;
    protected LocalDateTime              mCreateTime;
    protected LocalDateTime              mUpdateTime;

    public long getEnumId() { return this.mnEnumId; }
    public void setEnumId( long enumId ) { this.mnEnumId = enumId; }
    public GUID getGuid() { return this.mGuid; }
    public void setGuid( GUID guid ) { this.mGuid = guid; }
    public UofsTransferOperation getOperation() { return this.mOperation; }
    public void setOperation( UofsTransferOperation operation ) { this.mOperation = operation; }
    public int getSourceCount() { return this.mnSourceCount; }
    public void setSourceCount( int sourceCount ) { this.mnSourceCount = Math.max( 1, sourceCount ); }
    public UofsTransferStatus getStatus() { return this.mStatus; }
    public void setStatus( UofsTransferStatus status ) { this.mStatus = status; }
    public UofsTransferPhase getPhase() { return this.mPhase; }
    public void setPhase( UofsTransferPhase phase ) { this.mPhase = phase; }
    public String getSourcePath() { return this.mszSourcePath; }
    public void setSourcePath( String sourcePath ) { this.mszSourcePath = sourcePath; }
    public String getSourcePathHash() { return this.mszSourcePathHash; }
    public void setSourcePathHash( String sourcePathHash ) { this.mszSourcePathHash = sourcePathHash; }
    public GUID getSourceBucketGuid() { return this.mSourceBucketGuid; }
    public void setSourceBucketGuid( GUID sourceBucketGuid ) { this.mSourceBucketGuid = sourceBucketGuid; }
    public GUID getSourceGuid() { return this.mSourceGuid; }
    public void setSourceGuid( GUID sourceGuid ) { this.mSourceGuid = sourceGuid; }
    public UofsTransferSourceType getSourceType() { return this.mSourceType; }
    public void setSourceType( UofsTransferSourceType sourceType ) { this.mSourceType = sourceType; }
    public String getTargetPath() { return this.mszTargetPath; }
    public void setTargetPath( String targetPath ) { this.mszTargetPath = targetPath; }
    public String getTargetPathHash() { return this.mszTargetPathHash; }
    public void setTargetPathHash( String targetPathHash ) { this.mszTargetPathHash = targetPathHash; }
    public GUID getTargetBucketGuid() { return this.mTargetBucketGuid; }
    public void setTargetBucketGuid( GUID targetBucketGuid ) { this.mTargetBucketGuid = targetBucketGuid; }
    public GUID getTargetParentGuid() { return this.mTargetParentGuid; }
    public void setTargetParentGuid( GUID targetParentGuid ) { this.mTargetParentGuid = targetParentGuid; }
    public GUID getTargetGuid() { return this.mTargetGuid; }
    public void setTargetGuid( GUID targetGuid ) { this.mTargetGuid = targetGuid; }
    public UofsTransferConflictPolicy getConflictPolicy() { return this.mConflictPolicy; }
    public void setConflictPolicy( UofsTransferConflictPolicy conflictPolicy ) { this.mConflictPolicy = conflictPolicy; }
    public UofsTransferLinkPolicy getLinkPolicy() { return this.mLinkPolicy; }
    public void setLinkPolicy( UofsTransferLinkPolicy linkPolicy ) { this.mLinkPolicy = linkPolicy; }
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
    public GUID getLastItemGuid() { return this.mLastItemGuid; }
    public void setLastItemGuid( GUID lastItemGuid ) { this.mLastItemGuid = lastItemGuid; }
    public GUID getLastSourceGuid() { return this.mLastSourceGuid; }
    public void setLastSourceGuid( GUID lastSourceGuid ) { this.mLastSourceGuid = lastSourceGuid; }
    public GUID getLastTargetGuid() { return this.mLastTargetGuid; }
    public void setLastTargetGuid( GUID lastTargetGuid ) { this.mLastTargetGuid = lastTargetGuid; }
    public String getMessage() { return this.mszMessage; }
    public void setMessage( String message ) { this.mszMessage = message; }
    public String getErrorMessage() { return this.mszErrorMessage; }
    public void setErrorMessage( String errorMessage ) { this.mszErrorMessage = errorMessage; }
    public String getPlanSnapshot() { return this.mszPlanSnapshot; }
    public void setPlanSnapshot( String planSnapshot ) { this.mszPlanSnapshot = planSnapshot; }
    public String getResultSnapshot() { return this.mszResultSnapshot; }
    public void setResultSnapshot( String resultSnapshot ) { this.mszResultSnapshot = resultSnapshot; }
    public GUID getOperatorGuid() { return this.mOperatorGuid; }
    public void setOperatorGuid( GUID operatorGuid ) { this.mOperatorGuid = operatorGuid; }
    public String getExtConfig() { return this.mszExtConfig; }
    public void setExtConfig( String extConfig ) { this.mszExtConfig = extConfig; }
    public LocalDateTime getCreateTime() { return this.mCreateTime; }
    public void setCreateTime( LocalDateTime createTime ) { this.mCreateTime = createTime; }
    public LocalDateTime getUpdateTime() { return this.mUpdateTime; }
    public void setUpdateTime( LocalDateTime updateTime ) { this.mUpdateTime = updateTime; }
}
