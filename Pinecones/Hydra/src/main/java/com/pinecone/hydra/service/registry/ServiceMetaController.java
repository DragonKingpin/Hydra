package com.pinecone.hydra.service.registry;

import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.ServiceManager;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collection;

@Controller
@AddressMapping("com.pinecone.hydra.service.registry.ServiceMetaManipulationIface.")
public class ServiceMetaController {
    private ServiceManager          mServiceManager;

    private ServicesInstrument      mServicesInstrument;

    public ServiceMetaController( ServiceManager serviceManager ){
        this.mServiceManager = serviceManager;
        this.mServicesInstrument = serviceManager.getServicesInstrument();
    }

    @AddressMapping("queryServiceInstanceByClientId")
    public ArrayList<ServiceInstance> queryServiceInstanceByClientId(Long clientId ) {
        return this.mServiceManager.queryServiceInstance( clientId );
    }

    @AddressMapping("queryServiceInstanceByServiceId")
    public Collection<ServiceInstance > queryServiceInstanceByServiceId( Identification serviceId ) {
        return this.mServiceManager.queryServiceInstance( serviceId );
    }

    @AddressMapping("queryServiceInstanceByUSII")
    public Collection<ServiceInstance > queryServiceInstanceByUSII( USII usii ){
        return this.mServiceManager.queryServiceInstance( usii );
    }

}
