package com.pinecone.hydra.auto;

import com.pinecone.framework.system.PineRuntimeException;

public class AbortException extends PineRuntimeException {
    public AbortException    () {
        super();
    }

    public AbortException    ( String message ) {
        super(message);
    }

    public AbortException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public AbortException    ( Throwable cause ) {
        super(cause);
    }
}
