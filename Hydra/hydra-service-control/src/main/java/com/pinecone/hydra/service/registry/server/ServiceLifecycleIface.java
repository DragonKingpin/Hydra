package com.pinecone.hydra.service.registry.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.umct.stereotype.Iface;

@Iface
public interface ServiceLifecycleIface extends Pinenut {

    /**
     * @return with service-instance-guid
     */
    String registerService( RegisterServiceDTO serviceDTO );

    boolean createInstanceMeta( ServiceInstanceEntry serviceInstanceEntry );

    void deregisterServiceByClientId( Long clientId );

    void deregisterServiceByInstanceId( String instanceId );

    boolean hasOwnedServiceByServiceId( String serviceId );

    boolean hasOwnedServiceInstance( Long clientId );

    boolean hasOwnedServiceInstance( String instanceId );

    boolean hasOwnedServiceClient( Long clientId );

    Integer countRegisteredService();

}
