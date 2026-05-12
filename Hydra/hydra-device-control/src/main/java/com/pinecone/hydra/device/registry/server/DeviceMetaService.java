package com.pinecone.hydra.device.registry.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.ElementNode;
import com.pinecone.hydra.device.registry.DeviceValidationException;
import com.pinecone.hydra.device.registry.dto.DeviceMetaDTO;

public class DeviceMetaService implements Pinenut {

    protected final DeviceControlManager mDeviceControlManager;

    protected final DeployInstrument mDeployInstrument;

    protected final GuidAllocator mGuidAllocator;

    public DeviceMetaService( DeviceControlManager deviceControlManager ) {
        this.mDeviceControlManager = deviceControlManager;
        this.mDeployInstrument = deviceControlManager.getDeployInstrument();
        this.mGuidAllocator = this.mDeployInstrument.getGuidAllocator();
    }

    public DeviceMetaDTO queryDeviceMetaByPath( String path ) {
        return DeviceMetaDTO.from( this.mDeviceControlManager.queryDeviceByPath( path ), this.mDeployInstrument );
    }

    public DeviceMetaDTO queryDeviceMetaByGuid( String guid ) {
        if ( isBlank( guid ) ) {
            return null;
        }
        return DeviceMetaDTO.from( this.mDeviceControlManager.queryDeviceByGuid( this.mGuidAllocator.parse( guid ) ), this.mDeployInstrument );
    }

    public boolean updateDeviceMetaByPath( String path, DeviceMetaDTO meta ) {
        ElementNode node = this.mDeviceControlManager.queryDeviceByPath( path );
        return this.updateDeviceMeta( node, meta );
    }

    public boolean updateDeviceMetaByGuid( String guid, DeviceMetaDTO meta ) {
        if ( isBlank( guid ) ) {
            return false;
        }
        ElementNode node = this.mDeviceControlManager.queryDeviceByGuid( this.mGuidAllocator.parse( guid ) );
        return this.updateDeviceMeta( node, meta );
    }

    protected boolean updateDeviceMeta( ElementNode node, DeviceMetaDTO meta ) {
        if ( node == null ) {
            return false;
        }
        if ( meta == null ) {
            throw new DeviceValidationException( "Device meta is required." );
        }

        meta.applyTo( node, this.mGuidAllocator );
        this.mDeviceControlManager.updateDevice( node );
        return true;
    }

    protected boolean isBlank( String value ) {
        return value == null || value.trim().isEmpty();
    }
}
