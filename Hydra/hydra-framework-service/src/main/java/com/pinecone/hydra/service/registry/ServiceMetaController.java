package com.pinecone.hydra.service.registry;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.Service;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.ServiceManager;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.pinecone.ulf.util.guid.GUIDs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@AddressMapping("com.pinecone.hydra.service.registry.ServiceMetaManipulationIface.")
public class ServiceMetaController implements Pinenut {
    protected ServiceManager          mServiceManager;

    protected ServicesInstrument      mServicesInstrument;

    public ServiceMetaController( ServiceManager serviceManager ){
        this.mServiceManager = serviceManager;
        this.mServicesInstrument = serviceManager.getServicesInstrument();
    }

    @AddressMapping( "fetchServiceInsMetaByClientId" )
    public List<ServiceMetaDTO> fetchServiceInsMetaByClientId( long clientId ){
        List<ServiceMetaDTO> serviceMetaDTOS = new ArrayList<>();
        Collection<ServiceInstance> serviceInstances = this.mServiceManager.fetchServiceInstance( clientId );
        for( ServiceInstance serviceInstance : serviceInstances ){
            Service service = serviceInstance.getService();
            serviceMetaDTOS.add( this.toServiceMetaDTO( service ) );
        }
        return serviceMetaDTOS;
    }

    @AddressMapping( "fetchServiceInsMetaByServiceId" )
    public List<ServiceMetaDTO> fetchServiceInsMetaByServiceId( String serviceId ) {
        List<ServiceMetaDTO> serviceMetaDTOS = new ArrayList<>();
        Collection<ServiceInstance> serviceInstances = this.mServiceManager.fetchServiceInstance(GUIDs.GUID72( serviceId ));
        for( ServiceInstance serviceInstance : serviceInstances ){
            Service service = serviceInstance.getService();
            serviceMetaDTOS.add( this.toServiceMetaDTO( service ) );
        }
        return serviceMetaDTOS;
    }

    private ServiceMetaDTO toServiceMetaDTO( Service service ){
        ServiceMetaDTO serviceMetaDTO = new ServiceMetaDTO();
        serviceMetaDTO.setGuid( service.getId().toString() );
        serviceMetaDTO.setName(service.getName());
        serviceMetaDTO.setDescription( service.getDescription() );
        serviceMetaDTO.setDisplayName( service.getDisplayName() );
        serviceMetaDTO.setFullName( service.getFullName() );
        serviceMetaDTO.setExtraInformation( service.getExtraInformation() );
        serviceMetaDTO.setLevel( service.getLevel() );
        serviceMetaDTO.setScenario( service.getScenario() );
        serviceMetaDTO.setPrimaryImplLang( service.getPrimaryImplLang() );
        serviceMetaDTO.setGroupName( service.getGroupName() );
        return serviceMetaDTO;
    }
}
