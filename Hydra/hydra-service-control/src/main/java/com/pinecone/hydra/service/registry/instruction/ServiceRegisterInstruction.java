package com.pinecone.hydra.service.registry.instruction;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class ServiceRegisterInstruction implements Pinenut {

    protected long mnClientId;

    protected GUID mServiceGuid;

    protected GUID mDeployGuid;

    protected GUID mInstanceGuid;

    protected String mszEndpointProtocol;

    protected String mszEndpointHost;

    protected Integer mEndpointPort;

    protected String mszEndpointPath;

    protected String mszEndpointAddress;

    protected String mszVersion;

    protected String mszZone;

    protected Integer mWeight;

    protected String mszMetadataJson;

    public long getClientId() {
        return this.mnClientId;
    }

    public void setClientId( long nClientId ) {
        this.mnClientId = nClientId;
    }

    public GUID getServiceGuid() {
        return this.mServiceGuid;
    }

    public void setServiceGuid( GUID serviceGuid ) {
        this.mServiceGuid = serviceGuid;
    }

    public GUID getDeployGuid() {
        return this.mDeployGuid;
    }

    public void setDeployGuid( GUID deployGuid ) {
        this.mDeployGuid = deployGuid;
    }

    public GUID getInstanceGuid() {
        return this.mInstanceGuid;
    }

    public void setInstanceGuid( GUID instanceGuid ) {
        this.mInstanceGuid = instanceGuid;
    }

    public String getEndpointProtocol() {
        return this.mszEndpointProtocol;
    }

    public void setEndpointProtocol( String szEndpointProtocol ) {
        this.mszEndpointProtocol = szEndpointProtocol;
    }

    public String getEndpointHost() {
        return this.mszEndpointHost;
    }

    public void setEndpointHost( String szEndpointHost ) {
        this.mszEndpointHost = szEndpointHost;
    }

    public Integer getEndpointPort() {
        return this.mEndpointPort;
    }

    public void setEndpointPort( Integer endpointPort ) {
        this.mEndpointPort = endpointPort;
    }

    public String getEndpointPath() {
        return this.mszEndpointPath;
    }

    public void setEndpointPath( String szEndpointPath ) {
        this.mszEndpointPath = szEndpointPath;
    }

    public String getEndpointAddress() {
        return this.mszEndpointAddress;
    }

    public void setEndpointAddress( String szEndpointAddress ) {
        this.mszEndpointAddress = szEndpointAddress;
    }

    public String getVersion() {
        return this.mszVersion;
    }

    public void setVersion( String szVersion ) {
        this.mszVersion = szVersion;
    }

    public String getZone() {
        return this.mszZone;
    }

    public void setZone( String szZone ) {
        this.mszZone = szZone;
    }

    public Integer getWeight() {
        return this.mWeight;
    }

    public void setWeight( Integer weight ) {
        this.mWeight = weight;
    }

    public String getMetadataJson() {
        return this.mszMetadataJson;
    }

    public void setMetadataJson( String szMetadataJson ) {
        this.mszMetadataJson = szMetadataJson;
    }

}
