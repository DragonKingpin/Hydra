package com.pinecone.hydra.service.registry;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.ServiceManager;
import com.pinecone.hydra.service.entity.BindUSII;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceElement;
import com.pinecone.hydra.service.registry.constant.ServiceStatus;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.service.registry.exception.ServiceValidationException;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.ulf.util.guid.GUIDs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@AddressMapping("com.pinecone.hydra.service.registry.ServiceLifecycleIface.")
public class ServiceLifecycleController {

    protected ServiceManager      mServiceManager;

    protected ServiceInstrument   mServiceInstrument;

    protected GuidAllocator       mGuidAllocator;

    protected Logger              mLogger;

    public ServiceLifecycleController( ServiceManager mServiceManager ){
        this.mServiceManager        = mServiceManager;
        this.mServiceInstrument     = mServiceManager.getServicesInstrument();
        this.mGuidAllocator         = this.mServiceInstrument.getGuidAllocator();
        this.mLogger                = LoggerFactory.getLogger( this.getClass() );
    }

    @AddressMapping("registerService")
    public void registerService( RegisterServiceDTO serviceDTO ) throws ServiceValidationException {
        // 进行参数校验
        if( this.ValidationServiceInstance( this.mGuidAllocator.parse( serviceDTO.getServiceId() ) ) ) {
            Long clientId   = serviceDTO.getClientId();
            String szServId = serviceDTO.getServiceId();
            GUID serviceId  = GUIDs.GUID128( szServId );

            ServiceInstanceElement element = this.mServiceInstrument.queryServiceInstance(serviceId);
            TreeNode node = this.mServiceInstrument.get( serviceId );
            ServiceElement serviceElement = (ServiceElement) node;
            WolfServiceInstance serviceInstance = new WolfServiceInstance( clientId, new UniformService( serviceId, serviceElement ), element.getGuid() );

            this.mServiceManager.registerService( serviceInstance );
            this.successRegisterServiceInstance( element );
        }

    }

    @AddressMapping("deregisterServiceByClientId")
    public void deregisterServiceByClientId( Long clientId ){
        this.mServiceManager.removeService( clientId );
    }

    @AddressMapping("deregisterServiceByServiceId")
    public void deregisterServiceByServiceId( String serviceId ){
        this.mServiceManager.removeService( GUIDs.GUID128( serviceId ) );
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
        return this.mServiceManager.hasOwnedService( GUIDs.GUID128( serviceId ) );
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

    protected boolean ValidationServiceInstance( GUID serviceId ) throws ServiceValidationException {
        ServiceInstanceElement element = this.mServiceInstrument.queryServiceInstance(serviceId);
        if( element == null ) {
            throw new ServiceValidationException( "The serviceInstance is not exist" );
        }

        if( element.getStatus() != ServiceStatus.SERVICE_NEW.getCode() ) {
            element.setStatus( ServiceStatus.SERVICE_ERROR.getCode() );
            this.mServiceInstrument.updateServiceInstance( element );
            throw new ServiceValidationException( "The serviceInstance status is incorrect" );
        }

        return true;
    }

    protected void successRegisterServiceInstance( ServiceInstanceElement element ) {
        element.setStatus( ServiceStatus.SERVICE_RUNNING.getCode() );
        element.setRunCount( element.getRunCount() + 1 );

        this.mServiceInstrument.updateServiceInstance( element );
        this.mLogger.info( "serviceInstance {} register success", element.getGuid());
    }
}
