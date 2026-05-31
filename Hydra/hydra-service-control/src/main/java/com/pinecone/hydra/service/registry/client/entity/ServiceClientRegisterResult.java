package com.pinecone.hydra.service.registry.client.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class ServiceClientRegisterResult implements Pinenut {

    protected GUID mServiceGuid;

    protected GUID mInstanceGuid;

    protected String mszStatus;

    protected long mnRegisterTimeMillis;

    protected long mnExpireTimeMillis;

    public GUID getServiceGuid() {
        return this.mServiceGuid;
    }

    public void setServiceGuid( GUID serviceGuid ) {
        this.mServiceGuid = serviceGuid;
    }

    public GUID getInstanceGuid() {
        return this.mInstanceGuid;
    }

    public void setInstanceGuid( GUID instanceGuid ) {
        this.mInstanceGuid = instanceGuid;
    }

    public String getStatus() {
        return this.mszStatus;
    }

    public void setStatus( String szStatus ) {
        this.mszStatus = szStatus;
    }

    public long getRegisterTimeMillis() {
        return this.mnRegisterTimeMillis;
    }

    public void setRegisterTimeMillis( long nRegisterTimeMillis ) {
        this.mnRegisterTimeMillis = nRegisterTimeMillis;
    }

    public long getExpireTimeMillis() {
        return this.mnExpireTimeMillis;
    }

    public void setExpireTimeMillis( long nExpireTimeMillis ) {
        this.mnExpireTimeMillis = nExpireTimeMillis;
    }

}
