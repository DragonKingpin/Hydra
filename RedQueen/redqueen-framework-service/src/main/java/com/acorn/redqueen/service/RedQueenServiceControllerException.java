package com.acorn.redqueen.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.ServiceControlException;

public class RedQueenServiceControllerException extends ServiceControlException implements Pinenut {
    public RedQueenServiceControllerException() {
        super();
    }

    public RedQueenServiceControllerException( String message ) {
        super(message);
    }

    public RedQueenServiceControllerException( String message, Throwable cause ) {
        super(message, cause);
    }

    public RedQueenServiceControllerException( Throwable cause ) {
        super(cause);
    }
}
