package com.pinecone.framework.system;

public class NotImplementedException extends PineRuntimeException {
    public NotImplementedException() {
        super();
    }

    public NotImplementedException( String message ) {
        super( message );
    }
}
