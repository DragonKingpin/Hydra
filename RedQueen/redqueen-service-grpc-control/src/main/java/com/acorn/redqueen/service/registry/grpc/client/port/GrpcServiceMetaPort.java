package com.acorn.redqueen.service.registry.grpc.client.port;

import java.util.List;

import com.pinecone.hydra.service.registry.client.port.ServiceMetaPort;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;

public class GrpcServiceMetaPort implements ServiceMetaPort {

    @Override
    public List<ServiceMetaDTO> fetchServiceInsMetaByClientId( long nClientId ) {
        throw new UnsupportedOperationException( "gRPC service meta port is waiting for service_meta.proto expansion." );
    }

    @Override
    public List<ServiceMetaDTO> fetchServiceInsMetaByServiceId( String szServiceId ) {
        throw new UnsupportedOperationException( "gRPC service meta port is waiting for service_meta.proto expansion." );
    }

    @Override
    public ServiceMetaDTO queryServiceMetaByPath( String szPath ) {
        throw new UnsupportedOperationException( "gRPC service meta port is waiting for service_meta.proto expansion." );
    }

    @Override
    public ServiceMetaDTO queryServiceMetaByGuid( String szGuid ) {
        throw new UnsupportedOperationException( "gRPC service meta port is waiting for service_meta.proto expansion." );
    }

    @Override
    public String evalCreationStatement( String szJonsStatement ) {
        throw new UnsupportedOperationException( "gRPC service meta port is waiting for service_meta.proto expansion." );
    }

    @Override
    public String createNewService( String szParentAppPath, ServiceMetaDTO meta ) {
        throw new UnsupportedOperationException( "gRPC service meta port is waiting for service_meta.proto expansion." );
    }

}
