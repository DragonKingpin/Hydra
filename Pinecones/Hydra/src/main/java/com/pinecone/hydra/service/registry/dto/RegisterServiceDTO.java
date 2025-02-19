package com.pinecone.hydra.service.registry.dto;

public class RegisterServiceDTO {

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
