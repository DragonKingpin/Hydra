package com.pinecone.hydra.auto;

import com.pinecone.framework.system.PineRuntimeException;

public class ContinueException extends PineRuntimeException {
    public ContinueException    () {
        super();
    }

    public ContinueException    ( String message ) {
        super(message);
    }

    public ContinueException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public ContinueException    ( Throwable cause ) {
        super(cause);
    }
}