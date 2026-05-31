package com.acorn.redqueen.service.registry.grpc.client.port;

import com.acorn.redqueen.service.registry.grpc.client.GrpcServiceClientTransport;
import com.pinecone.hydra.service.registry.client.instruction.ServiceClientDeregisterInstruction;
import com.pinecone.hydra.service.registry.client.instruction.ServiceClientRegisterInstruction;
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
            ServiceClientRegisterInstruction instruction
    ) throws ServiceClientTransportException {
        return this.mTransport.register( instruction );
    }

    @Override
    public ServiceClientDeregisterResult deregister(
            ServiceClientDeregisterInstruction instruction
    ) throws ServiceClientTransportException {
        return this.mTransport.deregister( instruction );
    }

}
