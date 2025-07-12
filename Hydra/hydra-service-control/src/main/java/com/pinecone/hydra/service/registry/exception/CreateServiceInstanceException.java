package com.pinecone.hydra.service.registry.exception;

public class CreateServiceInstanceException extends Exception {
    public CreateServiceInstanceException() {
        super();
    }

    public CreateServiceInstanceException( String message ) {
        super(message);
    }

    public CreateServiceInstanceException( String message, Throwable cause ) {
        super(message, cause);
    }

    public CreateServiceInstanceException( Throwable cause ) {
        super(cause);
    }
}
