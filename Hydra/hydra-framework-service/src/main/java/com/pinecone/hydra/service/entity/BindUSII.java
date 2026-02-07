package com.pinecone.hydra.service.entity;

import com.pinecone.framework.util.id.Identification;

public class BindUSII implements USII {
    protected Long clientId;

    protected Identification serviceId;

    protected Identification instanceId;

    public BindUSII( Long clientId, Identification serviceId, Identification instanceId ) {
        this.clientId  = clientId;
        this.serviceId = serviceId;
        this.instanceId = instanceId;
    }

    public BindUSII(){}

    @Override
    public Long getClientId() {
        return this.clientId;
    }

    @Override
    public Identification getServiceId() {
        return this.serviceId;
    }

    @Override
    public Identification getInstanceId() {
        return this.instanceId;
    }

    @Override
    public String getFullKey() {
        return this.serviceId + ":" + this.instanceId + ":" + this.clientId;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj instanceof USII) {
            USII USII = (USII) obj;
            return this.clientId.equals( USII.getClientId() ) && this.serviceId.equals( USII.getServiceId() );
        }
        else if ( obj instanceof Number ) {
            return this.clientId.equals( obj );
        }
        else if ( obj instanceof Identification ) {
            return this.serviceId.equals( obj );
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.clientId.hashCode() ^ this.serviceId.hashCode();
    }

    public static USII wrap(Long clientId, Identification serviceId, Identification instanceId ) {
        return new BindUSII( clientId, serviceId, instanceId );
    }
}
