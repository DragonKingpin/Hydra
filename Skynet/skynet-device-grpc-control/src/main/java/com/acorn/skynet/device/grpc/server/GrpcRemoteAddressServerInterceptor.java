package com.acorn.skynet.device.grpc.server;

import com.pinecone.framework.system.prototype.Pinenut;

import io.grpc.Attributes;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Grpc;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public class GrpcRemoteAddressServerInterceptor implements ServerInterceptor, Pinenut {

    protected static final Context.Key<String> REMOTE_ADDRESS = Context.key( "skynet-grpc-remote-address" );

    public static String currentRemoteAddress() {
        String remoteAddress = REMOTE_ADDRESS.get();
        return remoteAddress == null ? "" : remoteAddress;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next
    ) {
        Attributes attributes = call.getAttributes();
        Object remoteAddress = attributes.get( Grpc.TRANSPORT_ATTR_REMOTE_ADDR );
        Context context = Context.current().withValue( REMOTE_ADDRESS, String.valueOf( remoteAddress ) );
        return Contexts.interceptCall( context, call, headers, next );
    }
}
