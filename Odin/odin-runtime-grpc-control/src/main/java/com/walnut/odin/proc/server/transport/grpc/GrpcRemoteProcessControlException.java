package com.walnut.odin.proc.server.transport.grpc;

import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportException;

public class GrpcRemoteProcessControlException extends RemoteProcessControlTransportException {

    public GrpcRemoteProcessControlException( String message ) {
        super( message );
    }

    public GrpcRemoteProcessControlException( Throwable cause ) {
        super( cause );
    }

    public GrpcRemoteProcessControlException( String message, Throwable cause ) {
        super( message, cause );
    }

}
