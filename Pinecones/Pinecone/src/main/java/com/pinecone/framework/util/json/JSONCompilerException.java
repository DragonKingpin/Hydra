package com.pinecone.framework.util.json;

import com.pinecone.framework.system.ParseException;

public class JSONCompilerException extends ParseException {
    private static final long serialVersionUID = 0L;
    private Throwable cause;

    public JSONCompilerException    ( String what ) {
        super( what );
    }

    public JSONCompilerException    ( String what, int errorOffset ) {
        super( what, errorOffset );
    }

    public JSONCompilerException    ( String message, int errorOffset, Throwable cause ) {
        super( message, errorOffset );
        this.cause = cause;
    }

    public JSONCompilerException    ( Throwable cause, int errorOffset ) {
        super( cause.getMessage(), errorOffset );
        this.cause = cause;
    }

    public JSONCompilerException    ( Throwable cause ) {
        super( cause.getMessage() );
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }
}
