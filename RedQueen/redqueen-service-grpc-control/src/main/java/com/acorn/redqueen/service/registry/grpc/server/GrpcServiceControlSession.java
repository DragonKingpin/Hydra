package com.acorn.redqueen.service.registry.grpc.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class GrpcServiceControlSession implements Pinenut {

    protected long mnClientId;

    protected String mszSessionGuid;

    protected String mszRemoteAddress;

    protected String mszInstanceGuid;

    protected volatile boolean mbActive;

    protected volatile long mnLastActiveTimeMillis;

    protected volatile long mnLastHeartbeatTimeMillis;

    protected StreamObserver<ServiceControlFrame> mResponseObserver;

    public GrpcServiceControlSession(
            long nClientId,
            String szSessionGuid,
            String szRemoteAddress,
            StreamObserver<ServiceControlFrame> responseObserver
    ) {
        this.mnClientId = nClientId;
        this.mszSessionGuid = szSessionGuid;
        this.mszRemoteAddress = szRemoteAddress;
        this.mResponseObserver = responseObserver;
        this.mbActive = true;
        this.mnLastActiveTimeMillis = System.currentTimeMillis();
        this.mnLastHeartbeatTimeMillis = 0L;
    }

    public long clientId() {
        return this.mnClientId;
    }

    public String sessionGuid() {
        return this.mszSessionGuid;
    }

    public String remoteAddress() {
        return this.mszRemoteAddress;
    }

    public String instanceGuid() {
        return this.mszInstanceGuid;
    }

    public void bindInstance( String szInstanceGuid ) {
        this.mszInstanceGuid = szInstanceGuid;
    }

    public synchronized void send( ServiceControlFrame frame ) {
        if ( !this.mbActive ) {
            return;
        }
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

    public boolean isActive() {
        return this.mbActive;
    }
}



