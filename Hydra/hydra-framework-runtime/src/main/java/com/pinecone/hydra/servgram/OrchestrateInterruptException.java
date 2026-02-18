package com.pinecone.hydra.servgram;

import com.pinecone.framework.system.prototype.Pinenut;

public class OrchestrateInterruptException extends Exception implements Pinenut {
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
