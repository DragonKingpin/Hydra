package com.pinecone.framework.system;

import com.pinecone.framework.system.prototype.Pinenut;

public class RestartSignalException extends Exception implements Pinenut {
    public RestartSignalException    () {
        super();
    }

    public RestartSignalException    ( String message ) {
        super(message);
    }

    public RestartSignalException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public RestartSignalException    ( Throwable cause ) {
        super(cause);
    }

    protected RestartSignalException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}