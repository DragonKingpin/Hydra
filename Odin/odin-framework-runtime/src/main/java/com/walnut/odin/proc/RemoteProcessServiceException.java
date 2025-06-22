package com.walnut.odin.proc;

import com.pinecone.framework.system.prototype.Pinenut;

public class RemoteProcessServiceException extends Exception implements Pinenut {

    public RemoteProcessServiceException() {
        super();
    }

    public RemoteProcessServiceException( String message ) {
        super(message);
    }

    public RemoteProcessServiceException( String message, Throwable cause ) {
        super(message, cause);
    }

    public RemoteProcessServiceException( Throwable cause ) {
        super(cause);
    }

}