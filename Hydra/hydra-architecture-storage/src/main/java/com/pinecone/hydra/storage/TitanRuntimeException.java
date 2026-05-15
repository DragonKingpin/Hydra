package com.pinecone.hydra.storage;

import com.pinecone.framework.system.PineRuntimeException;

public class TitanRuntimeException extends PineRuntimeException {
    public TitanRuntimeException    () {
        super();
    }

    public TitanRuntimeException    ( String message ) {
        super(message);
    }

    public TitanRuntimeException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public TitanRuntimeException    ( Throwable cause ) {
        super(cause);
    }
}