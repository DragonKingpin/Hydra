package com.acorn.redqueen.service.conduct;

import com.pinecone.framework.system.prototype.Pinenut;

public class ServiceLegionaryException extends Exception implements Pinenut {

    public ServiceLegionaryException( String message ) {
        super( message );
    }

    public ServiceLegionaryException( Throwable cause ) {
        super( cause );
    }

    public ServiceLegionaryException( String message, Throwable cause ) {
        super( message, cause );
    }

}

