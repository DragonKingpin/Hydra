package com.pinecone.hydra.service.registry.client.port;

import com.pinecone.hydra.service.registry.client.entity.ServiceClientDeregisterResult;
import com.pinecone.hydra.service.registry.client.entity.ServiceClientRegisterResult;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportException;
import com.pinecone.hydra.service.registry.instruction.ServiceDeregisterInstruction;
import com.pinecone.hydra.service.registry.instruction.ServiceRegisterInstruction;

public interface ServiceLifecyclePort extends ServicePort {

    ServiceClientRegisterResult register( ServiceRegisterInstruction instruction ) throws ServiceClientTransportException;

    ServiceClientDeregisterResult deregister( ServiceDeregisterInstruction instruction ) throws ServiceClientTransportException;

}
