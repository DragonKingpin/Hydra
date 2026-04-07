package com.pinecone.hydra.service.registry.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.service.Service;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.entity.ApplicationElement;
import com.pinecone.hydra.service.kom.entity.ElementNode;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.kom.marshaling.ServiceJSONDecoder;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class ServiceMetaService implements Pinenut {

    protected ServiceManager mServiceManager;

    protected ServiceInstrument mServiceInstrument;

    protected ServiceJSONDecoder mServiceJSONDecoder;

    public ServiceMetaService( ServiceManager serviceManager ){
        this.mServiceManager = serviceManager;
        this.mServiceInstrument = serviceManager.getServicesInstrument();
        this.mServiceJSONDecoder = new ServiceJSONDecoder( this.mServiceInstrument );
    }

    public List<ServiceMetaDTO> fetchServiceInsMetaByClientId(long clientId ){
        List<ServiceMetaDTO> serviceMetaDTOS = new ArrayList<>();
        Collection<ServiceInstance> serviceInstances = this.mServiceManager.fetchServiceInstance( clientId );
        for( ServiceInstance serviceInstance : serviceInstances ){
            Service service = serviceInstance.getService();
            serviceMetaDTOS.add( ServiceMetaDTO.from( service ) );
        }
        return serviceMetaDTOS;
    }

    public List<ServiceMetaDTO> fetchServiceInsMetaByServiceId( String serviceId ) {
        List<ServiceMetaDTO> serviceMetaDTOS = new ArrayList<>();
        Collection<ServiceInstance> serviceInstances = this.mServiceManager.fetchServiceInstance(
                this.mServiceInstrument.getGuidAllocator().parse( serviceId )
        );
        for( ServiceInstance serviceInstance : serviceInstances ){
            Service service = serviceInstance.getService();
            serviceMetaDTOS.add( ServiceMetaDTO.from( service ) );
        }
        return serviceMetaDTOS;
    }

    public ServiceMetaDTO queryServiceMetaByPath( String path ) {
        ElementNode node = this.mServiceManager.getServicesInstrument().queryElement( path );
        ServiceElement serviceElement = node.evinceServiceElement();
        if ( serviceElement == null ) {
            return null;
        }

        return ServiceMetaDTO.from( serviceElement );
    }

    public ServiceMetaDTO queryServiceMetaByGuid( String guid ) {
        TreeNode node = this.mServiceManager.getServicesInstrument().get( this.mServiceInstrument.getGuidAllocator().parse( guid ) );
        if ( node instanceof ServiceElement ) {
            ServiceElement serviceElement = (ServiceElement) node;
            return ServiceMetaDTO.from( serviceElement );
        }
        return null;
    }

    public String evalCreationStatement( String jonsStatement ) {
        ElementNode node = this.mServiceJSONDecoder.decode( new JSONMaptron( jonsStatement ) );
        if ( node == null ) {
            return null;
        }

        return node.getGuid().toString();
    }

    public String createNewService( String parentAppPath, ServiceMetaDTO meta ) {
        ElementNode node = this.mServiceInstrument.queryElement( parentAppPath );
        if ( node instanceof ApplicationElement) {
            ApplicationElement applicationElement = (ApplicationElement) node;
            ServiceElement serviceElement = ServiceMetaDTO.toServiceElement( meta, this.mServiceInstrument.getGuidAllocator() );
            if ( serviceElement.getGuid() == null ) {
                serviceElement.setGuid( this.mServiceInstrument.getGuidAllocator().nextGUID() );
            }
            this.mServiceInstrument.put( serviceElement );
            this.mServiceInstrument.affirmOwnedNode( applicationElement.getGuid(), serviceElement.getGuid() );
            return serviceElement.getGuid().toString();
        }
        return null;
    }

}
