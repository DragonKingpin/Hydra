package com.pinecone.hydra.storage.repair;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class StorageRepairAction implements Pinenut {
    protected String actionType;
    protected String status;
    protected String message;
    protected String issueCode;
    protected GUID targetGuid;
    protected GUID volumeGuid;
    protected String objectKey;
    protected boolean dangerous;

    public String getActionType() {
        return this.actionType;
    }

    public void setActionType( String actionType ) {
        this.actionType = actionType;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }

    public String getIssueCode() {
        return this.issueCode;
    }

    public void setIssueCode( String issueCode ) {
        this.issueCode = issueCode;
    }

    public GUID getTargetGuid() {
        return this.targetGuid;
    }

    public void setTargetGuid( GUID targetGuid ) {
        this.targetGuid = targetGuid;
    }

    public GUID getVolumeGuid() {
        return this.volumeGuid;
    }

    public void setVolumeGuid( GUID volumeGuid ) {
        this.volumeGuid = volumeGuid;
    }

    public String getObjectKey() {
        return this.objectKey;
    }

    public void setObjectKey( String objectKey ) {
        this.objectKey = objectKey;
    }

    public boolean isDangerous() {
        return this.dangerous;
    }

    public void setDangerous( boolean dangerous ) {
        this.dangerous = dangerous;
    }
}
