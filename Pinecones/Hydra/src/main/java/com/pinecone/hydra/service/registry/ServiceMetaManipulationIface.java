package com.pinecone.hydra.service.registry;


import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.umct.stereotype.Iface;

import java.util.Collection;

@Iface
public interface ServiceMetaManipulationIface {
    Collection<ServiceInstance> queryServiceInstanceByClientId(Long clientId );

    Collection<ServiceInstance > queryServiceInstanceByServiceId( Identification serviceId );

    Collection<ServiceInstance > queryServiceInstanceByUSII( USII usii );
}
