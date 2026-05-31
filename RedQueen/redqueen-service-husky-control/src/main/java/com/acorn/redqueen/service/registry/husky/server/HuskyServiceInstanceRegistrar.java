package com.acorn.redqueen.service.registry.husky.server;

import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.server.registrar.ServiceInstanceRegistrar;

public class HuskyServiceInstanceRegistrar implements ServiceInstanceRegistrar {

    protected ServiceManager mServiceManager;

    public HuskyServiceInstanceRegistrar( ServiceManager serviceManager ) {
        this.mServiceManager = serviceManager;
    }

    @Override
    public String registerService( RegisterServiceDTO serviceDTO ) throws ClientServiceRegisterException {
        return this.mServiceManager.serviceLifecycleService().registerService( serviceDTO );
    }

    @Override
    public void deregisterServiceByClientId( Long clientId ) {
        this.mServiceManager.serviceLifecycleService().deregisterServiceByClientId( clientId );
    }

    @Override
    public void deregisterServiceByInstanceId( String szInstanceId ) {
        this.mServiceManager.serviceLifecycleService().deregisterServiceByInstanceId( szInstanceId );
    }

    public boolean hasOwnedServiceByServiceId( String szServiceId ) {
        return this.mServiceManager.serviceLifecycleService().hasOwnedServiceByServiceId( szServiceId );
    }

    public boolean hasOwnedServiceInstance( Long clientId ) {
        return this.mServiceManager.serviceLifecycleService().hasOwnedServiceInstance( clientId );
    }

    public boolean hasOwnedServiceClient( Long clientId ) {
        return this.mServiceManager.serviceLifecycleService().hasOwnedServiceClient( clientId );
    }

    public boolean hasOwnedServiceInstance( String szInstanceId ) {
        return this.mServiceManager.serviceLifecycleService().hasOwnedServiceInstance( szInstanceId );
    }

    public Integer countRegisteredService() {
        return this.mServiceManager.serviceLifecycleService().countRegisteredService();
    }

}

