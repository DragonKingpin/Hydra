package com.acorn.redqueen.service.registry.grpc.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportException;

public class GrpcServiceClientTransportException extends ServiceClientTransportException implements Pinenut {

    public GrpcServiceClientTransportException( String message ) {
        super( message );
    }

    public GrpcServiceClientTransportException( Throwable cause ) {
        super( cause );
    }

    public GrpcServiceClientTransportException( String message, Throwable cause ) {
        super( message, cause );
    }

}



