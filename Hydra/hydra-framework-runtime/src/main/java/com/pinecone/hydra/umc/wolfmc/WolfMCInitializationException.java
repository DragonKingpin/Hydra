package com.pinecone.hydra.umc.wolfmc;

public class WolfMCInitializationException extends WolfMCServiceException {
    public WolfMCInitializationException() {
        super();
    }

    public WolfMCInitializationException( String message ) {
        super(message);
    }

    public WolfMCInitializationException( String message, Throwable cause ) {
        super(message, cause);
    }

    public WolfMCInitializationException( Throwable cause ) {
        super(cause);
    }
}
