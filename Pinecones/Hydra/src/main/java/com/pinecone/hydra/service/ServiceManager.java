package com.pinecone.hydra.service;

import java.util.Collection;

import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.system.ko.KernelObjectInstrument;

public interface ServiceManager extends KernelObjectInstrument {

    void registerService( ServiceInstance instance );


    Collection<ServiceInstance >  queryServiceInstance( Long clientId );

    Collection<ServiceInstance >  queryServiceInstance( Identification serviceId );

    Collection<ServiceInstance >  queryServiceInstance( USII usii );

    default ServiceInstance queryFirstInstance( Long clientId ) {
        Collection<ServiceInstance > instances = this.queryServiceInstance( clientId );
        if ( !instances.isEmpty() ) {
            return instances.iterator().next();
        }
        return null;
    }

    default ServiceInstance queryFirstInstance( Identification serviceId ) {
        Collection<ServiceInstance > instances = this.queryServiceInstance( serviceId );
        if ( !instances.isEmpty() ) {
            return instances.iterator().next();
        }
        return null;
    }

    default ServiceInstance queryFirstInstance( USII usii ) {
        Collection<ServiceInstance > instances = this.queryServiceInstance( usii );
        if ( !instances.isEmpty() ) {
            return instances.iterator().next();
        }
        return null;
    }


    Collection<ServiceInstance > removeService ( Long clientId );

    Collection<ServiceInstance >  removeService( Identification serviceId );

    Collection<ServiceInstance >  removeService( USII usii );

    ServicesInstrument getServicesInstrument();

}
