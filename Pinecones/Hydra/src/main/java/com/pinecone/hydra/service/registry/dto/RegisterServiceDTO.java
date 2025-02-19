package com.pinecone.hydra.service.registry.dto;

import com.pinecone.framework.system.prototype.Pinenut;

public class RegisterServiceDTO implements Pinenut {

    protected Long clientId;

    protected String serviceId;

    public RegisterServiceDTO() {

    }

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId( Long clientId ) {
        this.clientId = clientId;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public void setServiceId( String serviceId ) {
        this.serviceId = serviceId;
    }
}
