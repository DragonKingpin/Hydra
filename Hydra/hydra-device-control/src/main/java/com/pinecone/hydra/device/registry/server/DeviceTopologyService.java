package com.pinecone.hydra.device.registry.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ElementNode;
import com.pinecone.hydra.device.registry.dto.DeviceMetaDTO;
import com.pinecone.hydra.device.registry.dto.DeviceTopologyDTO;

public class DeviceTopologyService implements Pinenut {

    protected final DeviceManager mDeviceManager;

    protected final DeviceInstrument mDeviceInstrument;

    protected final GuidAllocator mGuidAllocator;

    public DeviceTopologyService( DeviceManager deviceManager ) {
        this.mDeviceManager = deviceManager;
        this.mDeviceInstrument = deviceManager.getDeviceInstrument();
        this.mGuidAllocator = this.mDeviceInstrument.getGuidAllocator();
    }

    public boolean affirmOwnedRelation( DeviceTopologyDTO topologyDTO ) {
        if ( topologyDTO == null || isBlank( topologyDTO.getParentGuid() ) || isBlank( topologyDTO.getChildGuid() ) ) {
            return false;
        }

        GUID parentGuid = this.mGuidAllocator.parse( topologyDTO.getParentGuid() );
        GUID childGuid = this.mGuidAllocator.parse( topologyDTO.getChildGuid() );
        this.mDeviceInstrument.affirmOwnedNode( parentGuid, childGuid );
        return true;
    }

    public List<DeviceMetaDTO> fetchChildrenMeta( String parentGuid ) {
        List<DeviceMetaDTO> children = new ArrayList<>();
        if ( isBlank( parentGuid ) ) {
            return children;
        }

        Collection<GUID> childGuids = this.mDeviceInstrument.fetchChildrenGuids( this.mGuidAllocator.parse( parentGuid ) );
        for ( GUID childGuid : childGuids ) {
            ElementNode childNode = this.mDeviceManager.queryDeviceByGuid( childGuid );
            DeviceMetaDTO childMeta = DeviceMetaDTO.from( childNode, this.mDeviceInstrument );
            if ( childMeta != null ) {
                children.add( childMeta );
            }
        }
        return children;
    }

    protected boolean isBlank( String value ) {
        return value == null || value.trim().isEmpty();
    }
}
