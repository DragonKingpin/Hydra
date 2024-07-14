package com.pinecone.framework.system;

import com.pinecone.framework.system.prototype.Pinenut;

public class PineRuntimeException extends RuntimeException implements Pinenut {
    public PineRuntimeException    () {
        super();
    }

    public PineRuntimeException    ( String message ) {
        super(message);
    }

    public PineRuntimeException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public PineRuntimeException    ( Throwable cause ) {
        super(cause);
    }

    protected PineRuntimeException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}