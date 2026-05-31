package com.pinecone.hydra.service.registry.server.registrar;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;

public interface ServiceInstanceRegistrar extends Pinenut {

    String registerService( RegisterServiceDTO serviceDTO ) throws ClientServiceRegisterException;

    void deregisterServiceByClientId( Long clientId );

    void deregisterServiceByInstanceId( String szInstanceId );

}
