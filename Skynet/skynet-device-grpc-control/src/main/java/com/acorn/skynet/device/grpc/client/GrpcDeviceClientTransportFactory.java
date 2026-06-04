package com.acorn.skynet.device.grpc.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.grpc.client.GrpcAppointClient;

public class GrpcDeviceClientTransportFactory implements Pinenut {

    public GrpcDeviceClientTransport create(
            GrpcAppointClient grpcAppointClient,
            GuidAllocator guidAllocator
    ) {
        return new GrpcDeviceClientTransport(
                grpcAppointClient,
                guidAllocator,
                new GrpcDeviceClientTransportConfig()
        );
    }

    public GrpcDeviceClientTransport create(
            String name,
            GrpcAppointClient grpcAppointClient,
            GuidAllocator guidAllocator,
            GrpcDeviceClientTransportConfig config
    ) {
        return new GrpcDeviceClientTransport( name, grpcAppointClient, guidAllocator, config );
    }
}
