package com.pinecone.hydra.device.registry.ulf;

import java.util.List;

import com.pinecone.hydra.device.registry.dto.DeviceMetaDTO;
import com.pinecone.hydra.device.registry.dto.DeviceTopologyDTO;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.device.registry.server.DeviceTopologyService;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

@Controller
@AddressMapping( "com.pinecone.hydra.device.registry.server.DeviceTopologyManipulationIface." )
public class DeviceTopologyController implements DeviceRPCController {

    protected DeviceTopologyService deviceTopologyService;

    public DeviceTopologyController( DeviceManager deviceManager ) {
        this.deviceTopologyService = deviceManager.deviceTopologyService();
    }

    @AddressMapping( "affirmOwnedRelation" )
    public boolean affirmOwnedRelation( DeviceTopologyDTO topologyDTO ) {
        return this.deviceTopologyService.affirmOwnedRelation( topologyDTO );
    }

    @AddressMapping( "fetchChildrenMeta" )
    public List<DeviceMetaDTO> fetchChildrenMeta( String parentGuid ) {
        return this.deviceTopologyService.fetchChildrenMeta( parentGuid );
    }
}
