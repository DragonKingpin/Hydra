package com.acorn.redqueen.service.registry.husky.client.transformer;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.registry.instruction.ServiceDeregisterInstruction;
import com.pinecone.hydra.service.registry.instruction.ServiceRegisterInstruction;
import com.pinecone.hydra.service.registry.client.entity.ServiceClientDeregisterResult;
import com.pinecone.hydra.service.registry.client.entity.ServiceClientRegisterResult;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;

public class HuskyServiceLifecycleTransformer implements Pinenut {

    protected GuidAllocator mGuidAllocator;

    public HuskyServiceLifecycleTransformer( GuidAllocator guidAllocator ) {
        this.mGuidAllocator = guidAllocator;
    }

    public RegisterServiceDTO encodeRegisterInstruction(ServiceRegisterInstruction instruction, long nClientId ) {
        RegisterServiceDTO dto = new RegisterServiceDTO();
        dto.setClientId( nClientId );
        if ( instruction == null ) {
            return dto;
        }

        dto.setServiceId( this.toString( instruction.getServiceGuid() ) );
        dto.setDeployId( this.toString( instruction.getDeployGuid() ) );
        dto.setInstanceGuid( this.toString( instruction.getInstanceGuid() ) );
        dto.setEndpointProtocol( instruction.getEndpointProtocol() );
        dto.setEndpointHost( instruction.getEndpointHost() );
        dto.setEndpointPort( instruction.getEndpointPort() );
        dto.setEndpointPath( instruction.getEndpointPath() );
        dto.setEndpointAddress( instruction.getEndpointAddress() );
        dto.setVersion( instruction.getVersion() );
        dto.setZone( instruction.getZone() );
        dto.setWeight( instruction.getWeight() );
        dto.setMetadataJson( instruction.getMetadataJson() );
        dto.setRuntimeNodeId( instruction.getRuntimeNodeId() );
        dto.setRuntimeNodeAlias( instruction.getRuntimeNodeAlias() );
        dto.setRuntimeNodeMetadataJson( instruction.getRuntimeNodeMetadataJson() );
        return dto;
    }

    public ServiceClientRegisterResult decodeRegisterResult(
            ServiceRegisterInstruction instruction,
            String szInstanceGuid
    ) {
        ServiceClientRegisterResult result = new ServiceClientRegisterResult();
        if ( instruction != null ) {
            result.setServiceGuid( instruction.getServiceGuid() );
        }

        result.setInstanceGuid( this.toGuid( szInstanceGuid ) );
        result.setRegisterTimeMillis( System.currentTimeMillis() );
        return result;
    }

    public String encodeDeregisterInstruction( ServiceDeregisterInstruction instruction ) {
        return instruction == null ? null : this.toString( instruction.getInstanceGuid() );
    }

    public ServiceClientDeregisterResult toDeregisterResult(
            ServiceDeregisterInstruction instruction
    ) {
        ServiceClientDeregisterResult result = new ServiceClientDeregisterResult();
        if ( instruction != null ) {
            result.setInstanceGuid( instruction.getInstanceGuid() );
            result.setReason( instruction.getReason() );
        }
        result.setSuccess( true );
        return result;
    }

    protected GUID toGuid( String szGuid ) {
        if ( szGuid == null || szGuid.isBlank() ) {
            return null;
        }
        return this.mGuidAllocator.parse( szGuid );
    }

    protected String toString( GUID guid ) {
        return guid == null ? null : guid.toString();
    }

}

