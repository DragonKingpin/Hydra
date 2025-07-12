package com.pinecone.hydra.service.registry.exception;

public class RpcInitException extends Exception {
    public RpcInitException() {
        super();
    }

    public RpcInitException( String message ) {
        super(message);
    }

    public RpcInitException( String message, Throwable cause ) {
        super(message, cause);
    }

    public RpcInitException( Throwable cause ) {
        super(cause);
    }
}
