package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.hydra.umc.msg.UMCServiceException;

public class WolfMCServiceException extends UMCServiceException {
    public WolfMCServiceException() {
        super();
    }

    public WolfMCServiceException( String message ) {
        super(message);
    }

    public WolfMCServiceException( String message, Throwable cause ) {
        super(message, cause);
    }

    public WolfMCServiceException( Throwable cause ) {
        super(cause);
    }
}
