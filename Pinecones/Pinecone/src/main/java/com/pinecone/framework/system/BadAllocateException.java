package com.pinecone.framework.system;

public class BadAllocateException extends PineRuntimeException {
    public BadAllocateException() {
        super();
    }

    public BadAllocateException( String message ) {
        super( message );
    }

    public BadAllocateException( String message, Throwable cause ) {
        super( message, cause );
    }

    public BadAllocateException( Throwable cause ) {
        super(cause);
    }
}
