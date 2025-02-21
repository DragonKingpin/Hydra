package com.pinecone.hydra.service.registry;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.ServiceManager;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

@Controller
@AddressMapping("com.pinecone.hydra.service.registry.ServiceMetaManipulationIface.")
public class ServiceMetaController implements Pinenut {
    protected ServiceManager          mServiceManager;

    protected ServicesInstrument      mServicesInstrument;

    public ServiceMetaController( ServiceManager serviceManager ){
        this.mServiceManager = serviceManager;
        this.mServicesInstrument = serviceManager.getServicesInstrument();
    }


}
