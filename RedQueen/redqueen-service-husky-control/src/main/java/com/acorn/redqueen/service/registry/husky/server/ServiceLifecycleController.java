package com.acorn.redqueen.service.registry.husky.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.kom.entity.GenericServiceInstanceEntity;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.ServiceInstanceCreationException;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.service.registry.server.ServiceLifecycleService;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

@Controller
@AddressMapping("com.acorn.redqueen.service.registry.husky.protocol.ServiceLifecycleIface.")
public class ServiceLifecycleController implements Pinenut {

    protected ServiceLifecycleService mServiceLifecycleService;

    public ServiceLifecycleController( ServiceManager serviceManager ) {
        this.mServiceLifecycleService = serviceManager.serviceLifecycleService();
    }

    @AddressMapping( "registerService" )
    public String registerService( RegisterServiceDTO serviceDTO ) throws ClientServiceRegisterException {
        return this.mServiceLifecycleService.registerService( serviceDTO );
    }

    @AddressMapping("createInstanceMeta")
    public boolean createInstanceMeta( ServiceInstanceEntry serviceInstanceEntry ) throws ServiceInstanceCreationException {
        return this.mServiceLifecycleService.createInstanceMeta( this.toGenericServiceInstanceEntity( serviceInstanceEntry ) );
    }

    @AddressMapping("deregisterServiceByClientId")
    public void deregisterServiceByClientId( Long clientId ) {
        this.mServiceLifecycleService.deregisterServiceByClientId( clientId );
    }

    @AddressMapping("deregisterServiceByInstanceId")
    public void deregisterServiceByInstanceId( String instanceId ) {
        this.mServiceLifecycleService.deregisterServiceByInstanceId( instanceId );
    }

    @AddressMapping("hasOwnedServiceByServiceId")
    public boolean hasOwnedServiceByServiceId( String serviceId ) {
        return this.mServiceLifecycleService.hasOwnedServiceByServiceId( serviceId );
    }

    @AddressMapping("hasOwnedServiceInstance")
    public boolean hasOwnedServiceInstance( Long clientId ) {
        return this.mServiceLifecycleService.hasOwnedServiceInstance( clientId );
    }

    @AddressMapping("hasOwnedServiceInstance")
    public boolean hasOwnedServiceInstance( String instanceId ) {
        return this.mServiceLifecycleService.hasOwnedServiceInstance( instanceId );
    }

    @AddressMapping("hasOwnedServiceClient")
    public boolean hasOwnedServiceClient( Long clientId ) {
        return this.mServiceLifecycleService.hasOwnedServiceClient( clientId );
    }

    @AddressMapping("countRegisteredService")
    public Integer countRegisteredService() {
        return this.mServiceLifecycleService.countRegisteredService();
    }

    protected GenericServiceInstanceEntity toGenericServiceInstanceEntity( ServiceInstanceEntry entry ) {
        if ( entry instanceof GenericServiceInstanceEntity ) {
            return (GenericServiceInstanceEntity) entry;
        }

        GenericServiceInstanceEntity entity = new GenericServiceInstanceEntity();
        if ( entry == null ) {
            return entity;
        }

        entity.setGuid( entry.getGuid() );
        entity.setServiceGuid( entry.getServiceGuid() );
        entity.setDeployGuid( entry.getDeployGuid() );
        entity.setClientId( entry.getClientId() );
        entity.setTransportType( entry.getTransportType() );
        entity.setRemoteAddress( entry.getRemoteAddress() );
        entity.setEndpointProtocol( entry.getEndpointProtocol() );
        entity.setEndpointHost( entry.getEndpointHost() );
        entity.setEndpointPort( entry.getEndpointPort() );
        entity.setEndpointPath( entry.getEndpointPath() );
        entity.setEndpointAddress( entry.getEndpointAddress() );
        entity.setStatus( entry.getStatus() );
        entity.setStatusReason( entry.getStatusReason() );
        entity.setVersion( entry.getVersion() );
        entity.setZone( entry.getZone() );
        entity.setWeight( entry.getWeight() );
        entity.setRegisterTime( entry.getRegisterTime() );
        entity.setLastHeartbeatTime( entry.getLastHeartbeatTime() );
        entity.setExpireTime( entry.getExpireTime() );
        entity.setOfflineTime( entry.getOfflineTime() );
        entity.setLatestStartTime( entry.getLatestStartTime() );
        entity.setLatestEndTime( entry.getLatestEndTime() );
        entity.setErrorCause( entry.getErrorCause() );
        entity.setRunCount( entry.getRunCount() );
        entity.setIp( entry.getIp() );
        entity.setMetadataJson( entry.getMetadataJson() );
        return entity;
    }

}
