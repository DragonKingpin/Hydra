package com.pinecone.hydra.umc.msg;

public class UMCServiceException extends UMCException {
    public UMCServiceException() {
        super();
    }

    public UMCServiceException( String message ) {
        super(message);
    }

    public UMCServiceException( String message, Throwable cause ) {
        super(message, cause);
    }

    public UMCServiceException( Throwable cause ) {
        super(cause);
    }
}