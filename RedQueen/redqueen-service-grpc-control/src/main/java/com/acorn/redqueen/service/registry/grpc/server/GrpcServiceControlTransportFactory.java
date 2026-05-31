package com.acorn.redqueen.service.registry.grpc.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.grpc.server.GrpcAppointServer;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransport;

public class GrpcServiceControlTransportFactory implements Pinenut {

    public ServiceControlTransport create( ServiceManager serviceManager, GrpcAppointServer grpcAppointServer ) {
        return new GrpcServiceControlTransport( serviceManager, grpcAppointServer );
    }
}



