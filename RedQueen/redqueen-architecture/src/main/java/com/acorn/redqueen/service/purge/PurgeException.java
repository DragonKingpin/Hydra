package com.acorn.redqueen.service.purge;

import com.pinecone.framework.system.prototype.Pinenut;

public class PurgeException extends Exception implements Pinenut {
    public PurgeException( String message ) {
        super( message );
    }

    public PurgeException( Throwable cause ) {
        super( cause );
    }

    public PurgeException( String message, Throwable cause ) {
        super( message, cause );
    }
}
