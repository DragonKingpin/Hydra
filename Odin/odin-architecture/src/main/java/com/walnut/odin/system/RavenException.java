package com.walnut.odin.system;

import com.pinecone.framework.system.prototype.Pinenut;

public class RavenException extends Exception implements Pinenut {

    public RavenException() {
        super();
    }

    public RavenException(String message ) {
        super(message);
    }

    public RavenException(String message, Throwable cause ) {
        super(message, cause);
    }

    public RavenException(Throwable cause ) {
        super(cause);
    }

    protected RavenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }

}
