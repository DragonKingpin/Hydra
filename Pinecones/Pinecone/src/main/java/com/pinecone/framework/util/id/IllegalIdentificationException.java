package com.pinecone.framework.util.id;

import com.pinecone.framework.system.PineRuntimeException;

public class IllegalIdentificationException extends PineRuntimeException {
    public IllegalIdentificationException    () {
        super();
    }

    public IllegalIdentificationException    ( String message ) {
        super(message);
    }

    public IllegalIdentificationException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public IllegalIdentificationException    ( Throwable cause ) {
        super(cause);
    }

    protected IllegalIdentificationException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
