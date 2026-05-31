package com.pinecone.hydra.service.registry.client.port;

import java.util.List;

import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;

public interface ServiceMetaPort extends ServicePort {

    List<ServiceMetaDTO> fetchServiceInsMetaByClientId( long nClientId );

    List<ServiceMetaDTO> fetchServiceInsMetaByServiceId( String szServiceId );

    ServiceMetaDTO queryServiceMetaByPath( String szPath );

    ServiceMetaDTO queryServiceMetaByGuid( String szGuid );

    String evalCreationStatement( String szJonsStatement );

    String createNewService( String szParentAppPath, ServiceMetaDTO meta );

}
