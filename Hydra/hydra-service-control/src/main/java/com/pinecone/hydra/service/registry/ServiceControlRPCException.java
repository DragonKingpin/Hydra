package com.pinecone.hydra.service.registry;

public class ServiceControlRPCException extends ServiceControlException {

    public ServiceControlRPCException() {
        super();
    }

    public ServiceControlRPCException( String message ) {
        super(message);
    }

    public ServiceControlRPCException( String message, Throwable cause ) {
        super(message, cause);
    }

    public ServiceControlRPCException( Throwable cause ) {
        super(cause);
    }

}
