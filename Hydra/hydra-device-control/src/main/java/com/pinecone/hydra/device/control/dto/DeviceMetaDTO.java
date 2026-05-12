package com.pinecone.hydra.device.control.dto;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.ElementNode;

public class DeviceMetaDTO {

    protected String guid;
    protected String metaGuid;
    protected String path;
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
    protected String description;
    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;

    public static DeviceMetaDTO from( ElementNode node, DeployInstrument deployInstrument ) {
        if ( node == null ) {
            return null;
        }

        DeviceMetaDTO dto = new DeviceMetaDTO();
        dto.setGuid( stringify( node.getGuid() ) );
        dto.setMetaGuid( stringify( node.getMetaGuid() ) );
        dto.setPath( node.getGuid() == null ? null : deployInstrument.getPath( node.getGuid() ) );
        dto.setName( node.getName() );
        dto.setAlias( node.getAlias() );
        dto.setExtraInformation( node.getExtraInformation() );
        dto.setResourceType( node.getResourceType() );
        dto.setDeviceType( node.getDeviceType() );
        dto.setVendor( node.getVendor() );
        dto.setModel( node.getModel() );
        dto.setSerialNumber( node.getSerialNumber() );
        dto.setIpAddress( node.getIpAddress() );
        dto.setStatus( node.getStatus() );
        dto.setDescription( node.getDescription() );
        dto.setCreateTime( node.getCreateTime() );
        dto.setUpdateTime( node.getUpdateTime() );
        return dto;
    }

    public void applyTo( ElementNode node, GuidAllocator guidAllocator ) {
        if ( node == null ) {
            return;
        }

        if ( isNotBlank( this.metaGuid ) ) {
            node.setMetaGuid( guidAllocator.parse( this.metaGuid ) );
        }
        if ( isNotBlank( this.name ) ) {
            node.setName( this.name );
        }
        if ( this.alias != null ) {
            node.setAlias( this.alias );
        }
        if ( this.extraInformation != null ) {
            node.setExtraInformation( this.extraInformation );
        }
        if ( this.resourceType != null ) {
            node.setResourceType( this.resourceType );
        }
        if ( this.deviceType != null ) {
            node.setDeviceType( this.deviceType );
        }
        if ( this.vendor != null ) {
            node.setVendor( this.vendor );
        }
        if ( this.model != null ) {
            node.setModel( this.model );
        }
        if ( this.serialNumber != null ) {
            node.setSerialNumber( this.serialNumber );
        }
        if ( this.ipAddress != null ) {
            node.setIpAddress( this.ipAddress );
        }
        if ( this.status != null ) {
            node.setStatus( this.status );
        }
        if ( this.description != null ) {
            node.setDescription( this.description );
        }
        if ( this.createTime != null ) {
            node.setCreateTime( this.createTime );
        }
        node.setUpdateTime( this.updateTime == null ? LocalDateTime.now() : this.updateTime );
    }

    protected static boolean isNotBlank( String value ) {
        return value != null && !value.trim().isEmpty();
    }

    protected static String stringify( GUID guid ) {
        return guid == null ? null : guid.toString();
    }

    public String getGuid() {
        return this.guid;
    }

    public void setGuid( String guid ) {
        this.guid = guid;
    }

    public String getMetaGuid() {
        return this.metaGuid;
    }

    public void setMetaGuid( String metaGuid ) {
        this.metaGuid = metaGuid;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public String getName() {
        return this.name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias( String alias ) {
        this.alias = alias;
    }

    public String getExtraInformation() {
        return this.extraInformation;
    }

    public void setExtraInformation( String extraInformation ) {
        this.extraInformation = extraInformation;
    }

    public String getResourceType() {
        return this.resourceType;
    }

    public void setResourceType( String resourceType ) {
        this.resourceType = resourceType;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType( String deviceType ) {
        this.deviceType = deviceType;
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

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber( String serialNumber ) {
        this.serialNumber = serialNumber;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress( String ipAddress ) {
        this.ipAddress = ipAddress;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime( LocalDateTime createTime ) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime( LocalDateTime updateTime ) {
        this.updateTime = updateTime;
    }
}
