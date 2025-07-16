package com.pinecone.hydra.service.registry;

public class ServiceValidationException extends ServiceControlException {

    public ServiceValidationException() {
        super();
    }

    public ServiceValidationException( String message ) {
        super(message);
    }

    public ServiceValidationException( String message, Throwable cause ) {
        super(message, cause);
    }

    public ServiceValidationException( Throwable cause ) {
        super(cause);
    }

}
