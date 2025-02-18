package com.pinecone.hydra.service;

import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.system.ko.KernelObjectInstrument;

public interface ServiceManager extends KernelObjectInstrument {

    void registerService( ServiceInstance instance );


    ServiceInstance queryServiceInstance( Long clientId );

    ServiceInstance queryServiceInstance( Identification serviceId );

    ServiceInstance queryServiceInstance( USII usii );


    ServiceInstance removeService( Long clientId );

    ServiceInstance removeService( Identification serviceId );

    ServiceInstance removeService( USII usii );

}
