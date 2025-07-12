package com.pinecone.hydra.service.registry.exception;

public class ClientRegisterServiceException extends Exception {
    public ClientRegisterServiceException() {
        super();
    }

    public ClientRegisterServiceException( String message ) {
        super(message);
    }

    public ClientRegisterServiceException( String message, Throwable cause ) {
        super(message, cause);
    }

    public ClientRegisterServiceException( Throwable cause ) {
        super(cause);
    }
}
