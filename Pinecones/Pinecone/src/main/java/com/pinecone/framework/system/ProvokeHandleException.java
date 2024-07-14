package com.pinecone.framework.system;

public class ProvokeHandleException extends PineRuntimeException {
    public ProvokeHandleException() {
        super();
    }

    public ProvokeHandleException( String message ) {
        super( message );
    }

    public ProvokeHandleException( String message, Throwable cause ) {
        super( message, cause );
    }

    public ProvokeHandleException( Throwable cause ) {
        super(cause);
    }
}
