package com.acorn.redqueen.service.registry.husky.server;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.dto.ServiceInstanceMetaPageDTO;
import com.pinecone.hydra.service.registry.dto.ServiceInstanceQueryDTO;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;
import com.pinecone.hydra.service.registry.dto.ServiceMetaPageDTO;
import com.pinecone.hydra.service.registry.dto.ServiceQueryDTO;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.server.ServiceMetaService;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;

@Controller
@AddressMapping("com.acorn.redqueen.service.registry.husky.protocol.ServiceMetaManipulationIface.")
public class ServiceMetaController implements Pinenut {

    protected ServiceMetaService mServiceMetaService;

    public ServiceMetaController( ServiceManager serviceManager ) {
        this.mServiceMetaService = serviceManager.getServiceMetaService();
    }

    @AddressMapping( "fetchServiceInsMetaByClientId" )
    public List<ServiceMetaDTO> fetchServiceInsMetaByClientId( long nClientId ) {
        return this.mServiceMetaService.fetchServiceInsMetaByClientId( nClientId );
    }

    @AddressMapping( "fetchServiceInsMetaByServiceId" )
    public List<ServiceMetaDTO> fetchServiceInsMetaByServiceId( String szServiceId ) {
        return this.mServiceMetaService.fetchServiceInsMetaByServiceId( szServiceId );
    }

    @AddressMapping( "fetchServicePage" )
    public ServiceMetaPageDTO fetchServicePage( ServiceQueryDTO query ) {
        return this.mServiceMetaService.fetchServicePage( query );
    }

    @AddressMapping( "fetchServiceInstancePage" )
    public ServiceInstanceMetaPageDTO fetchServiceInstancePage( ServiceInstanceQueryDTO query ) {
        return this.mServiceMetaService.fetchServiceInstancePage( query );
    }

    @AddressMapping( "queryServiceMetaByPath" )
    public ServiceMetaDTO queryServiceMetaByPath( String szPath ) {
        return this.mServiceMetaService.queryServiceMetaByPath( szPath );
    }

    @AddressMapping( "queryServiceMetaByGuid" )
    public ServiceMetaDTO queryServiceMetaByGuid( String szGuid ) {
        return this.mServiceMetaService.queryServiceMetaByGuid( szGuid );
    }

    @AddressMapping( "evalCreationStatement" )
    public String evalCreationStatement( String szJonsStatement ) {
        return this.mServiceMetaService.evalCreationStatement( szJonsStatement );
    }

    @AddressMapping( "createNewService" )
    public String createNewService( String szParentAppPath, ServiceMetaDTO meta ) {
        return this.mServiceMetaService.createNewService( szParentAppPath, meta );
    }

}
