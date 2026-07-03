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

    public com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaDTO toServiceMetaDTO(
            com.pinecone.hydra.service.registry.dto.ServiceMetaDTO meta
    ) {
        com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaDTO.Builder builder =
                com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaDTO.newBuilder();
        if ( meta == null ) {
            return builder.build();
        }

        builder.setGuid( this.safe( meta.getGuid() ) );
        builder.setName( this.safe( meta.getName() ) );
        builder.setType( this.safe( meta.getType() ) );
        builder.setAlias( this.safe( meta.getAlias() ) );
        builder.setResourceType( this.safe( meta.getResourceType() ) );
        builder.setServiceType( this.safe( meta.getServiceType() ) );
        builder.setDisplayName( this.safe( meta.getDisplayName() ) );
        builder.setDescription( this.safe( meta.getDescription() ) );
        builder.setFullName( this.safe( meta.getFullName() ) );
        builder.setGroupNamespace( this.safe( meta.getGroupNamespace() ) );
        builder.setGroupName( this.safe( meta.getGroupName() ) );
        builder.setScenario( this.safe( meta.getScenario() ) );
        builder.setPrimaryImplLang( this.safe( meta.getPrimaryImplLang() ) );
        builder.setExtraInformation( this.safe( meta.getExtraInformation() ) );
        builder.setLevel( this.safe( meta.getLevel() ) );
        return builder.build();
    }

    public com.pinecone.hydra.service.registry.dto.ServiceMetaDTO toServiceMetaDTO(
            com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaDTO meta
    ) {
        com.pinecone.hydra.service.registry.dto.ServiceMetaDTO dto =
                new com.pinecone.hydra.service.registry.dto.ServiceMetaDTO();
        if ( meta == null ) {
            return dto;
        }

        dto.setGuid( this.nullIfBlank( meta.getGuid() ) );
        dto.setName( this.nullIfBlank( meta.getName() ) );
        dto.setType( this.nullIfBlank( meta.getType() ) );
        dto.setAlias( this.nullIfBlank( meta.getAlias() ) );
        dto.setResourceType( this.nullIfBlank( meta.getResourceType() ) );
        dto.setServiceType( this.nullIfBlank( meta.getServiceType() ) );
        dto.setDisplayName( this.nullIfBlank( meta.getDisplayName() ) );
        dto.setDescription( this.nullIfBlank( meta.getDescription() ) );
        dto.setFullName( this.nullIfBlank( meta.getFullName() ) );
        dto.setGroupNamespace( this.nullIfBlank( meta.getGroupNamespace() ) );
        dto.setGroupName( this.nullIfBlank( meta.getGroupName() ) );
        dto.setScenario( this.nullIfBlank( meta.getScenario() ) );
        dto.setPrimaryImplLang( this.nullIfBlank( meta.getPrimaryImplLang() ) );
        dto.setExtraInformation( this.nullIfBlank( meta.getExtraInformation() ) );
        dto.setLevel( this.nullIfBlank( meta.getLevel() ) );
        return dto;
    }

    protected String safe( Object value ) {
        if ( value == null ) {
            return "";
        }
        return String.valueOf( value );
    }

    protected String nullIfBlank( String value ) {
        if ( value == null || value.isBlank() ) {
            return null;
        }
        return value;
    }
}




