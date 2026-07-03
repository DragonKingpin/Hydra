package com.pinecone.hydra.storage.file.transfer;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.ArrayList;
import java.util.List;

public class UofsTransferPlan implements Pinenut {
    protected UofsTransferOperation mOperation;
    protected String                mszSourcePath;
    protected final List<String>    mSourcePaths = new ArrayList<>();
    protected String                mszTargetPath;
    protected UofsTransferTargetMode mTargetMode;
    protected UofsTransferSourceType mSourceType;
    protected UofsTransferMoveMode mMoveMode;
    protected UofsTransferSourceCleanupPolicy mSourceCleanupPolicy;
    protected UofsTransferDirectoryPolicy mDirectoryPolicy;
    protected UofsTransferItemTrackingMode mItemTrackingMode;
    protected long                  mnTotalCount;
    protected long                  mnTotalBytes;
    protected boolean               mbExecutable = true;
    protected List<String>          mBlockers = new ArrayList<>();
    protected List<String>          mWarnings = new ArrayList<>();
    protected List<UofsTransferRootPlan> mRootPlans = new ArrayList<>();

    public UofsTransferOperation getOperation() {
        return this.mOperation;
    }

    public void setOperation( UofsTransferOperation operation ) {
        this.mOperation = operation;
    }

    public String getSourcePath() {
        return this.mszSourcePath;
    }

    public void setSourcePath( String sourcePath ) {
        this.mszSourcePath = sourcePath;
    }

    public List<String> getSourcePaths() {
        return this.mSourcePaths;
    }

    public void setSourcePaths( List<String> sourcePaths ) {
        this.mSourcePaths.clear();
        if ( sourcePaths != null ) {
            this.mSourcePaths.addAll( sourcePaths );
        }
    }

    public int getSourceCount() {
        return this.mSourcePaths.isEmpty() ? 1 : this.mSourcePaths.size();
    }

    public String getTargetPath() {
        return this.mszTargetPath;
    }

    public void setTargetPath( String targetPath ) {
        this.mszTargetPath = targetPath;
    }

    public UofsTransferTargetMode getTargetMode() {
        return this.mTargetMode;
    }

    public void setTargetMode( UofsTransferTargetMode targetMode ) {
        this.mTargetMode = targetMode;
    }

    public UofsTransferSourceType getSourceType() {
        return this.mSourceType;
    }

    public void setSourceType( UofsTransferSourceType sourceType ) {
        this.mSourceType = sourceType;
    }

    public UofsTransferMoveMode getMoveMode() {
        return this.mMoveMode;
    }

    public void setMoveMode( UofsTransferMoveMode moveMode ) {
        this.mMoveMode = moveMode;
    }

    public UofsTransferSourceCleanupPolicy getSourceCleanupPolicy() {
        return this.mSourceCleanupPolicy;
    }

    public void setSourceCleanupPolicy( UofsTransferSourceCleanupPolicy sourceCleanupPolicy ) {
        this.mSourceCleanupPolicy = sourceCleanupPolicy;
    }

    public UofsTransferDirectoryPolicy getDirectoryPolicy() {
        return this.mDirectoryPolicy;
    }

    public void setDirectoryPolicy( UofsTransferDirectoryPolicy directoryPolicy ) {
        this.mDirectoryPolicy = directoryPolicy;
    }

    public UofsTransferItemTrackingMode getItemTrackingMode() {
        return this.mItemTrackingMode;
    }

    public void setItemTrackingMode( UofsTransferItemTrackingMode itemTrackingMode ) {
        this.mItemTrackingMode = itemTrackingMode;
    }

    public long getTotalCount() {
        return this.mnTotalCount;
    }

    public void setTotalCount( long totalCount ) {
        this.mnTotalCount = totalCount;
    }

    public long getTotalBytes() {
        return this.mnTotalBytes;
    }

    public void setTotalBytes( long totalBytes ) {
        this.mnTotalBytes = totalBytes;
    }

    public boolean isExecutable() {
        return this.mbExecutable;
    }

    public void setExecutable( boolean executable ) {
        this.mbExecutable = executable;
    }

    public List<String> getBlockers() {
        return this.mBlockers;
    }

    public void addBlocker( String blocker ) {
        this.mbExecutable = false;
        this.mBlockers.add( blocker );
    }

    public List<String> getWarnings() {
        return this.mWarnings;
    }

    public void addWarning( String warning ) {
        this.mWarnings.add( warning );
    }

    public List<UofsTransferRootPlan> getRootPlans() {
        return this.mRootPlans;
    }

    public void setRootPlans( List<UofsTransferRootPlan> rootPlans ) {
        this.mRootPlans.clear();
        if ( rootPlans != null ) {
            this.mRootPlans.addAll( rootPlans );
        }
    }
}
