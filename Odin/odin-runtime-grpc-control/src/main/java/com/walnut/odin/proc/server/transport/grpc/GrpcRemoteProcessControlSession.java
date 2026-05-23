package com.walnut.odin.proc.server.transport.grpc;

import com.walnut.odin.proc.server.transport.RemoteProcessControlSession;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportType;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame;

import io.grpc.stub.StreamObserver;

public class GrpcRemoteProcessControlSession implements RemoteProcessControlSession {

    protected long      mnClientId;

    protected String    mszSessionGuid;

    protected boolean   mbActive;

    protected StreamObserver<RemoteProcessControlFrame> mResponseObserver;

    public GrpcRemoteProcessControlSession( long clientId, String szSessionGuid, StreamObserver<RemoteProcessControlFrame> responseObserver ) {
        this.mnClientId         = clientId;
        this.mszSessionGuid     = szSessionGuid;
        this.mResponseObserver  = responseObserver;
        this.mbActive           = true;
    }

    public String sessionGuid() {
        return this.mszSessionGuid;
    }

    public StreamObserver<RemoteProcessControlFrame> responseObserver() {
        return this.mResponseObserver;
    }

    public void send( RemoteProcessControlFrame frame ) {
        this.mResponseObserver.onNext( frame );
    }

    public void close() {
        this.mbActive = false;
    }

    @Override
    public long clientId() {
        return this.mnClientId;
    }

    @Override
    public RemoteProcessControlTransportType transportType() {
        return RemoteProcessControlTransportType.Grpc;
    }

    @Override
    public boolean isActive() {
        return this.mbActive;
    }

}
