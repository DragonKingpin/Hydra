package com.pinecone.hydra.system.component;

import com.pinecone.framework.system.prototype.Pinenut;

public class ComponentInitializationException extends Exception implements Pinenut {
    public ComponentInitializationException    () {
        super();
    }

    public ComponentInitializationException    ( String message ) {
        super(message);
    }

    public ComponentInitializationException    ( String message, Throwable cause ) {
        super(message, cause);
    }

    public ComponentInitializationException    ( Throwable cause ) {
        super(cause);
    }

    protected ComponentInitializationException ( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}