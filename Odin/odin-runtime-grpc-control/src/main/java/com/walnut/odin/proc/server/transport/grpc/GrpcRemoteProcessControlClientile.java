package com.walnut.odin.proc.server.transport.grpc;

import com.walnut.odin.proc.server.transport.GenericRemoteProcessControlClientile;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransport;

public class GrpcRemoteProcessControlClientile extends GenericRemoteProcessControlClientile {

    public GrpcRemoteProcessControlClientile( long clientId, RemoteProcessControlTransport transport ) {
        super( clientId, transport );
    }

}
