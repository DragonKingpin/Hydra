package com.acorn.redqueen.service.registry.husky.client.port;

import java.util.List;

import com.pinecone.hydra.service.registry.client.port.ServiceMetaPort;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;
import com.acorn.redqueen.service.registry.husky.protocol.ServiceMetaManipulationIface;

public class HuskyServiceMetaPort implements ServiceMetaPort {

    protected ServiceMetaManipulationIface mMetaIface;

    public HuskyServiceMetaPort( ServiceMetaManipulationIface metaIface ) {
        this.mMetaIface = metaIface;
    }

    public void bind( ServiceMetaManipulationIface metaIface ) {
        this.mMetaIface = metaIface;
    }

    @Override
    public List<ServiceMetaDTO> fetchServiceInsMetaByClientId( long nClientId ) {
        return this.mMetaIface.fetchServiceInsMetaByClientId( nClientId );
    }

    @Override
    public List<ServiceMetaDTO> fetchServiceInsMetaByServiceId( String szServiceId ) {
        return this.mMetaIface.fetchServiceInsMetaByServiceId( szServiceId );
    }

    @Override
    public ServiceMetaDTO queryServiceMetaByPath( String szPath ) {
        return this.mMetaIface.queryServiceMetaByPath( szPath );
    }

    @Override
    public ServiceMetaDTO queryServiceMetaByGuid( String szGuid ) {
        return this.mMetaIface.queryServiceMetaByGuid( szGuid );
    }

    @Override
    public String evalCreationStatement( String szJonsStatement ) {
        return this.mMetaIface.evalCreationStatement( szJonsStatement );
    }

    @Override
    public String createNewService( String szParentAppPath, ServiceMetaDTO meta ) {
        return this.mMetaIface.createNewService( szParentAppPath, meta );
    }

}
