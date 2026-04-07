package com.pinecone.hydra.service.registry.grpc.server;

import java.net.SocketAddress;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Grpc;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public class ClientMetaDataInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        SocketAddress remoteAddr = call.getAttributes().get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR);

        Context ctx = Context.current().withValue(ClientAddress.CLIENT_ADDR, remoteAddr);
        return Contexts.interceptCall(ctx, call, headers, next);
    }

}

final class ClientAddress {
    private ClientAddress() {}
    public static final io.grpc.Context.Key<SocketAddress> CLIENT_ADDR = io.grpc.Context.key( "client-addr" );
}