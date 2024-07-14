package com.pinecone.hydra.umc.msg;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.IOException;

public class StreamTerminateException extends IOException implements Pinenut {
    public StreamTerminateException() {
        super();
    }

    public StreamTerminateException( String message ) {
        super(message);
    }

    public StreamTerminateException( String message, Throwable cause ) {
        super(message, cause);
    }

    public StreamTerminateException( Throwable cause ) {
        super(cause);
    }
}
