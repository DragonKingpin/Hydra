package com.acorn.skynet.device.conduct;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.registry.instruction.DeviceRegisterInstruction;

public class DeviceLegionaryJoinRequest implements Pinenut {

    protected GUID deviceGuid;

    protected String deviceGuidText;

    protected String devicePath;

    protected String endpointProtocol;

    protected String endpointHost;

    protected Integer endpointPort;

    protected String endpointPath;

    protected String endpointAddress;

    protected String metadataJson;

    public GUID getDeviceGuid() {
        return this.deviceGuid;
    }

    public void setDeviceGuid( GUID deviceGuid ) {
        this.deviceGuid = deviceGuid;
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

    public DeviceRegisterInstruction toDeviceRegisterInstruction( long clientId ) {
        DeviceRegisterInstruction instruction = new DeviceRegisterInstruction();
        instruction.setDeviceGuid( this.deviceGuid );
        instruction.setDeviceGuidText( this.deviceGuidText );
        instruction.setDevicePath( this.devicePath );
        instruction.setClientId( clientId );
        instruction.setEndpointProtocol( this.endpointProtocol );
        instruction.setEndpointHost( this.endpointHost );
        instruction.setEndpointPort( this.endpointPort );
        instruction.setEndpointPath( this.endpointPath );
        instruction.setEndpointAddress( this.endpointAddress );
        instruction.setMetadataJson( this.metadataJson );
        return instruction;
    }
}
