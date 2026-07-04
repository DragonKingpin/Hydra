package com.acorn.redqueen.service.conduct;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.registry.client.entity.ServiceClientRegisterResult;
import com.pinecone.hydra.service.registry.instruction.ServiceRegisterInstruction;

public class ServiceLegionaryJoinFactory implements Pinenut {

    public ServiceRegisterInstruction toRegisterInstruction(
            long nClientId,
            GUID instanceGuid,
            ServiceLegionaryJoinRequest request
    ) {
        ServiceRegisterInstruction instruction = new ServiceRegisterInstruction();
        if ( request == null ) {
            return instruction;
        }

        instruction.setClientId( nClientId );
        instruction.setInstanceGuid( instanceGuid );
        instruction.setServiceGuid( request.getServiceGuid() );
        instruction.setDeployGuid( request.getDeployGuid() );
        instruction.setEndpointProtocol( request.getEndpointProtocol() );
        instruction.setEndpointHost( request.getEndpointHost() );
        instruction.setEndpointPort( request.getEndpointPort() );
        instruction.setEndpointPath( request.getEndpointPath() );
        instruction.setEndpointAddress( request.getEndpointAddress() );
        instruction.setVersion( request.getVersion() );
        instruction.setZone( request.getZone() );
        instruction.setWeight( request.getWeight() );
        instruction.setMetadataJson( request.getMetadataJson() );
        instruction.setRuntimeNodeId( request.getRuntimeNodeId() );
        instruction.setRuntimeNodeAlias( request.getRuntimeNodeAlias() );
        instruction.setRuntimeNodeMetadataJson( request.getRuntimeNodeMetadataJson() );
        return instruction;
    }

    public ServiceLegionaryJoinResponse fromRegisterResult( ServiceClientRegisterResult response ) {
        ServiceLegionaryJoinResponse result = new ServiceLegionaryJoinResponse();
        if ( response == null ) {
            return result;
        }

        result.setServiceGuid( response.getServiceGuid() );
        result.setInstanceGuid( response.getInstanceGuid() );
        result.setStatus( response.getStatus() );
        result.setRegisterTimeMillis( response.getRegisterTimeMillis() );
        result.setExpireTimeMillis( response.getExpireTimeMillis() );
        return result;
    }

}
