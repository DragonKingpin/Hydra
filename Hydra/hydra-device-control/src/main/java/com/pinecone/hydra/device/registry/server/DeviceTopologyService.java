package com.pinecone.hydra.device.registry.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.ElementNode;
import com.pinecone.hydra.device.registry.dto.DeviceMetaDTO;
import com.pinecone.hydra.device.registry.dto.DeviceTopologyDTO;

public class DeviceTopologyService implements Pinenut {

    protected final DeviceControlManager mDeviceControlManager;

    protected final DeployInstrument mDeployInstrument;

    protected final GuidAllocator mGuidAllocator;

    public DeviceTopologyService( DeviceControlManager deviceControlManager ) {
        this.mDeviceControlManager = deviceControlManager;
        this.mDeployInstrument = deviceControlManager.getDeployInstrument();
        this.mGuidAllocator = this.mDeployInstrument.getGuidAllocator();
    }

    public boolean affirmOwnedRelation( DeviceTopologyDTO topologyDTO ) {
        if ( topologyDTO == null || isBlank( topologyDTO.getParentGuid() ) || isBlank( topologyDTO.getChildGuid() ) ) {
            return false;
        }

        GUID parentGuid = this.mGuidAllocator.parse( topologyDTO.getParentGuid() );
        GUID childGuid = this.mGuidAllocator.parse( topologyDTO.getChildGuid() );
        this.mDeployInstrument.affirmOwnedNode( parentGuid, childGuid );
        return true;
    }

    public List<DeviceMetaDTO> fetchChildrenMeta( String parentGuid ) {
        List<DeviceMetaDTO> children = new ArrayList<>();
        if ( isBlank( parentGuid ) ) {
            return children;
        }

        Collection<GUID> childGuids = this.mDeployInstrument.fetchChildrenGuids( this.mGuidAllocator.parse( parentGuid ) );
        for ( GUID childGuid : childGuids ) {
            ElementNode childNode = this.mDeviceControlManager.queryDeviceByGuid( childGuid );
            DeviceMetaDTO childMeta = DeviceMetaDTO.from( childNode, this.mDeployInstrument );
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
