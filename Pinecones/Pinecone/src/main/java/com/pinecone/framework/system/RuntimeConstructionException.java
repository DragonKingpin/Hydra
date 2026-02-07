package com.pinecone.framework.system;

import com.pinecone.framework.system.prototype.Pinenut;

public class RuntimeConstructionException extends PineRuntimeException implements Pinenut {
    public RuntimeConstructionException    () {
        super();
    }

    public RuntimeConstructionException    ( String message ) {
        super(message);
    }

    public RuntimeConstructionException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public RuntimeConstructionException    ( Throwable cause ) {
        super(cause);
    }

    protected RuntimeConstructionException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}