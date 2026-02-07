package com.pinecone.framework.system;

public class RuntimeInstantiationException extends RuntimeConstructionException {
    public RuntimeInstantiationException() {
        super();
    }

    public RuntimeInstantiationException( String message ) {
        super( message );
    }

    public RuntimeInstantiationException( String message, Throwable cause ) {
        super( message, cause );
    }

    public RuntimeInstantiationException( Throwable cause ) {
        super(cause);
    }
}
