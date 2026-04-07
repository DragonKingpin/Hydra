package com.walnut.odin.system;

import com.pinecone.framework.system.PineRuntimeException;

public class RavenRuntimeException extends PineRuntimeException {

    public RavenRuntimeException() {
        super();
    }

    public RavenRuntimeException( String message ) {
        super(message);
    }

    public RavenRuntimeException( String message, Throwable cause ) {
        super(message, cause);
    }

    public RavenRuntimeException( Throwable cause ) {
        super(cause);
    }

    protected RavenRuntimeException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }

}
