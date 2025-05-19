package com.pinecone.hydra.service.registry;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.entity.BindUSII;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.umct.stereotype.Iface;

@Iface
public interface ServiceLifecycleIface extends Pinenut {

    void registerService( RegisterServiceDTO serviceDTO );

    void deregisterServiceByClientId( Long clientId );

    void deregisterServiceByServiceId( String serviceId );

    void deregisterServiceByUSII( BindUSII usii);

    boolean hasOwnedServiceByUSII( BindUSII usii );

    boolean hasOwnedServiceByServiceId( String serviceId );

    boolean hasOwnedServiceInstance( Long clientId );

    boolean hasOwnedServiceClient( Long clientId );

    Integer countRegisteredService();

}
