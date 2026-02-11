package com.pinecone.hydra.service.registry.ulf;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;
import com.pinecone.hydra.service.registry.server.ServiceMetaService;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

@Controller
@AddressMapping("com.pinecone.hydra.service.registry.server.ServiceMetaManipulationIface.")
public class ServiceMetaController implements Pinenut {
    protected ServiceMetaService serviceMetaService;

    public ServiceMetaController( ServiceManager serviceManager ) {
        this.serviceMetaService = serviceManager.getServiceMetaService();
    }

    @AddressMapping( "fetchServiceInsMetaByClientId" )
    public List<ServiceMetaDTO> fetchServiceInsMetaByClientId( long clientId ){
        return this.serviceMetaService.fetchServiceInsMetaByClientId( clientId );
    }

    @AddressMapping( "fetchServiceInsMetaByServiceId" )
    public List<ServiceMetaDTO> fetchServiceInsMetaByServiceId( String serviceId ) {
        return this.serviceMetaService.fetchServiceInsMetaByServiceId( serviceId );
    }

    @AddressMapping( "queryServiceMetaByPath" )
    public ServiceMetaDTO queryServiceMetaByPath( String path ) {
        return this.serviceMetaService.queryServiceMetaByPath( path );
    }

    @AddressMapping( "queryServiceMetaByGuid" )
    public ServiceMetaDTO queryServiceMetaByGuid( String guid ) {
        return this.serviceMetaService.queryServiceMetaByGuid( guid );
    }


    @AddressMapping( "evalCreationStatement" )
    public String evalCreationStatement( String jonsStatement ) {
        return this.serviceMetaService.evalCreationStatement( jonsStatement );
    }

    @AddressMapping( "createNewService" )
    public String createNewService( String parentAppPath, ServiceMetaDTO meta ) {
        return this.serviceMetaService.createNewService( parentAppPath, meta );
    }


}
