package com.pinecone.hydra.service.registry;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.umct.stereotype.Iface;

@Iface
public interface ServiceLifecycleIface extends Pinenut {
    void registerService( RegisterServiceDTO serviceDTO );

    void removeServiceByClientId(Long clientId );

    void removeServiceByServiceId(Identification serviceId );

    void removeServiceByUSII(USII usii);
}
