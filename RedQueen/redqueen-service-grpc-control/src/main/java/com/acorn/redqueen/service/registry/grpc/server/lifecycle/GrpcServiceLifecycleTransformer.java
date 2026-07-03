package com.acorn.redqueen.service.registry.grpc.server.lifecycle;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GuidAllocator;
import com.acorn.redqueen.service.registry.grpc.server.GrpcServiceControlSession;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ClientReady;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.Deregistered;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ErrorFrame;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.RegisterServiceAccepted;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrameType;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ShutdownServiceCommand;
import com.pinecone.framework.util.id.GUID;

public class GrpcServiceLifecycleTransformer implements Pinenut {

    protected GuidAllocator mGuidAllocator;

    public GrpcServiceLifecycleTransformer( GuidAllocator guidAllocator ) {
        this.mGuidAllocator = guidAllocator;
    }

    protected String nextGuidString() {
        return this.mGuidAllocator.nextGUID().toString();
    }

    protected ServiceControlFrame.Builder baseFrame(
            ServiceControlFrame request,
            long nClientId,
            String szSessionGuid,
            ServiceControlFrameType frameType
    ) {
        ServiceControlFrame.Builder builder = ServiceControlFrame.newBuilder()
                .setFrameGuid( this.nextGuidString() )
                .setClientId( nClientId )
                .setSessionGuid( this.safe( szSessionGuid ) )
                .setCreateTimeMillis( System.currentTimeMillis() )
                .setFrameType( frameType );
        if ( request != null ) {
            builder.setCorrelationGuid( request.getFrameGuid() );
        }
        return builder;
    }

    public ServiceControlFrame clientReadyFrame( ServiceControlFrame request, GrpcServiceControlSession session ) {
        return this.baseFrame( request, session.clientId(), session.sessionGuid(), ServiceControlFrameType.CLIENT_READY )
                .setClientReady(
                        ClientReady.newBuilder()
                                .setClientId( session.clientId() )
                                .setSessionGuid( session.sessionGuid() )
                                .setRemoteAddress( this.safe( session.remoteAddress() ) )
                                .setServerTimeMillis( System.currentTimeMillis() )
                                .build()
                )
                .build();
    }

    public ServiceControlFrame registerAcceptedFrame( ServiceControlFrame request, GrpcServiceControlSession session, String szInstanceGuid ) {
        return this.baseFrame( request, session.clientId(), session.sessionGuid(), ServiceControlFrameType.REGISTER_ACCEPTED )
                .setRegisterAccepted(
                        RegisterServiceAccepted.newBuilder()
                                .setInstanceGuid( this.safe( szInstanceGuid ) )
                                .setServiceGuid( request.getRegisterService().getServiceGuid() )
                                .setSessionGuid( session.sessionGuid() )
                                .setStatus( "ONLINE" )
                                .setRegisterTimeMillis( System.currentTimeMillis() )
                                .build()
                )
                .build();
    }

    public ServiceControlFrame deregisteredFrame( ServiceControlFrame request, GrpcServiceControlSession session, String szReason ) {
        return this.baseFrame( request, session.clientId(), session.sessionGuid(), ServiceControlFrameType.DEREGISTERED )
                .setDeregistered(
                        Deregistered.newBuilder()
                                .setInstanceGuid( this.safe( session.instanceGuid() ) )
                                .setStatus( "DEREGISTERED" )
                                .setReason( this.safe( szReason ) )
                                .build()
                )
                .build();
    }

    public ServiceControlFrame shutdownServiceFrame(
            GrpcServiceControlSession session,
            GUID instanceGuid,
            String szReason
    ) {
        return this.baseFrame( null, session.clientId(), session.sessionGuid(), ServiceControlFrameType.SHUTDOWN_SERVICE )
                .setShutdownService(
                        ShutdownServiceCommand.newBuilder()
                                .setInstanceGuid( this.safe( instanceGuid == null ? null : instanceGuid.toString() ) )
                                .setReason( this.safe( szReason ) )
                                .build()
                )
                .build();
    }

    public ServiceControlFrame errorFrame( ServiceControlFrame request, long nClientId, String szSessionGuid, String szCode, String szMessage ) {
        return this.baseFrame( request, nClientId, szSessionGuid, ServiceControlFrameType.ERROR )
                .setError(
                        ErrorFrame.newBuilder()
                                .setCode( this.safe( szCode ) )
                                .setMessage( this.safe( szMessage ) )
                                .build()
                )
                .build();
    }

    protected String safe( String szValue ) {
        if ( szValue == null ) {
            return "";
        }
        return szValue;
    }
}




