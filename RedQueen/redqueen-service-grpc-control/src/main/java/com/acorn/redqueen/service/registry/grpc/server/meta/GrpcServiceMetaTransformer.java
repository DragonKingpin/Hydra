package com.acorn.redqueen.service.registry.grpc.server.meta;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceDTO;
import com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceInstanceDTO;

public class GrpcServiceMetaTransformer implements Pinenut {

    public ServiceDTO toServiceDTO( ServiceElement element ) {
        ServiceDTO.Builder builder = ServiceDTO.newBuilder();
        if ( element == null ) {
            return builder.build();
        }

        builder.setGuid( this.safe( element.getGuid() ) );
        builder.setName( this.safe( element.getName() ) );
        builder.setType( this.safe( element.getType() ) );
        builder.setDisplayName( this.safe( element.getAlias() ) );
        builder.setDescription( this.safe( element.getDescription() ) );
        builder.setFullName( this.safe( element.getPath() ) );
        builder.setScenario( this.safe( element.getScenario() ) );
        builder.setPrimaryImplLang( this.safe( element.getPrimaryImplLang() ) );
        builder.setExtraInformation( this.safe( element.getExtraInformation() ) );
        builder.setLevel( this.safe( element.getLevel() ) );
        return builder.build();
    }

    public ServiceInstanceDTO toServiceInstanceDTO( ServiceInstanceEntry entry ) {
        ServiceInstanceDTO.Builder builder = ServiceInstanceDTO.newBuilder();
        if ( entry == null ) {
            return builder.build();
        }

        builder.setGuid( this.safe( entry.getGuid() ) );
        builder.setServiceGuid( this.safe( entry.getServiceGuid() ) );
        builder.setDeployGuid( this.safe( entry.getDeployGuid() ) );
        builder.setClientId( this.safe( entry.getClientId() ) );
        builder.setTransportType( this.safe( entry.getTransportType() ) );
        builder.setRemoteAddress( this.safe( entry.getRemoteAddress() ) );
        builder.setEndpointProtocol( this.safe( entry.getEndpointProtocol() ) );
        builder.setEndpointHost( this.safe( entry.getEndpointHost() ) );
        if ( entry.getEndpointPort() != null ) {
            builder.setEndpointPort( entry.getEndpointPort() );
        }
        builder.setEndpointPath( this.safe( entry.getEndpointPath() ) );
        builder.setEndpointAddress( this.safe( entry.getEndpointAddress() ) );
        builder.setStatus( this.safe( entry.getStatus() ) );
        builder.setStatusReason( this.safe( entry.getStatusReason() ) );
        builder.setVersion( this.safe( entry.getVersion() ) );
        builder.setZone( this.safe( entry.getZone() ) );
        builder.setWeight( entry.getWeight() );
        builder.setMetadataJson( this.safe( entry.getMetadataJson() ) );
        return builder.build();
    }

    protected String safe( Object value ) {
        if ( value == null ) {
            return "";
        }
        return String.valueOf( value );
    }
}




