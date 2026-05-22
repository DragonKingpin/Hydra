package com.walnut.odin.proc.server.transport.grpc;

import com.pinecone.framework.system.prototype.Pinenut;

public class GrpcRemoteProcessControlService implements Pinenut {

    protected GrpcRemoteProcessControlTransport mTransport;

    public GrpcRemoteProcessControlService( GrpcRemoteProcessControlTransport transport ) {
        this.mTransport = transport;
    }

    public GrpcRemoteProcessControlTransport transport() {
        return this.mTransport;
    }

}
