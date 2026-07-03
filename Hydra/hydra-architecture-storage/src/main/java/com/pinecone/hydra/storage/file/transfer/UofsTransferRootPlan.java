package com.pinecone.hydra.storage.file.transfer;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class UofsTransferRootPlan implements Pinenut {
    protected String mszSourcePath;
    protected GUID mSourceGuid;
    protected UofsTransferSourceType mSourceType;
    protected String mszRequestedTargetPath;
    protected String mszFinalTargetPath;
    protected UofsTransferConflictPolicy mConflictPolicy;
    protected UofsTransferConflictAction mConflictAction;
    protected GUID mOverwrittenTargetGuid;
    protected UofsTransferSourceType mOverwrittenTargetType;
    protected UofsTransferMoveMode mMoveMode;

    public String getSourcePath() {
        return this.mszSourcePath;
    }

    public void setSourcePath( String sourcePath ) {
        this.mszSourcePath = sourcePath;
    }

    public GUID getSourceGuid() {
        return this.mSourceGuid;
    }

    public void setSourceGuid( GUID sourceGuid ) {
        this.mSourceGuid = sourceGuid;
    }

    public UofsTransferSourceType getSourceType() {
        return this.mSourceType;
    }

    public void setSourceType( UofsTransferSourceType sourceType ) {
        this.mSourceType = sourceType;
    }

    public String getRequestedTargetPath() {
        return this.mszRequestedTargetPath;
    }

    public void setRequestedTargetPath( String requestedTargetPath ) {
        this.mszRequestedTargetPath = requestedTargetPath;
    }

    public String getFinalTargetPath() {
        return this.mszFinalTargetPath;
    }

    public void setFinalTargetPath( String finalTargetPath ) {
        this.mszFinalTargetPath = finalTargetPath;
    }

    public UofsTransferConflictPolicy getConflictPolicy() {
        return this.mConflictPolicy;
    }

    public void setConflictPolicy( UofsTransferConflictPolicy conflictPolicy ) {
        this.mConflictPolicy = conflictPolicy;
    }

    public UofsTransferConflictAction getConflictAction() {
        return this.mConflictAction;
    }

    public void setConflictAction( UofsTransferConflictAction conflictAction ) {
        this.mConflictAction = conflictAction;
    }

    public GUID getOverwrittenTargetGuid() {
        return this.mOverwrittenTargetGuid;
    }

    public void setOverwrittenTargetGuid( GUID overwrittenTargetGuid ) {
        this.mOverwrittenTargetGuid = overwrittenTargetGuid;
    }

    public UofsTransferSourceType getOverwrittenTargetType() {
        return this.mOverwrittenTargetType;
    }

    public void setOverwrittenTargetType( UofsTransferSourceType overwrittenTargetType ) {
        this.mOverwrittenTargetType = overwrittenTargetType;
    }

    public UofsTransferMoveMode getMoveMode() {
        return this.mMoveMode;
    }

    public void setMoveMode( UofsTransferMoveMode moveMode ) {
        this.mMoveMode = moveMode;
    }
}
