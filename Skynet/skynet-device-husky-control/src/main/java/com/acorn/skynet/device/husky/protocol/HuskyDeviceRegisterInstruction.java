package com.acorn.skynet.device.husky.protocol;

import com.pinecone.framework.system.prototype.Pinenut;

public class HuskyDeviceRegisterInstruction implements Pinenut {

    protected String deviceGuid;

    protected String instanceGuid;

    protected String deviceGuidText;

    protected String devicePath;

    protected Long clientId;

    protected String endpointProtocol;

    protected String endpointHost;

    protected Integer endpointPort;

    protected String endpointPath;

    protected String endpointAddress;

    protected String metadataJson;

    public String getDeviceGuid() {
        return this.deviceGuid;
    }

    public void setDeviceGuid( String deviceGuid ) {
        this.deviceGuid = deviceGuid;
    }

    public String getInstanceGuid() {
        return this.instanceGuid;
    }

    public void setInstanceGuid( String instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }

    public String getDeviceGuidText() {
        return this.deviceGuidText;
    }

    public void setDeviceGuidText( String deviceGuidText ) {
        this.deviceGuidText = deviceGuidText;
    }

    public String getDevicePath() {
        return this.devicePath;
    }

    public void setDevicePath( String devicePath ) {
        this.devicePath = devicePath;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId( Long clientId ) {
        this.clientId = clientId;
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

    public String getMetadataJson() {
        return this.metadataJson;
    }

    public void setMetadataJson( String metadataJson ) {
        this.metadataJson = metadataJson;
    }
}
