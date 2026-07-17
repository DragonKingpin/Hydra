package com.acorn.redqueen.service.conduct;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class ServiceLegionaryJoinRequest implements Pinenut {

    protected GUID mServiceGuid;

    protected GUID mDeployGuid;

    protected String mszEndpointProtocol;

    protected String mszEndpointHost;

    protected Integer mEndpointPort;

    protected String mszEndpointPath;

    protected String mszEndpointAddress;

    protected String mszVersion;

    protected String mszZone;

    protected Integer mWeight;

    protected String mszMetadataJson;

    protected String mszRuntimeNodeId;

    protected String mszRuntimeNodeAlias;

    protected String mszRuntimeNodeMetadataJson;

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

    public String getRuntimeNodeId() {
        return this.mszRuntimeNodeId;
    }

    public void setRuntimeNodeId( String szRuntimeNodeId ) {
        this.mszRuntimeNodeId = szRuntimeNodeId;
    }

    public String getRuntimeNodeAlias() {
        return this.mszRuntimeNodeAlias;
    }

    public void setRuntimeNodeAlias( String szRuntimeNodeAlias ) {
        this.mszRuntimeNodeAlias = szRuntimeNodeAlias;
    }

    public String getRuntimeNodeMetadataJson() {
        return this.mszRuntimeNodeMetadataJson;
    }

    public void setRuntimeNodeMetadataJson( String szRuntimeNodeMetadataJson ) {
        this.mszRuntimeNodeMetadataJson = szRuntimeNodeMetadataJson;
    }

}
