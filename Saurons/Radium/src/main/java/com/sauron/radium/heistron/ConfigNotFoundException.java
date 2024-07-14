package com.sauron.radium.heistron;

import com.pinecone.framework.system.PineRuntimeException;

public class ConfigNotFoundException extends PineRuntimeException {
    public ConfigNotFoundException() {
        super();
    }

    public ConfigNotFoundException( String message ) {
        super( message );
    }

    public ConfigNotFoundException( String message, Throwable cause ) {
        super( message, cause );
    }

    public ConfigNotFoundException( Throwable cause ) {
        super(cause);
    }
}
