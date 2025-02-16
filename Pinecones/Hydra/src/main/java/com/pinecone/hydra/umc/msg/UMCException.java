package com.pinecone.hydra.umc.msg;

import com.pinecone.framework.system.prototype.Pinenut;

public class UMCException extends Exception implements Pinenut {
    public UMCException() {
        super();
    }

    public UMCException( String message ) {
        super(message);
    }

    public UMCException( String message, Throwable cause ) {
        super(message, cause);
    }

    public UMCException( Throwable cause ) {
        super(cause);
    }
}