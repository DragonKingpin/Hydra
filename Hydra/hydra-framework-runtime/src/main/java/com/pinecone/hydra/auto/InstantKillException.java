package com.pinecone.hydra.auto;

import com.pinecone.framework.system.PineRuntimeException;

public class InstantKillException extends PineRuntimeException {
    public InstantKillException    () {
        super();
    }

    public InstantKillException    ( String message ) {
        super(message);
    }

    public InstantKillException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public InstantKillException    ( Throwable cause ) {
        super(cause);
    }
}
