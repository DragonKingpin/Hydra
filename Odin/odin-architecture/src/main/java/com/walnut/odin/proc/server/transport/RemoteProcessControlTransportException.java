package com.walnut.odin.proc.server.transport;

import com.walnut.odin.proc.RemoteProcessServiceRPCException;

public class RemoteProcessControlTransportException extends RemoteProcessServiceRPCException {

    public RemoteProcessControlTransportException( String message ) {
        super( message );
    }

    public RemoteProcessControlTransportException( Throwable cause ) {
        super( cause );
    }

    public RemoteProcessControlTransportException( String message, Throwable cause ) {
        super( message, cause );
    }

}
