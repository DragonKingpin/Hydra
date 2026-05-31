package com.pinecone.hydra.service.registry.client.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class ServiceClientDeregisterResult implements Pinenut {

    protected GUID mInstanceGuid;

    protected boolean mbSuccess;

    protected String mszReason;

    public GUID getInstanceGuid() {
        return this.mInstanceGuid;
    }

    public void setInstanceGuid( GUID instanceGuid ) {
        this.mInstanceGuid = instanceGuid;
    }

    public boolean isSuccess() {
        return this.mbSuccess;
    }

    public void setSuccess( boolean bSuccess ) {
        this.mbSuccess = bSuccess;
    }

    public String getReason() {
        return this.mszReason;
    }

    public void setReason( String szReason ) {
        this.mszReason = szReason;
    }

}
