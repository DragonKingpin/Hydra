package com.acorn.redqueen.service.registry.grpc.client.port;

import com.acorn.redqueen.service.registry.grpc.client.GrpcServiceClientTransport;
import com.pinecone.hydra.service.registry.instruction.ServiceDeregisterInstruction;
import com.pinecone.hydra.service.registry.instruction.ServiceRegisterInstruction;
import com.pinecone.hydra.service.registry.client.entity.ServiceClientDeregisterResult;
import com.pinecone.hydra.service.registry.client.entity.ServiceClientRegisterResult;
import com.pinecone.hydra.service.registry.client.port.ServiceLifecyclePort;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportException;

public class GrpcServiceLifecyclePort implements ServiceLifecyclePort {

    protected GrpcServiceClientTransport mTransport;

    public GrpcServiceLifecyclePort( GrpcServiceClientTransport transport ) {
        this.mTransport = transport;
    }

    @Override
    public ServiceClientRegisterResult register(
            ServiceRegisterInstruction instruction
    ) throws ServiceClientTransportException {
        return this.mTransport.register( instruction );
    }

    @Override
    public ServiceClientDeregisterResult deregister(
            ServiceDeregisterInstruction instruction
    ) throws ServiceClientTransportException {
        return this.mTransport.deregister( instruction );
    }

}

