package com.acorn.redqueen.service.registry.grpc.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.grpc.client.GrpcAppointClient;

public class GrpcServiceClientTransportFactory implements Pinenut {

    public GrpcServiceClientTransport create(
            GrpcAppointClient grpcAppointClient,
            GuidAllocator guidAllocator,
            GrpcServiceClientTransportConfig config
    ) {
        return new GrpcServiceClientTransport( grpcAppointClient, guidAllocator, config );
    }

}



