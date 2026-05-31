package com.pinecone.hydra.service.registry.client.port;

import com.pinecone.hydra.service.registry.client.instruction.ServiceClientDeregisterInstruction;
import com.pinecone.hydra.service.registry.client.instruction.ServiceClientRegisterInstruction;
import com.pinecone.hydra.service.registry.client.entity.ServiceClientDeregisterResult;
import com.pinecone.hydra.service.registry.client.entity.ServiceClientRegisterResult;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportException;

public interface ServiceLifecyclePort extends ServicePort {

    ServiceClientRegisterResult register( ServiceClientRegisterInstruction instruction ) throws ServiceClientTransportException;

    ServiceClientDeregisterResult deregister( ServiceClientDeregisterInstruction instruction ) throws ServiceClientTransportException;

}
