package com.pinecone.hydra.service.registry;

public class ClientServiceRegisterException extends ServiceControlException {

    public ClientServiceRegisterException() {
        super();
    }

    public ClientServiceRegisterException( String message ) {
        super(message);
    }

    public ClientServiceRegisterException( String message, Throwable cause ) {
        super(message, cause);
    }

    public ClientServiceRegisterException( Throwable cause ) {
        super(cause);
    }

}
