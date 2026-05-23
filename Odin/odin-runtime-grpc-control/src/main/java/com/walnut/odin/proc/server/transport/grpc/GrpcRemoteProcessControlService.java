package com.walnut.odin.proc.server.transport.grpc;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.ClientReady;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrameType;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessLifecycleGrpc;

import io.grpc.stub.StreamObserver;

public class GrpcRemoteProcessControlService extends RemoteProcessLifecycleGrpc.RemoteProcessLifecycleImplBase implements Pinenut {

    protected GrpcRemoteProcessControlTransport mTransport;

    public GrpcRemoteProcessControlService( GrpcRemoteProcessControlTransport transport ) {
        this.mTransport = transport;
    }

    public GrpcRemoteProcessControlTransport transport() {
        return this.mTransport;
    }

    @Override
    public StreamObserver<RemoteProcessControlFrame> control( StreamObserver<RemoteProcessControlFrame> responseObserver ) {
        return new StreamObserver<RemoteProcessControlFrame>() {

            protected GrpcRemoteProcessControlSession mSession;

            @Override
            public void onNext( RemoteProcessControlFrame frame ) {
                if ( frame == null ) {
                    return;
                }
                if ( frame.getFrameType() == RemoteProcessControlFrameType.CLIENT_MUSTER ) {
                    this.bindSession( frame, responseObserver );
                    return;
                }
                if ( this.mSession == null ) {
                    responseObserver.onNext( errorFrame( frame, "CLIENT_NOT_READY", "CLIENT_MUSTER is required before command frames." ) );
                    return;
                }
                GrpcRemoteProcessControlService.this.mTransport.acceptClientFrame( this.mSession, frame );
            }

            @Override
            public void onError( Throwable throwable ) {
                if ( this.mSession != null ) {
                    GrpcRemoteProcessControlService.this.mTransport.detachClientSession( this.mSession );
                }
            }

            @Override
            public void onCompleted() {
                if ( this.mSession != null ) {
                    GrpcRemoteProcessControlService.this.mTransport.detachClientSession( this.mSession );
                }
                responseObserver.onCompleted();
            }

            protected void bindSession( RemoteProcessControlFrame frame, StreamObserver<RemoteProcessControlFrame> responseObserver ) {
                long clientId = frame.getClientId();
                if ( frame.hasClientMuster() && frame.getClientMuster().getClientId() > 0 ) {
                    clientId = frame.getClientMuster().getClientId();
                }
                String szSessionGuid = GrpcRemoteProcessControlService.this.mTransport.nextGuidString();
                this.mSession = new GrpcRemoteProcessControlSession( clientId, szSessionGuid, responseObserver );
                GrpcRemoteProcessControlService.this.mTransport.bindClientSession( this.mSession );
                responseObserver.onNext(
                        RemoteProcessControlFrame.newBuilder()
                                .setFrameGuid( GrpcRemoteProcessControlService.this.mTransport.nextGuidString() )
                                .setCorrelationGuid( frame.getFrameGuid() )
                                .setClientId( clientId )
                                .setCreateTimeMillis( System.currentTimeMillis() )
                                .setFrameType( RemoteProcessControlFrameType.CLIENT_READY )
                                .setClientReady(
                                        ClientReady.newBuilder()
                                                .setClientId( clientId )
                                                .setSessionGuid( szSessionGuid )
                                                .build()
                                )
                                .build()
                );
            }
        };
    }

    protected RemoteProcessControlFrame errorFrame( RemoteProcessControlFrame request, String szCode, String szMessage ) {
        return this.mTransport.frameMapper().errorFrame( request, szCode, szMessage );
    }

}
