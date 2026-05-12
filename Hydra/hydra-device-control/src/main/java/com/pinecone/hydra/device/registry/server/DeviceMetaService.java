package com.pinecone.hydra.device.registry.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ElementNode;
import com.pinecone.hydra.device.registry.DeviceValidationException;
import com.pinecone.hydra.device.registry.dto.DeviceMetaDTO;

public class DeviceMetaService implements Pinenut {

    protected final DeviceManager mDeviceManager;

    protected final DeviceInstrument mDeviceInstrument;

    protected final GuidAllocator mGuidAllocator;

    public DeviceMetaService( DeviceManager deviceManager ) {
        this.mDeviceManager = deviceManager;
        this.mDeviceInstrument = deviceManager.getDeviceInstrument();
        this.mGuidAllocator = this.mDeviceInstrument.getGuidAllocator();
    }

    public DeviceMetaDTO queryDeviceMetaByPath( String path ) {
        return DeviceMetaDTO.from( this.mDeviceManager.queryDeviceByPath( path ), this.mDeviceInstrument );
    }

    public DeviceMetaDTO queryDeviceMetaByGuid( String guid ) {
        if ( isBlank( guid ) ) {
            return null;
        }
        return DeviceMetaDTO.from( this.mDeviceManager.queryDeviceByGuid( this.mGuidAllocator.parse( guid ) ), this.mDeviceInstrument );
    }

    public boolean updateDeviceMetaByPath( String path, DeviceMetaDTO meta ) {
        ElementNode node = this.mDeviceManager.queryDeviceByPath( path );
        return this.updateDeviceMeta( node, meta );
    }

    public boolean updateDeviceMetaByGuid( String guid, DeviceMetaDTO meta ) {
        if ( isBlank( guid ) ) {
            return false;
        }
        ElementNode node = this.mDeviceManager.queryDeviceByGuid( this.mGuidAllocator.parse( guid ) );
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
        this.mDeviceManager.updateDevice( node );
        return true;
    }

    protected boolean isBlank( String value ) {
        return value == null || value.trim().isEmpty();
    }
}
