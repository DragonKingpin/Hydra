package com.acorn.redqueen.service.purge;

import com.pinecone.framework.system.prototype.Pinenut;

public class PurgeBlocker implements Pinenut {
    protected String type;
    protected String targetGuid;
    protected String status;
    protected String message;

    public PurgeBlocker() {
    }

    public PurgeBlocker( String type, String targetGuid, String status, String message ) {
        this.type = type;
        this.targetGuid = targetGuid;
        this.status = status;
        this.message = message;
    }

    public String getType() {
        return this.type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public String getTargetGuid() {
        return this.targetGuid;
    }

    public void setTargetGuid( String targetGuid ) {
        this.targetGuid = targetGuid;
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
}
