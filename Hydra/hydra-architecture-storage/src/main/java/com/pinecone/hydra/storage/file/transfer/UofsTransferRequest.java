package com.pinecone.hydra.storage.file.transfer;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.ArrayList;
import java.util.List;

public class UofsTransferRequest implements Pinenut {
    protected UofsTransferOperation      mOperation;
    protected String                     mszSourcePath;
    protected final List<String>         mSourcePaths = new ArrayList<>();
    protected String                     mszTargetPath;
    protected UofsTransferTargetMode     mTargetMode;
    protected UofsTransferConflictPolicy mConflictPolicy;
    protected UofsTransferLinkPolicy     mLinkPolicy;
    protected UofsTransferDirectoryPolicy mDirectoryPolicy;
    protected UofsTransferItemTrackingMode mItemTrackingMode;
    protected UofsTransferSourceCleanupPolicy mSourceCleanupPolicy;
    protected GUID                       mOperatorGuid;
    protected String                     mszExtConfig;

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

    public UofsTransferConflictPolicy getConflictPolicy() {
        return this.mConflictPolicy;
    }

    public void setConflictPolicy( UofsTransferConflictPolicy conflictPolicy ) {
        this.mConflictPolicy = conflictPolicy;
    }

    public UofsTransferLinkPolicy getLinkPolicy() {
        return this.mLinkPolicy;
    }

    public void setLinkPolicy( UofsTransferLinkPolicy linkPolicy ) {
        this.mLinkPolicy = linkPolicy;
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

    public UofsTransferSourceCleanupPolicy getSourceCleanupPolicy() {
        return this.mSourceCleanupPolicy;
    }

    public void setSourceCleanupPolicy( UofsTransferSourceCleanupPolicy sourceCleanupPolicy ) {
        this.mSourceCleanupPolicy = sourceCleanupPolicy;
    }

    public GUID getOperatorGuid() {
        return this.mOperatorGuid;
    }

    public void setOperatorGuid( GUID operatorGuid ) {
        this.mOperatorGuid = operatorGuid;
    }

    public String getExtConfig() {
        return this.mszExtConfig;
    }

    public void setExtConfig( String extConfig ) {
        this.mszExtConfig = extConfig;
    }
}
