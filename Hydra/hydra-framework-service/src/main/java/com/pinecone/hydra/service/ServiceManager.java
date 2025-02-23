package com.pinecone.hydra.service;

import java.util.Collection;

import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.system.ko.KernelObjectInstrument;

public interface ServiceManager extends KernelObjectInstrument {

    void registerService( ServiceInstance instance );

    Collection<ServiceInstance >  fetchServiceInstance( Long clientId );

    Collection<ServiceInstance >  fetchServiceInstance( Identification serviceId );

    Collection<ServiceInstance >  fetchServiceInstance( USII usii );



    ServiceInstance queryServiceInstance( Long clientId );

    ServiceInstance queryServiceInstance( USII usii );



    boolean hasOwnedService( Identification serviceId );

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


    Collection<ServiceInstance >  removeService ( Long clientId );

    Collection<ServiceInstance >  removeService( Identification serviceId );

    Collection<ServiceInstance >  removeService( USII usii );


    ServicesInstrument getServicesInstrument();

}
