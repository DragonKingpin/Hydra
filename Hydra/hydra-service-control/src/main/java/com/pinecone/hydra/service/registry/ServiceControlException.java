package com.pinecone.hydra.service.registry;

import com.pinecone.framework.system.prototype.Pinenut;

public class ServiceControlException extends Exception implements Pinenut {

    public ServiceControlException() {
        super();
    }

    public ServiceControlException( String message ) {
        super(message);
    }

    public ServiceControlException( String message, Throwable cause ) {
        super(message, cause);
    }

    public ServiceControlException( Throwable cause ) {
        super(cause);
    }

}
