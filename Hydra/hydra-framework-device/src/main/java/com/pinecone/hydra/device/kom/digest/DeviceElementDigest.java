package com.pinecone.hydra.device.kom.digest;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class DeviceElementDigest implements Pinenut {

    protected GUID guid;

    protected String name;

    protected String path;

    protected String objectCategoryName;

    protected String className;

    protected String alias;

    protected String code;

    protected String category;

    protected String deviceType;

    protected String classCode;

    protected String deploymentProfile;

    protected String topologyRole;

    protected String ipAddress;

    protected String region;

    protected String zone;

    protected String location;

    protected String vendor;

    protected String model;

    protected String tags;

    protected String resourceSummary;

    protected String status;

    protected String lifecycleStatus;

    protected String genericDevTypeCode;

    protected GUID schemaGuid;

    protected String schemaDataJson;

    protected boolean enabled;

    public GUID getGuid() {
        return this.guid;
    }

    public void setGuid( GUID guid ) {
        this.guid = guid;
    }

    public String getName() {
        return this.name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public String getObjectCategoryName() {
        return this.objectCategoryName;
    }

    public void setObjectCategoryName( String objectCategoryName ) {
        this.objectCategoryName = objectCategoryName;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName( String className ) {
        this.className = className;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias( String alias ) {
        this.alias = alias;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode( String code ) {
        this.code = code;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory( String category ) {
        this.category = category;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType( String deviceType ) {
        this.deviceType = deviceType;
    }

    public String getClassCode() {
        return this.classCode;
    }

    public void setClassCode( String classCode ) {
        this.classCode = classCode;
    }

    public String getDeploymentProfile() {
        return this.deploymentProfile;
    }

    public void setDeploymentProfile( String deploymentProfile ) {
        this.deploymentProfile = deploymentProfile;
    }

    public String getTopologyRole() {
        return this.topologyRole;
    }

    public void setTopologyRole( String topologyRole ) {
        this.topologyRole = topologyRole;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress( String ipAddress ) {
        this.ipAddress = ipAddress;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion( String region ) {
        this.region = region;
    }

    public String getZone() {
        return this.zone;
    }

    public void setZone( String zone ) {
        this.zone = zone;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation( String location ) {
        this.location = location;
    }

    public String getVendor() {
        return this.vendor;
    }

    public void setVendor( String vendor ) {
        this.vendor = vendor;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel( String model ) {
        this.model = model;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags( String tags ) {
        this.tags = tags;
    }

    public String getResourceSummary() {
        return this.resourceSummary;
    }

    public void setResourceSummary( String resourceSummary ) {
        this.resourceSummary = resourceSummary;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    public String getLifecycleStatus() {
        return this.lifecycleStatus;
    }

    public void setLifecycleStatus( String lifecycleStatus ) {
        this.lifecycleStatus = lifecycleStatus;
    }

    public String getGenericDevTypeCode() {
        return this.genericDevTypeCode;
    }

    public void setGenericDevTypeCode( String genericDevTypeCode ) {
        this.genericDevTypeCode = genericDevTypeCode;
    }

    public GUID getSchemaGuid() {
        return this.schemaGuid;
    }

    public void setSchemaGuid( GUID schemaGuid ) {
        this.schemaGuid = schemaGuid;
    }

    public String getSchemaDataJson() {
        return this.schemaDataJson;
    }

    public void setSchemaDataJson( String schemaDataJson ) {
        this.schemaDataJson = schemaDataJson;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }
}
