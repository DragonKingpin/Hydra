package com.pinecone.hydra.device.registry.server;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.registry.dto.DeviceMetaDTO;
import com.pinecone.hydra.device.registry.dto.DeviceTopologyDTO;
import com.pinecone.hydra.umct.stereotype.Iface;

@Iface
public interface DeviceTopologyManipulationIface extends Pinenut {

    boolean affirmOwnedRelation( DeviceTopologyDTO topologyDTO );

    List<DeviceMetaDTO> fetchChildrenMeta( String parentGuid );
}
