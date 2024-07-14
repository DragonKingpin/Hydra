package com.pinecone.framework.system;

public class ParseException extends PineRuntimeException {
    protected int errorOffset;

    public int getErrorOffset () {
        return errorOffset;
    }

    public ParseException    ( String what ) {
        this( what, -1 );
    }

    public ParseException    ( String what, int errorOffset ) {
        super( what );
        this.errorOffset = errorOffset;
    }

    public ParseException    ( String message, int errorOffset, Throwable cause ) {
        super( message, cause );
        this.errorOffset = errorOffset;
    }

    public ParseException    ( Throwable cause, int errorOffset ) {
        super(cause.getMessage(), cause);
        this.errorOffset = errorOffset;
    }

    public ParseException    ( Throwable cause ) {
        this( cause, -1 );
    }
}
