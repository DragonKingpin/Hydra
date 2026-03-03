package com.walnut.odin.proc;

public class RemoteProcessLifecycleException extends RemoteProcessServiceException {

    public RemoteProcessLifecycleException() {
        super();
    }

    public RemoteProcessLifecycleException( String message ) {
        super(message);
    }

    public RemoteProcessLifecycleException( String message, Throwable cause ) {
        super(message, cause);
    }

    public RemoteProcessLifecycleException( Throwable cause ) {
        super(cause);
    }

}