package com.pinecone.hydra.service.registry.ulf;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.entity.GenericServiceInstanceEntity;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.ServiceInstanceCreationException;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@AddressMapping("com.pinecone.hydra.service.registry.server.ServiceLifecycleIface.")
public class ServiceLifecycleController {

    protected ServiceManager mServiceManager;

    protected ServiceInstrument   mServiceInstrument;

    protected GuidAllocator       mGuidAllocator;

    protected Logger              mLogger;

    public ServiceLifecycleController( ServiceManager mServiceManager ) {
        this.mServiceManager        = mServiceManager;
        this.mServiceInstrument     = mServiceManager.getServicesInstrument();
        this.mGuidAllocator         = this.mServiceInstrument.getGuidAllocator();
        this.mLogger                = LoggerFactory.getLogger( this.getClass() );
    }

    @AddressMapping( "registerService" )
    public String registerService( RegisterServiceDTO serviceDTO ) throws ClientServiceRegisterException {
        Long clientId   = serviceDTO.getClientId();
        String szServId = serviceDTO.getServiceId();
        GUID serviceId  = this.mGuidAllocator.parse( szServId );
        GUID deployId   = null;
        if ( serviceDTO.getDeployId() != null ) {
            deployId = this.mGuidAllocator.parse( serviceDTO.getDeployId() );
        }

        GUID insId = this.mServiceManager.registerService( clientId, serviceId, deployId );

        if ( insId != null ) {
            return insId.toString();
        }
        return null;
    }

    @AddressMapping("createInstanceMeta")
    boolean createInstanceMeta( GenericServiceInstanceEntity instanceEntity ) throws ServiceInstanceCreationException {
        try {
            this.mServiceInstrument.createServiceInstance( instanceEntity );
        }
        catch (Exception e) {
            throw new ServiceInstanceCreationException( e );
        }
        return true;
    }

    @AddressMapping("deregisterServiceByClientId")
    public void deregisterServiceByClientId( Long clientId ) {
        this.mServiceManager.deregisterServiceInstance( clientId );
    }

    @AddressMapping("deregisterServiceByInstanceId")
    public void deregisterServiceByInstanceId( String instanceId ) {
        this.mServiceManager.deregisterServiceInstance( this.mGuidAllocator.parse( instanceId ) );
    }

    @AddressMapping("hasOwnedServiceByServiceId")
    public boolean hasOwnedServiceByServiceId( String serviceId ) {
        return this.mServiceManager.hasOwnedService( this.mGuidAllocator.parse( serviceId ) );
    }

    @AddressMapping("hasOwnedServiceInstance")
    public boolean hasOwnedServiceInstance( Long clientId ) {
        return this.mServiceManager.hasOwnedServiceInstance( clientId );
    }

    @AddressMapping("hasOwnedServiceClient")
    public boolean hasOwnedServiceClient( Long clientId ) {
        return this.mServiceManager.hasOwnedServiceClient( clientId );
    }

    @AddressMapping("hasOwnedServiceClient")
    public boolean hasOwnedServiceInstance( String instanceId ) {
        return this.mServiceManager.hasOwnedInstance( this.mGuidAllocator.parse( instanceId ) );
    }

    @AddressMapping("countRegisteredService")
    public Integer countRegisteredService() {
        return this.mServiceManager.countRegisteredService();
    }

}
