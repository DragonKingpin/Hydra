package com.pinecone.framework.system;

public class RedirectRuntimeException extends PineRuntimeException {
    public RedirectRuntimeException    () {
        super();
    }

    public RedirectRuntimeException    ( String message ) {
        super(message);
    }

    public RedirectRuntimeException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public RedirectRuntimeException    ( Throwable cause ) {
        super(cause);
    }

    protected RedirectRuntimeException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
