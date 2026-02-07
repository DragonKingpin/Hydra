package com.pinecone.hydra.service.registry.dto;

import com.pinecone.framework.system.prototype.Pinenut;

public class RegisterServiceDTO implements Pinenut {

    protected Long clientId;

    protected String serviceId;

    protected String deployId;

    public RegisterServiceDTO() {

    }

    public RegisterServiceDTO( Long clientId, String serviceId, String deployId ) {
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.deployId = deployId;
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

    public String getDeployId() {
        return this.deployId;
    }

    public void setDeployId(String deployId) {
        this.deployId = deployId;
    }

}
