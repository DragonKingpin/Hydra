package com.acorn.redqueen.service.registry.grpc.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;

public class GrpcServiceControlException extends ServiceControlRPCException implements Pinenut {

    public GrpcServiceControlException() {
        super();
    }

    public GrpcServiceControlException( String message ) {
        super( message );
    }

    public GrpcServiceControlException( String message, Throwable cause ) {
        super( message, cause );
    }

    public GrpcServiceControlException( Throwable cause ) {
        super( cause );
    }
}



