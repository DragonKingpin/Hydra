package com.pinecone.framework.util.json;

import com.pinecone.framework.system.PineRuntimeException;

public class JSONException extends PineRuntimeException {
    private static final long serialVersionUID = 0L;
    private Throwable cause;

    public JSONException    ( String what ) {
        super( what );
    }

    public JSONException( Throwable cause ) {
        super( cause.getMessage() );
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }
}