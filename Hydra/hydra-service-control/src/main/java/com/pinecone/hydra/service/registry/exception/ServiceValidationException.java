package com.pinecone.hydra.service.registry.exception;

public class ServiceValidationException extends Exception {
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
