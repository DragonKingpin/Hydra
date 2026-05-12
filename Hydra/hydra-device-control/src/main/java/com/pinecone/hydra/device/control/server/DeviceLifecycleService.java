package com.pinecone.hydra.device.control.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.ElementNode;
import com.pinecone.hydra.device.control.dto.DeviceRegistrationDTO;

public class DeviceLifecycleService implements Pinenut {

    protected final DeviceControlManager mDeviceControlManager;

    protected final DeployInstrument mDeployInstrument;

    protected final GuidAllocator mGuidAllocator;

    public DeviceLifecycleService( DeviceControlManager deviceControlManager ) {
        this.mDeviceControlManager = deviceControlManager;
        this.mDeployInstrument = deviceControlManager.getDeployInstrument();
        this.mGuidAllocator = this.mDeployInstrument.getGuidAllocator();
    }

    public String registerDevice( DeviceRegistrationDTO registrationDTO ) {
        GUID guid = this.mDeviceControlManager.registerDevice( registrationDTO );
        return guid == null ? null : guid.toString();
    }

    public void deregisterDeviceByGuid( String guid ) {
        if ( isNotBlank( guid ) ) {
            this.mDeviceControlManager.removeDevice( this.mGuidAllocator.parse( guid ) );
        }
    }

    public void deregisterDeviceByPath( String path ) {
        if ( isNotBlank( path ) ) {
            ElementNode node = this.mDeviceControlManager.queryDeviceByPath( path );
            if ( node != null ) {
                this.mDeviceControlManager.removeDevice( node.getGuid() );
            }
        }
    }

    public boolean hasDeviceByGuid( String guid ) {
        return isNotBlank( guid ) && this.mDeployInstrument.contains( this.mGuidAllocator.parse( guid ) );
    }

    public boolean hasDeviceByPath( String path ) {
        return isNotBlank( path ) && this.mDeviceControlManager.queryDeviceByPath( path ) != null;
    }

    protected boolean isNotBlank( String value ) {
        return value != null && !value.trim().isEmpty();
    }
}
