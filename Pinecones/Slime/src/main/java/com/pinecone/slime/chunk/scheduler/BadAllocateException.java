package com.pinecone.slime.chunk.scheduler;

import com.pinecone.framework.system.PineRuntimeException;

public class BadAllocateException extends PineRuntimeException {
    public BadAllocateException    () {
        super();
    }

    public BadAllocateException    ( String message ) {
        super(message);
    }

    public BadAllocateException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public BadAllocateException    ( Throwable cause ) {
        super(cause);
    }
}
