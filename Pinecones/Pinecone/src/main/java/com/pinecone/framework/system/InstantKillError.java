package com.pinecone.framework.system;

import com.pinecone.framework.system.prototype.Pinenut;

public class InstantKillError extends Error implements Pinenut {
    public InstantKillError() {
        super();
    }

    public InstantKillError( String message ) {
        super(message);
    }

    public InstantKillError( String message, Throwable cause ) {
        super(message, cause);
    }

    public InstantKillError( Throwable cause ) {
        super(cause);
    }

    protected InstantKillError( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}