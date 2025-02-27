package com.pinecone.hydra.service.registry;


import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;
import com.pinecone.hydra.umct.stereotype.Iface;

import java.util.List;

@Iface
public interface ServiceMetaManipulationIface extends Pinenut {
    List<ServiceMetaDTO> fetchServiceInsMetaByClientId( long clientId );

    List<ServiceMetaDTO> fetchServiceInsMetaByServiceId( String serviceId );
}
