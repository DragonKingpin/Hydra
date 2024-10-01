package com.pinecone.hydra.registry;

import com.pinecone.framework.system.ParseException;

public class SelectorParseException extends ParseException {
    public SelectorParseException( String what ) {
        this( what, -1 );
    }

    public SelectorParseException    ( String what, int errorOffset ) {
        super( what, errorOffset );
    }

    public SelectorParseException    ( String message, int errorOffset, Throwable cause ) {
        super( message, errorOffset, cause );
    }

    public SelectorParseException    ( Throwable cause, int errorOffset ) {
        super(cause.getMessage(), errorOffset, cause);
    }

    public SelectorParseException    ( Throwable cause ) {
        this( cause, -1 );
    }
}
