package com.pinecone.hydra.service.registry;

import java.util.Collection;

import com.pinecone.framework.system.regime.arch.Manager;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.Service;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.registry.event.ServiceRegisterEventHandler;
import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.pinecone.hydra.system.ko.KernelObjectInstrument;

public interface ServiceManager extends Manager, Slf4jTraceable {

    void startService () throws ServiceControlRPCException;

    void registerServiceInstance( ServiceInstance instance );

    GUID registerService( Long clientId, GUID serviceId, GUID deployGuid ) throws ClientServiceRegisterException;

    void destroyServiceInstance( GUID serviceId, GUID instanceGuid );

    Collection<ServiceInstance >  fetchServiceInstance( Long clientId );

    Collection<ServiceInstance >  fetchServiceInstance( Identification serviceId );

    Collection<ServiceInstance >  fetchServiceInstanceByIId( Identification instanceId );

    Collection<ServiceInstance >  fetchServiceInstance( USII usii );



    ServiceInstance queryServiceInstance( Long clientId );

    ServiceInstance queryServiceInstance( USII usii );



    boolean hasOwnedService( Identification serviceId );

    boolean hasOwnedInstance( Identification instanceId );

    boolean hasOwnedService( USII usii );

    boolean hasOwnedServiceInstance( Long clientId );

    boolean hasOwnedServiceClient( Long clientId );



    default ServiceInstance queryFirstInstance( Long clientId ) {
        Collection<ServiceInstance > instances = this.fetchServiceInstance( clientId );
        if ( !instances.isEmpty() ) {
            return instances.iterator().next();
        }
        return null;
    }

    default ServiceInstance queryFirstInstance( Identification serviceId ) {
        Collection<ServiceInstance > instances = this.fetchServiceInstance( serviceId );
        if ( !instances.isEmpty() ) {
            return instances.iterator().next();
        }
        return null;
    }

    default ServiceInstance queryFirstInstance( USII usii ) {
        Collection<ServiceInstance > instances = this.fetchServiceInstance( usii );
        if ( !instances.isEmpty() ) {
            return instances.iterator().next();
        }
        return null;
    }



    ServiceInstance getInstance( Identification instanceId ) ;

    Collection<ServiceInstance >  deregisterServiceInstance ( Long clientId );

    Collection<ServiceInstance > deregisterServiceInstance( Identification instanceId );

    Collection<ServiceInstance >  deregisterService( Identification serviceId );



    ServiceInstrument getServicesInstrument();

    int countRegisteredService();



    void addRegisterEventHandler( ServiceRegisterEventHandler handler ) ;

    void removeRegisterEventHandler( ServiceRegisterEventHandler handler ) ;

    int registerEventHandlerSize(  ) ;
}
