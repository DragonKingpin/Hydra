package com.acorn.skynet.device.grpc.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.device.registry.server.transport.DeviceControlTransport;
import com.pinecone.hydra.grpc.server.GrpcAppointServer;

public class GrpcDeviceLifecycleTransportFactory implements Pinenut {

    public DeviceControlTransport create( DeviceManager deviceManager, GrpcAppointServer grpcAppointServer ) {
        return new GrpcDeviceLifecycleTransport( deviceManager, grpcAppointServer );
    }
}
