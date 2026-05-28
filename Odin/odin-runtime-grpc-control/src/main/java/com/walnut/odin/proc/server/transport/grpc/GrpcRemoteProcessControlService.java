package com.walnut.odin.proc.server.transport.grpc;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.ClientReady;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrame;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessControlFrameType;
import com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessLifecycleGrpc;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcRemoteProcessControlService extends RemoteProcessLifecycleGrpc.RemoteProcessLifecycleImplBase implements Pinenut {

    protected Logger log = LoggerFactory.getLogger( this.getClass() );

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

            protected boolean mbDetached;

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
                    GrpcRemoteProcessControlService.this.log.warn(
                            "[GrpcControl] [FrameRejected] (ClientId: `{}`, Type: `{}`, Reason: `CLIENT_NOT_READY`) <Rejected>",
                            frame.getClientId(),
                            frame.getFrameType()
                    );
                    responseObserver.onNext( errorFrame( frame, "CLIENT_NOT_READY", "CLIENT_MUSTER is required before command frames." ) );
                    return;
                }
                GrpcRemoteProcessControlService.this.mTransport.acceptClientFrame( this.mSession, frame );
            }

            @Override
            public void onError( Throwable throwable ) {
                this.detachSession( "Error", GrpcRemoteProcessControlService.this.stringifyThrowable( throwable ) );
            }

            @Override
            public void onCompleted() {
                this.detachSession( "Completed", "COMPLETED" );
                responseObserver.onCompleted();
            }

            protected void bindSession( RemoteProcessControlFrame frame, StreamObserver<RemoteProcessControlFrame> responseObserver ) {
                long clientId = frame.getClientId();
                if ( frame.hasClientMuster() && frame.getClientMuster().getClientId() > 0 ) {
                    clientId = frame.getClientMuster().getClientId();
                }
                String szSessionGuid = GrpcRemoteProcessControlService.this.mTransport.nextGuidString();
                String szRemoteAddress = GrpcRemoteAddressServerInterceptor.currentRemoteAddress();
                this.mSession = new GrpcRemoteProcessControlSession( clientId, szSessionGuid, szRemoteAddress, responseObserver );
                GrpcRemoteProcessControlService.this.log.info(
                        "[GrpcControl] [ClientMuster] (ClientId: `{}`, Session: `{}`, RemoteAddress: `{}`) <Accepted>",
                        clientId,
                        szSessionGuid,
                        szRemoteAddress
                );
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
                GrpcRemoteProcessControlService.this.mTransport.remoteProcessManagerServer().markControlClientReady( clientId );
                GrpcRemoteProcessControlService.this.log.info(
                        "[GrpcControl] [ClientReady] (ClientId: `{}`, Session: `{}`, RemoteAddress: `{}`) <Sent>",
                        clientId,
                        szSessionGuid,
                        szRemoteAddress
                );
            }

            protected void detachSession( String szEvent, String szReason ) {
                if ( this.mbDetached ) {
                    return;
                }
                this.mbDetached = true;
                if ( this.mSession == null ) {
                    GrpcRemoteProcessControlService.this.log.info(
                            "[GrpcControl] [ClientDisconnected] (ClientId: `0`, Session: `-`, Reason: `{}`) <{}>",
                            szReason,
                            szEvent
                    );
                    return;
                }

                GrpcRemoteProcessControlService.this.log.info(
                        "[GrpcControl] [ClientDisconnected] (ClientId: `{}`, Session: `{}`, RemoteAddress: `{}`, Reason: `{}`) <{}>",
                        this.mSession.clientId(),
                        this.mSession.sessionGuid(),
                        this.mSession.remoteAddress(),
                        szReason,
                        szEvent
                );
                GrpcRemoteProcessControlService.this.mTransport.detachClientSession( this.mSession );
            }
        };
    }

    protected RemoteProcessControlFrame errorFrame( RemoteProcessControlFrame request, String szCode, String szMessage ) {
        return this.mTransport.frameMapper().errorFrame( request, szCode, szMessage );
    }

    protected String stringifyThrowable( Throwable throwable ) {
        if ( throwable == null ) {
            return "UNKNOWN";
        }
        String szMessage = throwable.getMessage();
        if ( szMessage == null || szMessage.trim().isEmpty() ) {
            return throwable.getClass().getName();
        }
        return throwable.getClass().getName() + ": " + szMessage;
    }

}
