package com.walnut.odin.proc.server.transport.grpc;

import java.net.SocketAddress;

import com.pinecone.framework.system.prototype.Pinenut;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Grpc;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public class GrpcRemoteAddressServerInterceptor implements ServerInterceptor, Pinenut {

    protected static final String UNKNOWN_VALUE = "-";

    protected static final Context.Key<String> GRPC_REMOTE_ADDRESS =
            Context.key( "odin-grpc-remote-address" );

    public static String currentRemoteAddress() {
        String szRemoteAddress = GRPC_REMOTE_ADDRESS.get();
        if ( szRemoteAddress == null || szRemoteAddress.trim().isEmpty() ) {
            return UNKNOWN_VALUE;
        }
        return szRemoteAddress;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next ) {
        SocketAddress remoteAddress = call.getAttributes().get( Grpc.TRANSPORT_ATTR_REMOTE_ADDR );
        String szRemoteAddress = remoteAddress == null ? UNKNOWN_VALUE : remoteAddress.toString();
        Context context = Context.current().withValue( GRPC_REMOTE_ADDRESS, szRemoteAddress );
        return Contexts.interceptCall( context, call, headers, next );
    }
}
