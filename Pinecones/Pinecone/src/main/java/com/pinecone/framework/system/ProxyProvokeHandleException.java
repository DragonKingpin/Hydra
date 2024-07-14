package com.pinecone.framework.system;

public class ProxyProvokeHandleException extends PineRuntimeException {
    public ProxyProvokeHandleException() {
        super();
    }

    public ProxyProvokeHandleException( String message ) {
        super( message );
    }

    public ProxyProvokeHandleException( String message, Throwable cause ) {
        super( message, cause );
    }

    public ProxyProvokeHandleException( Throwable cause ) {
        super(cause);
    }
}
