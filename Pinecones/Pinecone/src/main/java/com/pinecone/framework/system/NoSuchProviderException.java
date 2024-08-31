package com.pinecone.framework.system;

public class NoSuchProviderException extends Exception {
    public NoSuchProviderException() {
        super();
    }

    public NoSuchProviderException( String message ) {
        super( message );
    }

    public NoSuchProviderException( String message, Throwable cause ) {
        super( message, cause );
    }

    public NoSuchProviderException( Throwable cause ) {
        super(cause);
    }
}
