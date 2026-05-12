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

    protected String extraInformation;

    protected String resourceType;

    protected String deviceType;

    protected String vendor;

    protected String model;

    protected String serialNumber;

    protected String ipAddress;

    protected String status;

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
    public String getStatus() {
        return this.status;
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
