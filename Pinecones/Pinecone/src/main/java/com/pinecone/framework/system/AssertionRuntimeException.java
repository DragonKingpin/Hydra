package com.pinecone.framework.system;

public class AssertionRuntimeException extends PineRuntimeException {
    public AssertionRuntimeException() {
        super();
    }

    public AssertionRuntimeException( String message ) {
        super( message );
    }

    public AssertionRuntimeException( String message, Throwable cause ) {
        super( message, cause );
    }

    public AssertionRuntimeException( Throwable cause ) {
        super(cause);
    }
}
