package com.pinecone.hydra.service.registry;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

@Controller
@AddressMapping( "com.pinecone.hydra.service.registry.ServiceLifecycleIface." )
public class ServiceLifecycle implements Pinenut {
    private UniformServiceManager servicesManager;


    public void serviceRegister( long clientId, ServiceElement serviceElement ){
        //GenericServiceInstance controlBlock = new GenericServiceInstance( clientId, serviceElement );

        //servicesManager.registryService( clientId, controlBlock );
    }
}
