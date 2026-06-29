package com.pinecone.hydra.service.registry.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.service.Service;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.kom.ServiceElementPage;
import com.pinecone.hydra.service.kom.ServiceElementQuery;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.ServiceInstancePage;
import com.pinecone.hydra.service.kom.ServiceInstanceQuery;
import com.pinecone.hydra.service.kom.entity.ElementNode;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.kom.marshaling.ServiceJSONDecoder;
import com.pinecone.hydra.service.registry.dto.ServiceInstanceMetaDTO;
import com.pinecone.hydra.service.registry.dto.ServiceInstanceMetaPageDTO;
import com.pinecone.hydra.service.registry.dto.ServiceInstanceQueryDTO;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;
import com.pinecone.hydra.service.registry.dto.ServiceMetaPageDTO;
import com.pinecone.hydra.service.registry.dto.ServiceQueryDTO;
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

    public ServiceMetaPageDTO fetchServicePage( ServiceQueryDTO query ) {
        ServiceElementPage page = this.mServiceInstrument.fetchServicePage( this.toServiceElementQuery( query ) );
        return this.toServiceMetaPageDTO( page );
    }

    public ServiceInstanceMetaPageDTO fetchServiceInstancePage( ServiceInstanceQueryDTO query ) {
        ServiceInstancePage page = this.mServiceInstrument.fetchServiceInstancePage( this.toServiceInstanceQuery( query ) );
        return this.toServiceInstanceMetaPageDTO( page );
    }

    public ServiceMetaDTO queryServiceMetaByPath( String path ) {
        ElementNode node = this.mServiceManager.getServicesInstrument().queryElement( path );
        if ( node == null ) {
            return null;
        }

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

    public String createNewService( String parentPath, ServiceMetaDTO meta ) {
        if ( meta == null || this.isBlank( meta.getName() ) ) {
            return null;
        }

        String servicePath = this.joinServicePath( parentPath, meta.getName() );
        ElementNode existed = this.mServiceInstrument.queryElement( servicePath );
        if ( existed != null && existed.evinceServiceElement() == null ) {
            return null;
        }

        ServiceElement serviceElement = existed == null
                ? this.mServiceInstrument.affirmService( servicePath )
                : existed.evinceServiceElement();
        if ( serviceElement == null ) {
            return null;
        }

        ServiceElement incoming = ServiceMetaDTO.toServiceElement( meta, this.mServiceInstrument.getGuidAllocator() );
        serviceElement.setType( incoming.getType() );
        serviceElement.setAlias( incoming.getAlias() );
        serviceElement.setResourceType( incoming.getResourceType() );
        serviceElement.setServiceType( incoming.getServiceType() );
        serviceElement.setScenario( incoming.getScenario() );
        serviceElement.setPrimaryImplLang( incoming.getPrimaryImplLang() );
        serviceElement.setLevel( incoming.getLevel() );
        serviceElement.setDescription( incoming.getDescription() );
        serviceElement.setExtraInformation( incoming.getExtraInformation() );
        this.mServiceInstrument.update( serviceElement );
        return serviceElement.getGuid().toString();
    }

    protected String joinServicePath( String parentPath, String serviceName ) {
        String parent = parentPath == null ? "" : parentPath.trim();
        String name = serviceName == null ? "" : serviceName.trim();
        while ( parent.endsWith( "/" ) && parent.length() > 1 ) {
            parent = parent.substring( 0, parent.length() - 1 );
        }
        while ( name.startsWith( "/" ) ) {
            name = name.substring( 1 );
        }
        if ( parent.isEmpty() || "/".equals( parent ) ) {
            return "/" + name;
        }
        return parent + "/" + name;
    }

    protected ServiceElementQuery toServiceElementQuery( ServiceQueryDTO dto ) {
        ServiceElementQuery query = new ServiceElementQuery();
        if ( dto == null ) {
            return query;
        }

        query.setOffset( dto.getOffset() );
        query.setLimit( dto.getLimit() );
        query.setKeyword( dto.getKeyword() );
        query.setType( dto.getType() );
        query.setAlias( dto.getAlias() );
        query.setResourceType( dto.getResourceType() );
        query.setServiceType( dto.getServiceType() );
        query.setScenario( dto.getScenario() );
        query.setPrimaryImplLang( dto.getPrimaryImplLang() );
        query.setLevel( dto.getLevel() );

        if ( !this.isBlank( dto.getServiceGuid() ) ) {
            query.setServiceGuid( this.mServiceInstrument.getGuidAllocator().parse( dto.getServiceGuid() ) );
        }

        return query;
    }

    protected ServiceInstanceQuery toServiceInstanceQuery( ServiceInstanceQueryDTO dto ) {
        ServiceInstanceQuery query = new ServiceInstanceQuery();
        if ( dto == null ) {
            return query;
        }

        query.setOffset( dto.getOffset() );
        query.setLimit( dto.getLimit() );
        query.setKeyword( dto.getKeyword() );
        query.setStatus( dto.getStatus() );
        query.setIp( dto.getIp() );

        if ( !this.isBlank( dto.getInstanceGuid() ) ) {
            query.setInstanceGuid( this.mServiceInstrument.getGuidAllocator().parse( dto.getInstanceGuid() ) );
        }
        if ( !this.isBlank( dto.getServiceGuid() ) ) {
            query.setServiceGuid( this.mServiceInstrument.getGuidAllocator().parse( dto.getServiceGuid() ) );
        }
        if ( !this.isBlank( dto.getDeployGuid() ) ) {
            query.setDeployGuid( this.mServiceInstrument.getGuidAllocator().parse( dto.getDeployGuid() ) );
        }
        if ( !this.isBlank( dto.getLatestStartTimeStart() ) ) {
            query.setLatestStartTimeStart( LocalDateTime.parse( dto.getLatestStartTimeStart() ) );
        }
        if ( !this.isBlank( dto.getLatestStartTimeEnd() ) ) {
            query.setLatestStartTimeEnd( LocalDateTime.parse( dto.getLatestStartTimeEnd() ) );
        }
        if ( !this.isBlank( dto.getLatestEndTimeStart() ) ) {
            query.setLatestEndTimeStart( LocalDateTime.parse( dto.getLatestEndTimeStart() ) );
        }
        if ( !this.isBlank( dto.getLatestEndTimeEnd() ) ) {
            query.setLatestEndTimeEnd( LocalDateTime.parse( dto.getLatestEndTimeEnd() ) );
        }

        return query;
    }

    protected ServiceMetaPageDTO toServiceMetaPageDTO( ServiceElementPage page ) {
        ServiceMetaPageDTO dto = new ServiceMetaPageDTO();
        if ( page == null ) {
            return dto;
        }

        List<ServiceMetaDTO> items = new ArrayList<>();
        for ( ServiceElement serviceElement : page.getItems() ) {
            items.add( ServiceMetaDTO.from( serviceElement ) );
        }

        dto.setItems( items );
        dto.setTotal( page.getTotal() );
        dto.setOffset( page.getOffset() );
        dto.setLimit( page.getLimit() );
        return dto;
    }

    protected ServiceInstanceMetaPageDTO toServiceInstanceMetaPageDTO( ServiceInstancePage page ) {
        ServiceInstanceMetaPageDTO dto = new ServiceInstanceMetaPageDTO();
        if ( page == null ) {
            return dto;
        }

        List<ServiceInstanceMetaDTO> items = new ArrayList<>();
        for ( ServiceInstanceEntry entry : page.getItems() ) {
            items.add( ServiceInstanceMetaDTO.from( entry ) );
        }

        dto.setItems( items );
        dto.setTotal( page.getTotal() );
        dto.setOffset( page.getOffset() );
        dto.setLimit( page.getLimit() );
        return dto;
    }

    protected boolean isBlank( String value ) {
        if ( value == null ) {
            return true;
        }

        return value.trim().isEmpty();
    }

}
