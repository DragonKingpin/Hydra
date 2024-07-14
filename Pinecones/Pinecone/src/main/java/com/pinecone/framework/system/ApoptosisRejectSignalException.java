package com.pinecone.framework.system;

public class ApoptosisRejectSignalException extends PineRuntimeException {
    public ApoptosisRejectSignalException() {
        super();
    }

    public ApoptosisRejectSignalException( String message ) {
        super( message );
    }

    public ApoptosisRejectSignalException( String message, Throwable cause ) {
        super( message, cause );
    }

    public ApoptosisRejectSignalException( Throwable cause ) {
        super(cause);
    }
}
