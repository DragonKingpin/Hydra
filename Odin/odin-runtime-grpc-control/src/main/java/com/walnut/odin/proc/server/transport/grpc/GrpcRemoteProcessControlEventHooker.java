package com.walnut.odin.proc.server.transport.grpc;

import com.walnut.odin.proc.server.transport.GenericRemoteProcessControlEventHooker;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportRegistry;

public class GrpcRemoteProcessControlEventHooker extends GenericRemoteProcessControlEventHooker {

    public GrpcRemoteProcessControlEventHooker( RemoteProcessControlTransportRegistry transportRegistry ) {
        super( transportRegistry );
    }

}
