package com.pinecone.hydra.umc.msg;

public class MediumTerminationException extends UMCServiceException {
    public MediumTerminationException() {
        super();
    }

    public MediumTerminationException( String message ) {
        super(message);
    }

    public MediumTerminationException( String message, Throwable cause ) {
        super(message, cause);
    }

    public MediumTerminationException( Throwable cause ) {
        super(cause);
    }
}