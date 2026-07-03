package com.walnut.odin.proc.server.transport.grpc;

import com.walnut.odin.proc.server.transport.RemoteProcessControlSession;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportType;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class GrpcRemoteProcessControlSession implements RemoteProcessControlSession {

    protected long      mnClientId;

    protected String    mszSessionGuid;

    protected String    mszRemoteAddress;

    protected volatile boolean   mbActive;

    protected volatile long      mnLastActiveTimeMillis;

    protected volatile long      mnLastHeartbeatTimeMillis;

    protected StreamObserver<RemoteProcessControlFrame> mResponseObserver;

    public GrpcRemoteProcessControlSession( long clientId, String szSessionGuid, String szRemoteAddress, StreamObserver<RemoteProcessControlFrame> responseObserver ) {
        this.mnClientId         = clientId;
        this.mszSessionGuid     = szSessionGuid;
        this.mszRemoteAddress   = szRemoteAddress;
        this.mResponseObserver  = responseObserver;
        this.mbActive           = true;
        this.mnLastActiveTimeMillis    = System.currentTimeMillis();
        this.mnLastHeartbeatTimeMillis = 0L;
    }

    public String sessionGuid() {
        return this.mszSessionGuid;
    }

    public String remoteAddress() {
        return this.mszRemoteAddress;
    }

    public StreamObserver<RemoteProcessControlFrame> responseObserver() {
        return this.mResponseObserver;
    }

    public synchronized void send( RemoteProcessControlFrame frame ) {
        this.mResponseObserver.onNext( frame );
    }

    public synchronized void close() {
        this.mbActive = false;
    }

    public synchronized void closeByServer( String szReason ) {
        if ( !this.mbActive ) {
            return;
        }
        this.mbActive = false;
        try {
            this.mResponseObserver.onError(
                    Status.UNAVAILABLE
                            .withDescription( szReason )
                            .asRuntimeException()
            );
        }
        catch ( RuntimeException ignore ) {
        }
    }

    public void touchActive() {
        this.mnLastActiveTimeMillis = System.currentTimeMillis();
    }

    public void touchHeartbeat() {
        long nNow = System.currentTimeMillis();
        this.mnLastActiveTimeMillis = nNow;
        this.mnLastHeartbeatTimeMillis = nNow;
    }

    public long lastActiveTimeMillis() {
        return this.mnLastActiveTimeMillis;
    }

    public long lastHeartbeatTimeMillis() {
        return this.mnLastHeartbeatTimeMillis;
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
