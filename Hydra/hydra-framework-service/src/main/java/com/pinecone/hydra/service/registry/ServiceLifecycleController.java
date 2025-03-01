package com.pinecone.hydra.service.registry;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.ServiceManager;
import com.pinecone.hydra.service.entity.BindUSII;
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

    @AddressMapping("deregisterServiceByClientId")
    public void deregisterServiceByClientId( Long clientId ){
        this.mServiceManager.removeService( clientId );
    }

    @AddressMapping("deregisterServiceByServiceId")
    public void deregisterServiceByServiceId( String serviceId ){
        this.mServiceManager.removeService( GUIDs.GUID72( serviceId ) );
    }

    @AddressMapping("deregisterServiceByUSII")
    public void deregisterServiceByUSII( BindUSII usii ){
        this.mServiceManager.removeService( usii );
    }

    @AddressMapping("hasOwnedServiceByUSII")
    public boolean hasOwnedServiceByUSII( BindUSII usii ){
        return this.mServiceManager.hasOwnedService( usii );
    }

    @AddressMapping("hasOwnedServiceByServiceId")
    public boolean hasOwnedServiceByServiceId( String serviceId ){
        return this.mServiceManager.hasOwnedService( GUIDs.GUID72( serviceId ) );
    }

    @AddressMapping("hasOwnedServiceInstance")
    public boolean hasOwnedServiceInstance( Long clientId ){
        return this.mServiceManager.hasOwnedServiceInstance( clientId );
    }

    @AddressMapping("hasOwnedServiceClient")
    public boolean hasOwnedServiceClient( Long clientId ){
        return this.mServiceManager.hasOwnedServiceClient( clientId );
    }

    @AddressMapping("countRegisteredService")
    public Integer countRegisteredService(){
        return this.mServiceManager.countRegisteredService();
    }
}
