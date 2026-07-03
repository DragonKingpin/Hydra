package com.acorn.skynet.device.grpc.server.meta;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.registry.dto.DeviceMetaDTO;

public class GrpcDeviceMetaTransformer implements Pinenut {

    public com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaDTO toProtocolMeta( DeviceMetaDTO meta ) {
        com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaDTO.Builder builder =
                com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaDTO.newBuilder();
        if ( meta == null ) {
            return builder.build();
        }

        builder.setGuid( this.safe( meta.getGuid() ) );
        builder.setMetaGuid( this.safe( meta.getMetaGuid() ) );
        builder.setPath( this.safe( meta.getPath() ) );
        builder.setName( this.safe( meta.getName() ) );
        builder.setAlias( this.safe( meta.getAlias() ) );
        builder.setExtraInformation( this.safe( meta.getExtraInformation() ) );
        builder.setResourceType( this.safe( meta.getResourceType() ) );
        builder.setDeviceType( this.safe( meta.getDeviceType() ) );
        builder.setVendor( this.safe( meta.getVendor() ) );
        builder.setModel( this.safe( meta.getModel() ) );
        builder.setSerialNumber( this.safe( meta.getSerialNumber() ) );
        builder.setIpAddress( this.safe( meta.getIpAddress() ) );
        builder.setStatus( this.safe( meta.getStatus() ) );
        builder.setDescription( this.safe( meta.getDescription() ) );
        builder.setCreateTime( this.safe( meta.getCreateTime() ) );
        builder.setUpdateTime( this.safe( meta.getUpdateTime() ) );
        return builder.build();
    }

    public DeviceMetaDTO toDeviceMetaDTO( com.acorn.skynet.device.grpc.protocol.meta.proto.DeviceMetaDTO meta ) {
        if ( meta == null ) {
            return null;
        }

        DeviceMetaDTO dto = new DeviceMetaDTO();
        dto.setGuid( this.nullIfBlank( meta.getGuid() ) );
        dto.setMetaGuid( this.nullIfBlank( meta.getMetaGuid() ) );
        dto.setPath( this.nullIfBlank( meta.getPath() ) );
        dto.setName( this.nullIfBlank( meta.getName() ) );
        dto.setAlias( this.nullIfBlank( meta.getAlias() ) );
        dto.setExtraInformation( this.nullIfBlank( meta.getExtraInformation() ) );
        dto.setResourceType( this.nullIfBlank( meta.getResourceType() ) );
        dto.setDeviceType( this.nullIfBlank( meta.getDeviceType() ) );
        dto.setVendor( this.nullIfBlank( meta.getVendor() ) );
        dto.setModel( this.nullIfBlank( meta.getModel() ) );
        dto.setSerialNumber( this.nullIfBlank( meta.getSerialNumber() ) );
        dto.setIpAddress( this.nullIfBlank( meta.getIpAddress() ) );
        dto.setStatus( this.nullIfBlank( meta.getStatus() ) );
        dto.setDescription( this.nullIfBlank( meta.getDescription() ) );
        dto.setCreateTime( this.nullIfBlank( meta.getCreateTime() ) );
        dto.setUpdateTime( this.nullIfBlank( meta.getUpdateTime() ) );
        return dto;
    }

    protected String safe( String value ) {
        return value == null ? "" : value;
    }

    protected String nullIfBlank( String value ) {
        return value == null || value.trim().isEmpty() ? null : value;
    }
}
