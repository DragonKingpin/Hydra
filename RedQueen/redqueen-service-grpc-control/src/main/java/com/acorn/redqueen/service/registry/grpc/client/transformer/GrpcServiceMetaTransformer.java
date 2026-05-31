package com.acorn.redqueen.service.registry.grpc.client.transformer;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;

public class GrpcServiceMetaTransformer implements Pinenut {

    public com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaDTO toProtocolMeta(
            ServiceMetaDTO meta
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

    public ServiceMetaDTO toServiceMetaDTO(
            com.acorn.redqueen.service.registry.grpc.protocol.meta.proto.ServiceMetaDTO meta
    ) {
        if ( meta == null || this.isBlank( meta.getGuid() ) && this.isBlank( meta.getName() ) ) {
            return null;
        }

        ServiceMetaDTO dto = new ServiceMetaDTO();
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
        if ( this.isBlank( value ) ) {
            return null;
        }
        return value;
    }

    protected boolean isBlank( String value ) {
        return value == null || value.isBlank();
    }
}
