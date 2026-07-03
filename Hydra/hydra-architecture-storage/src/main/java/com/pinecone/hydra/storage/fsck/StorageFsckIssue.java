package com.pinecone.hydra.storage.fsck;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class StorageFsckIssue implements Pinenut {
    protected String domain;
    protected String severity;
    protected String code;
    protected String message;
    protected GUID guid;
    protected GUID fileGuid;
    protected GUID chunkGuid;
    protected GUID locationGuid;
    protected GUID volumeGuid;
    protected String objectKey;
    protected String path;
    protected String actualValue;
    protected String expectedValue;
    protected String actionType;
    protected boolean dangerous;

    public String getDomain() {
        return this.domain;
    }

    public void setDomain( String domain ) {
        this.domain = domain;
    }

    public String getSeverity() {
        return this.severity;
    }

    public void setSeverity( String severity ) {
        this.severity = severity;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode( String code ) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }

    public GUID getGuid() {
        return this.guid;
    }

    public void setGuid( GUID guid ) {
        this.guid = guid;
    }

    public GUID getFileGuid() {
        return this.fileGuid;
    }

    public void setFileGuid( GUID fileGuid ) {
        this.fileGuid = fileGuid;
    }

    public GUID getChunkGuid() {
        return this.chunkGuid;
    }

    public void setChunkGuid( GUID chunkGuid ) {
        this.chunkGuid = chunkGuid;
    }

    public GUID getLocationGuid() {
        return this.locationGuid;
    }

    public void setLocationGuid( GUID locationGuid ) {
        this.locationGuid = locationGuid;
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

    public String getPath() {
        return this.path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public String getActualValue() {
        return this.actualValue;
    }

    public void setActualValue( String actualValue ) {
        this.actualValue = actualValue;
    }

    public String getExpectedValue() {
        return this.expectedValue;
    }

    public void setExpectedValue( String expectedValue ) {
        this.expectedValue = expectedValue;
    }

    public String getActionType() {
        return this.actionType;
    }

    public void setActionType( String actionType ) {
        this.actionType = actionType;
    }

    public boolean isDangerous() {
        return this.dangerous;
    }

    public void setDangerous( boolean dangerous ) {
        this.dangerous = dangerous;
    }
}
