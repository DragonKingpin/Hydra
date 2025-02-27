package com.pinecone.hydra.storage.io;

import java.io.IOException;

import com.pinecone.framework.system.prototype.Pinenut;

public class UIOException extends IOException implements Pinenut {
    public UIOException() {
        super();
    }

    public UIOException( String message ) {
        super(message);
    }

    public UIOException( String message, Throwable cause ) {
        super(message, cause);
    }

    public UIOException( Throwable cause ) {
        super(cause);
    }
}
