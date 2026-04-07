package com.pinecone.hydra.service.registry.ulf;

import com.pinecone.hydra.service.kom.entity.GenericServiceInstanceEntity;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.ServiceInstanceCreationException;
import com.pinecone.hydra.service.registry.server.ServiceLifecycleService;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

@Controller
@AddressMapping("com.pinecone.hydra.service.registry.server.ServiceLifecycleIface.")
public class ServiceLifecycleController {

    protected ServiceLifecycleService serviceLifecycleService;

    public ServiceLifecycleController( ServiceManager serviceManager ) {
        this.serviceLifecycleService = serviceManager.serviceLifecycleService();
    }

    @AddressMapping( "registerService" )
    public String registerService( RegisterServiceDTO serviceDTO ) throws ClientServiceRegisterException {
        return this.serviceLifecycleService.registerService( serviceDTO );
    }

    @AddressMapping("createInstanceMeta")
    boolean createInstanceMeta( GenericServiceInstanceEntity instanceEntity ) throws ServiceInstanceCreationException {
        return this.serviceLifecycleService.createInstanceMeta( instanceEntity );
    }

    @AddressMapping("deregisterServiceByClientId")
    public void deregisterServiceByClientId( Long clientId ) {
        this.serviceLifecycleService.deregisterServiceByClientId( clientId );
    }

    @AddressMapping("deregisterServiceByInstanceId")
    public void deregisterServiceByInstanceId( String instanceId ) {
        this.serviceLifecycleService.deregisterServiceByInstanceId( instanceId );
    }

    @AddressMapping("hasOwnedServiceByServiceId")
    public boolean hasOwnedServiceByServiceId( String serviceId ) {
        return this.serviceLifecycleService.hasOwnedServiceByServiceId( serviceId );
    }

    @AddressMapping("hasOwnedServiceInstance")
    public boolean hasOwnedServiceInstance( Long clientId ) {
        return this.serviceLifecycleService.hasOwnedServiceInstance( clientId );
    }

    @AddressMapping("hasOwnedServiceClient")
    public boolean hasOwnedServiceClient( Long clientId ) {
        return this.serviceLifecycleService.hasOwnedServiceClient( clientId );
    }

    @AddressMapping("hasOwnedServiceClient")
    public boolean hasOwnedServiceInstance( String instanceId ) {
        return this.serviceLifecycleService.hasOwnedServiceInstance( instanceId );
    }

    @AddressMapping("countRegisteredService")
    public Integer countRegisteredService() {
        return this.serviceLifecycleService.countRegisteredService();
    }

}
