package com.pinecone.hydra.service.registry;

public class ServiceInstanceCreationException extends ServiceControlException {

    public ServiceInstanceCreationException() {
        super();
    }

    public ServiceInstanceCreationException( String message ) {
        super(message);
    }

    public ServiceInstanceCreationException( String message, Throwable cause ) {
        super(message, cause);
    }

    public ServiceInstanceCreationException( Throwable cause ) {
        super(cause);
    }

}
