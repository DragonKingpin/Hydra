package com.walnut.odin.proc;

public class RemoteProcessServiceRPCException extends RemoteProcessServiceException {

    public RemoteProcessServiceRPCException() {
        super();
    }

    public RemoteProcessServiceRPCException( String message ) {
        super(message);
    }

    public RemoteProcessServiceRPCException( String message, Throwable cause ) {
        super(message, cause);
    }

    public RemoteProcessServiceRPCException( Throwable cause ) {
        super(cause);
    }

}
