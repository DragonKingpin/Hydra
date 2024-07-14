package com.pinecone.framework.util.json;

import com.pinecone.framework.system.ParseException;

public class JSONParseException extends ParseException {
    private static final long serialVersionUID = 0L;
    private Throwable cause;

    public JSONParseException    ( String what ) {
        super( what );
    }

    public JSONParseException    ( String what, int errorOffset ) {
        super( what, errorOffset );
    }

    public JSONParseException    ( String message, int errorOffset, Throwable cause ) {
        super( message, errorOffset );
        this.cause = cause;
    }

    public JSONParseException    ( Throwable cause, int errorOffset ) {
        super( cause.getMessage(), errorOffset );
        this.cause = cause;
    }

    public JSONParseException    ( Throwable cause ) {
        super( cause.getMessage() );
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }

}
