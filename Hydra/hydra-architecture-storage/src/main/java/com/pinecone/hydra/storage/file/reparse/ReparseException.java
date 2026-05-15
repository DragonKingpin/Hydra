package com.pinecone.hydra.storage.file.reparse;

import com.pinecone.hydra.storage.TitanRuntimeException;

public class ReparseException extends TitanRuntimeException {
    public ReparseException    () {
        super();
    }

    public ReparseException    ( String message ) {
        super(message);
    }

    public ReparseException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public ReparseException    ( Throwable cause ) {
        super(cause);
    }
}
