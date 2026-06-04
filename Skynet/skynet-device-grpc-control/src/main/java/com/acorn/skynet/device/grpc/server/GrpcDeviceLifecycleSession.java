package com.acorn.skynet.device.grpc.server;

import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame;
import com.pinecone.framework.system.prototype.Pinenut;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class GrpcDeviceLifecycleSession implements Pinenut {

    protected final long clientId;

    protected final String sessionGuid;

    protected final String remoteAddress;

    protected volatile String instanceGuid;

    protected volatile boolean active;

    protected volatile long lastActiveTimeMillis;

    protected final StreamObserver<DeviceLifecycleFrame> responseObserver;

    public GrpcDeviceLifecycleSession(
            long clientId,
            String sessionGuid,
            String remoteAddress,
            StreamObserver<DeviceLifecycleFrame> responseObserver
    ) {
        this.clientId = clientId;
        this.sessionGuid = sessionGuid;
        this.remoteAddress = remoteAddress;
        this.responseObserver = responseObserver;
        this.active = true;
        this.lastActiveTimeMillis = System.currentTimeMillis();
    }

    public long clientId() {
        return this.clientId;
    }

    public String sessionGuid() {
        return this.sessionGuid;
    }

    public String remoteAddress() {
        return this.remoteAddress;
    }

    public String instanceGuid() {
        return this.instanceGuid;
    }

    public void bindInstance( String instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }

    public synchronized void send( DeviceLifecycleFrame frame ) {
        if ( !this.active ) {
            return;
        }
        this.responseObserver.onNext( frame );
    }

    public synchronized void close() {
        this.active = false;
    }

    public synchronized void closeByServer( String reason ) {
        if ( !this.active ) {
            return;
        }
        this.active = false;
        try {
            this.responseObserver.onError(
                    Status.UNAVAILABLE.withDescription( reason ).asRuntimeException()
            );
        }
        catch ( RuntimeException ignore ) {
        }
    }

    public void touchActive() {
        this.lastActiveTimeMillis = System.currentTimeMillis();
    }

    public long lastActiveTimeMillis() {
        return this.lastActiveTimeMillis;
    }

    public boolean isActive() {
        return this.active;
    }
}
