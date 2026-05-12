package com.pinecone.hydra.device.registry.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ElementNode;
import com.pinecone.hydra.device.registry.dto.DeviceRegistrationDTO;

public class DeviceLifecycleService implements Pinenut {

    protected final DeviceManager mDeviceManager;

    protected final DeviceInstrument mDeviceInstrument;

    protected final GuidAllocator mGuidAllocator;

    public DeviceLifecycleService( DeviceManager deviceManager ) {
        this.mDeviceManager = deviceManager;
        this.mDeviceInstrument = deviceManager.getDeviceInstrument();
        this.mGuidAllocator = this.mDeviceInstrument.getGuidAllocator();
    }

    public String enrollDevice( DeviceRegistrationDTO registrationDTO ) {
        GUID guid = this.mDeviceManager.enrollDevice( registrationDTO );
        return guid == null ? null : guid.toString();
    }

    public void dismissDeviceByGuid( String guid ) {
        if ( isNotBlank( guid ) ) {
            this.mDeviceManager.removeDevice( this.mGuidAllocator.parse( guid ) );
        }
    }

    public void dismissDeviceByPath( String path ) {
        if ( isNotBlank( path ) ) {
            ElementNode node = this.mDeviceManager.queryDeviceByPath( path );
            if ( node != null ) {
                this.mDeviceManager.removeDevice( node.getGuid() );
            }
        }
    }

    public boolean hasDeviceByGuid( String guid ) {
        return isNotBlank( guid ) && this.mDeviceInstrument.contains( this.mGuidAllocator.parse( guid ) );
    }

    public boolean hasDeviceByPath( String path ) {
        return isNotBlank( path ) && this.mDeviceManager.queryDeviceByPath( path ) != null;
    }

    protected boolean isNotBlank( String value ) {
        return value != null && !value.trim().isEmpty();
    }
}
