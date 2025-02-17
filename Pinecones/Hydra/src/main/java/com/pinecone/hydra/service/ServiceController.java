package com.pinecone.hydra.service;

import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.registry.GenericServiceControlBlock;
import com.pinecone.hydra.service.registry.UniformServicesManager;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

@Controller
@AddressMapping( "com.pinecone.hydra.service.ServiceControllerIfce." )
public class ServiceController {
    private UniformServicesManager servicesManager;


    public void serviceRegister(long clientId, ServiceElement serviceElement){
        GenericServiceControlBlock controlBlock = new GenericServiceControlBlock( clientId, serviceElement );

        servicesManager.registryService( clientId, controlBlock );
    }
}
