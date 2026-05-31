package com.acorn.redqueen.service.registry.husky.protocol;


import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.dto.ServiceInstanceMetaPageDTO;
import com.pinecone.hydra.service.registry.dto.ServiceInstanceQueryDTO;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;
import com.pinecone.hydra.service.registry.dto.ServiceMetaPageDTO;
import com.pinecone.hydra.service.registry.dto.ServiceQueryDTO;
import com.pinecone.hydra.umct.stereotype.Iface;

@Iface
public interface ServiceMetaManipulationIface extends Pinenut {
    List<ServiceMetaDTO> fetchServiceInsMetaByClientId( long clientId );

    List<ServiceMetaDTO> fetchServiceInsMetaByServiceId( String serviceId );

    ServiceMetaPageDTO fetchServicePage( ServiceQueryDTO query );

    ServiceInstanceMetaPageDTO fetchServiceInstancePage( ServiceInstanceQueryDTO query );

    ServiceMetaDTO queryServiceMetaByPath( String path );

    ServiceMetaDTO queryServiceMetaByGuid( String guid );

    String evalCreationStatement( String jonsStatement );

    String createNewService( String parentAppPath, ServiceMetaDTO meta );

}
