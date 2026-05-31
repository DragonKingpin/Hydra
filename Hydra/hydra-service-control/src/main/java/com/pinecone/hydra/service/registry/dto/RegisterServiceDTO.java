package com.pinecone.hydra.service.registry.dto;

import com.pinecone.framework.system.prototype.Pinenut;

public class RegisterServiceDTO implements Pinenut {

    protected Long clientId;

    protected String serviceId;

    protected String deployId;

    protected String transportType;

    protected String endpointProtocol;

    protected String endpointHost;

    protected Integer endpointPort;

    protected String endpointPath;

    protected String endpointAddress;

    protected String version;

    protected String zone;

    protected Integer weight;

    protected String metadataJson;

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

    public String getTransportType() {
        return this.transportType;
    }

    public void setTransportType( String transportType ) {
        this.transportType = transportType;
    }

    public String getEndpointProtocol() {
        return this.endpointProtocol;
    }

    public void setEndpointProtocol( String endpointProtocol ) {
        this.endpointProtocol = endpointProtocol;
    }

    public String getEndpointHost() {
        return this.endpointHost;
    }

    public void setEndpointHost( String endpointHost ) {
        this.endpointHost = endpointHost;
    }

    public Integer getEndpointPort() {
        return this.endpointPort;
    }

    public void setEndpointPort( Integer endpointPort ) {
        this.endpointPort = endpointPort;
    }

    public String getEndpointPath() {
        return this.endpointPath;
    }

    public void setEndpointPath( String endpointPath ) {
        this.endpointPath = endpointPath;
    }

    public String getEndpointAddress() {
        return this.endpointAddress;
    }

    public void setEndpointAddress( String endpointAddress ) {
        this.endpointAddress = endpointAddress;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion( String version ) {
        this.version = version;
    }

    public String getZone() {
        return this.zone;
    }

    public void setZone( String zone ) {
        this.zone = zone;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public void setWeight( Integer weight ) {
        this.weight = weight;
    }

    public String getMetadataJson() {
        return this.metadataJson;
    }

    public void setMetadataJson( String metadataJson ) {
        this.metadataJson = metadataJson;
    }

}
