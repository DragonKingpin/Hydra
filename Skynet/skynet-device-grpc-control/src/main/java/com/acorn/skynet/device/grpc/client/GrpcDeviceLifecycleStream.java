package com.acorn.skynet.device.grpc.client;

import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame;
import com.pinecone.framework.system.prototype.Pinenut;

import io.grpc.stub.StreamObserver;

public class GrpcDeviceLifecycleStream implements Pinenut {

    protected final StreamObserver<DeviceLifecycleFrame> requestObserver;

    protected volatile boolean active;

    public GrpcDeviceLifecycleStream( StreamObserver<DeviceLifecycleFrame> requestObserver ) {
        this.requestObserver = requestObserver;
        this.active = true;
    }

    public synchronized void send( DeviceLifecycleFrame frame ) {
        if ( !this.active ) {
            throw new IllegalStateException( "gRPC device lifecycle stream is not active." );
        }
        this.requestObserver.onNext( frame );
    }

    public synchronized void close() {
        if ( !this.active ) {
            return;
        }
        this.active = false;
        this.requestObserver.onCompleted();
    }

    public synchronized void closeQuietly() {
        try {
            this.close();
        }
        catch ( RuntimeException ignore ) {
        }
    }

    public boolean isActive() {
        return this.active;
    }

    public void markInactive() {
        this.active = false;
    }
}
