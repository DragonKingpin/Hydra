package com.walnut.odin.proc.server.transport.grpc;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.grpc.server.GrpcAppointServer;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.proc.server.transport.RemoteProcessControlEventHooker;

public class GrpcRemoteProcessControlTransportFactory implements Pinenut {

    public GrpcRemoteProcessControlTransport create( RemoteProcessManagerServer remoteProcessManagerServer,
                                                     GrpcAppointServer grpcAppointServer,
                                                     RemoteProcessControlEventHooker eventHooker ) {
        return new GrpcRemoteProcessControlTransport( remoteProcessManagerServer, grpcAppointServer, eventHooker );
    }

}
