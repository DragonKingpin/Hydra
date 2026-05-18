package com.pinecone.hydra.device;

import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;
import com.pinecone.ulf.util.guid.GUIDs;

public abstract class ArchDeviceFamilyMeta implements DeviceFamilyMeta {
    protected GUID   guid;

    protected String name;

    protected String alias;

    protected String code;

    protected String extraInformation;

    protected String resourceType;

    protected String deviceType;

    protected String category;

    protected String classCode;

    protected String deploymentProfile;

    protected String topologyRole;

    protected String vendor;

    protected String model;

    protected String serialNumber;

    protected String ipAddress;

    protected String region;

    protected String zone;

    protected String location;

    protected String managementProtocol;

    protected String managementHost;

    protected Integer managementPort;

    protected String credentialRef;

    protected String status;

    protected String lifecycleStatus;

    protected boolean enabled = true;

    protected String tags;

    protected String resourceSummary;

    protected String szElementaryConfig;

    protected Map<String, Object > elementaryConfig;


    protected String description;

    public ArchDeviceFamilyMeta() {
    }

    public ArchDeviceFamilyMeta(Map<String, Object > joEntity ) {
        this.apply( joEntity );
    }

    protected ArchDeviceFamilyMeta apply(Map<String, Object > joEntity ) {
        String szGuid = (String) joEntity.get( "guid" );
        if( szGuid != null ) {
            this.guid = GUIDs.GUID128( (String) joEntity.get( "guid" ) );
        }
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );

        return this;
    }

    public GUID getGuid() {
        return this.guid;
    }

    @Override
    public Identification getId() {
        return this.getGuid();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getExtraInformation() {
        return this.extraInformation;
    }

    @Override
    public String getResourceType() {
        return this.resourceType;
    }

    @Override
    public String getDeviceType() {
        return this.deviceType;
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public String getClassCode() {
        return this.classCode;
    }

    @Override
    public String getDeploymentProfile() {
        return this.deploymentProfile;
    }

    @Override
    public String getTopologyRole() {
        return this.topologyRole;
    }

    @Override
    public String getVendor() {
        return this.vendor;
    }

    @Override
    public String getModel() {
        return this.model;
    }

    @Override
    public String getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Override
    public String getRegion() {
        return this.region;
    }

    @Override
    public String getZone() {
        return this.zone;
    }

    @Override
    public String getLocation() {
        return this.location;
    }

    @Override
    public String getManagementProtocol() {
        return this.managementProtocol;
    }

    @Override
    public String getManagementHost() {
        return this.managementHost;
    }

    @Override
    public Integer getManagementPort() {
        return this.managementPort;
    }

    @Override
    public String getCredentialRef() {
        return this.credentialRef;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public String getLifecycleStatus() {
        return this.lifecycleStatus;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getTags() {
        return this.tags;
    }

    @Override
    public String getResourceSummary() {
        return this.resourceSummary;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }
}
