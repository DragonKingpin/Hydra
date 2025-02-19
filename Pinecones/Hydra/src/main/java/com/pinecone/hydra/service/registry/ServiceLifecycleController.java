package com.pinecone.hydra.service.registry;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.ServiceManager;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.ulf.util.guid.GUIDs;

@Controller
@AddressMapping("com.pinecone.hydra.service.registry.ServiceLifecycleIface.")
public class ServiceLifecycleController {
    private ServiceManager      mServiceManager;

    private ServicesInstrument  mServicesInstrument;

    public ServiceLifecycleController( ServiceManager mServiceManager ){
        this.mServiceManager = mServiceManager;
        this.mServicesInstrument = mServiceManager.getServicesInstrument();
    }

    @AddressMapping("registerService")
    public void registerService( RegisterServiceDTO serviceDTO ) {
        Long clientId   = serviceDTO.getClientId();
        String szServId = serviceDTO.getServiceId();
        GUID serviceId  = GUIDs.GUID72( szServId );

        TreeNode node = this.mServicesInstrument.get( serviceId );
        ServiceElement serviceElement = (ServiceElement) node;
        WolfServiceInstance serviceInstance = new WolfServiceInstance( clientId, new UniformService( serviceId, serviceElement ) );

        this.mServiceManager.registerService( serviceInstance );
    }

    @AddressMapping("removeServiceByClientId")
    public void removeServiceByClientId( Long clientId ){
        this.mServiceManager.removeService( clientId );
    }

    @AddressMapping("removeServiceByServiceId")
    public void removeServiceByServiceId( Identification serviceId ){
        this.mServiceManager.removeService( serviceId );
    }

    @AddressMapping("removeServiceByUSII")
    public void removeServiceByUSII( USII usii ){
        this.mServiceManager.removeService( usii );
    }
}
