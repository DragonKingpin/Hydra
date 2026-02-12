package com.pinecone.hydra.service.registry.grpc.server;

import io.grpc.stub.StreamObserver;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class GrpcSession {

    private final String            connectId;
    private final SocketAddress     remoteAddress;
    private final StreamObserver<?> outbound;
    private final AtomicBoolean     closed = new AtomicBoolean(false);
    private volatile long           lastHeartbeatTime = System.currentTimeMillis();

    public void refreshHeartbeat() {
        this.lastHeartbeatTime = System.currentTimeMillis();
    }

    public boolean isTimeout( long timeoutMillis ) {
        return System.currentTimeMillis() - this.lastHeartbeatTime > timeoutMillis;
    }

    public GrpcSession( String connectId, SocketAddress remoteAddress, StreamObserver<?> outbound ) {
        this.connectId      = connectId;
        this.remoteAddress  = remoteAddress;
        this.outbound       = outbound;
    }

    public String getConnectId() {
        return this.connectId;
    }

    public SocketAddress getRemoteAddress() {
        return this.remoteAddress;
    }

    public StreamObserver<?> getOutbound() {
        return this.outbound;
    }

    public boolean isClosed() {
        return this.closed.get();
    }

    public boolean markClosed() {
        return this.closed.compareAndSet(false, true);
    }

}