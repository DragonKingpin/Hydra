package com.pinecone.hydra.service.registry.client.transport;

import com.pinecone.framework.system.prototype.Pinenut;

public class ServiceClientTransportException extends Exception implements Pinenut {

    public ServiceClientTransportException() {
        super();
    }

    public ServiceClientTransportException( String message ) {
        super( message );
    }

    public ServiceClientTransportException( String message, Throwable cause ) {
        super( message, cause );
    }

    public ServiceClientTransportException( Throwable cause ) {
        super( cause );
    }

}
