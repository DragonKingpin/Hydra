package com.pinecone.hydra.device.kom.entity;

import java.util.Map;

import com.pinecone.hydra.device.DeviceExtraMeta;
import com.pinecone.hydra.device.kom.DeviceInstrument;

public class GenericDeviceElement extends ArchElementNode implements DeviceElement {
    protected String genericDevTypeCode;
    protected String schemaGuid;
    protected String schemaDataJson;
    protected DeviceExtraMeta extraMeta;

    public GenericDeviceElement() {
        super();
    }

    public GenericDeviceElement( DeviceInstrument deviceInstrument ) {
        super( deviceInstrument );
    }

    public GenericDeviceElement( Map<String, Object> joEntity ) {
        super( joEntity );
    }

    public GenericDeviceElement( Map<String, Object> joEntity, DeviceInstrument deviceInstrument ) {
        super( joEntity, deviceInstrument );
    }

    @Override
    public boolean isEnable() {
        return this.enabled;
    }

    @Override
    public void setEnable( boolean enable ) {
        this.enabled = enable;
    }

    @Override
    public DeviceExtraMeta getExtraMeta() {
        return this.extraMeta;
    }

    @Override
    public void setExtraMeta( DeviceExtraMeta extraMeta ) {
        this.extraMeta = extraMeta;
    }

    @Override
    public DeviceElement getAffiliateDevicement() {
        return null;
    }

    @Override
    public void addChild( ElementNode child ) {
        super.addChild( child );
    }

    public String getGenericDevTypeCode() {
        return this.genericDevTypeCode;
    }

    public void setGenericDevTypeCode( String genericDevTypeCode ) {
        this.genericDevTypeCode = genericDevTypeCode;
    }

    public String getSchemaGuid() {
        return this.schemaGuid;
    }

    public void setSchemaGuid( String schemaGuid ) {
        this.schemaGuid = schemaGuid;
    }

    public String getSchemaDataJson() {
        return this.schemaDataJson;
    }

    public void setSchemaDataJson( String schemaDataJson ) {
        this.schemaDataJson = schemaDataJson;
    }
}
