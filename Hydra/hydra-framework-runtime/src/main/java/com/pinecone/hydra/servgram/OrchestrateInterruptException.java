package com.pinecone.hydra.servgram;

import com.pinecone.framework.system.PineRuntimeException;

public class OrchestrateInterruptException extends Exception {
    public OrchestrateInterruptException    () {
        super();
    }

    public OrchestrateInterruptException    ( String message ) {
        super(message);
    }

    public OrchestrateInterruptException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public OrchestrateInterruptException    ( Throwable cause ) {
        super(cause);
    }

    protected OrchestrateInterruptException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
