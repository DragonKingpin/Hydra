package com.acorn.redqueen.service.registry.grpc.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame;

import io.grpc.stub.StreamObserver;

public class GrpcServiceControlStream implements Pinenut {

    protected StreamObserver<ServiceControlFrame> mRequestObserver;

    protected volatile boolean mbActive;

    public GrpcServiceControlStream( StreamObserver<ServiceControlFrame> requestObserver ) {
        this.mRequestObserver = requestObserver;
        this.mbActive = true;
    }

    public synchronized void send( ServiceControlFrame frame ) {
        if ( !this.mbActive ) {
            throw new IllegalStateException( "gRPC service control stream is not active." );
        }
        this.mRequestObserver.onNext( frame );
    }

    public synchronized void close() {
        if ( !this.mbActive ) {
            return;
        }
        this.mbActive = false;
        this.mRequestObserver.onCompleted();
    }

    public synchronized void closeQuietly() {
        try {
            this.close();
        }
        catch ( RuntimeException ignore ) {
        }
    }

    public boolean isActive() {
        return this.mbActive;
    }

    public void markInactive() {
        this.mbActive = false;
    }

}



