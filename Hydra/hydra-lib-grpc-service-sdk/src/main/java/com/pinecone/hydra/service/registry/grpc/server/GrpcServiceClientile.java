package com.pinecone.hydra.service.registry.grpc.server;

import java.net.SocketAddress;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.pinecone.hydra.service.registry.appoint.ServiceClientile;
import com.pinecone.hydra.service.registry.appoint.ServiceAppointServer;

public class GrpcServiceClientile implements ServiceClientile {

    protected long                                      mClientId = -1;

    // connectId -> GrpcSession
    protected final ConcurrentMap<Object, GrpcSession>  mSessions;

    protected final ServiceAppointServer                mServiceAppointServer;

    protected SocketAddress                             mRemoteAddress;

    public GrpcServiceClientile( ServiceAppointServer serviceAppointServer ) {
        this.mServiceAppointServer = serviceAppointServer;
        this.mSessions             = new ConcurrentHashMap<>();
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return this.mRemoteAddress;
    }

    @Override
    public void afterNewConnectionInbound( Long clientId, Object connectId, Object connection, Object context ) {
        if ( !(connection instanceof GrpcSession) ) {
            throw new IllegalArgumentException(
                    "GrpcServiceClientile expects `GrpcSession`, but got: " + (connection == null ? "null" : connection.getClass().getName())
            );
        }

        GrpcSession session = (GrpcSession) connection;
        this.mClientId      = clientId;
        this.mRemoteAddress = session.getRemoteAddress();
        this.mSessions.put( connectId, session );
    }

    @Override
    public void afterConnectionDetach( Long clientId, Object connectId, Object connection ) {
        GrpcSession removed = this.mSessions.remove( connectId );
        if ( removed != null ) {
            removed.markClosed();
        }
    }

    @Override
    public ServiceAppointServer serviceAppointServer() {
        return this.mServiceAppointServer;
    }

    @Override
    public long getClientId() {
        return this.mClientId;
    }

    @Override
    public int connectionCount() {
        return this.mSessions.size();
    }

    @Override
    public boolean isDefunct() {
        return this.mSessions.isEmpty();
    }

    @Override
    public GrpcSession queryNativeConnection(Object connectionIdentity) {
        return this.mSessions.get( connectionIdentity );
    }

    @Override
    public Collection<?> connections() {
        return this.mSessions.values();
    }

    @Override
    public void shutdown() {
        // 尽力关闭所有 session：完成 outbound 流（server 侧主动结束）
        for ( GrpcSession s : this.mSessions.values() ) {
            if (s == null) {
                continue;
            }
            if ( s.markClosed() ) {
                try {
                    // outbound 是 StreamObserver<?>，onCompleted 可以安全调用（若已结束会抛异常则忽略）
                    @SuppressWarnings("unchecked")
                    io.grpc.stub.StreamObserver<Object> out = (io.grpc.stub.StreamObserver<Object>) s.getOutbound();
                    out.onCompleted();
                }
                catch (Throwable ignored) {
                    // best-effort close
                }
            }
        }
        this.mSessions.clear();
    }
}