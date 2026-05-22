package com.pinecone.framework.system;

import com.pinecone.framework.system.prototype.Pinenut;

public class BadAllocateException extends Exception implements Pinenut {
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
