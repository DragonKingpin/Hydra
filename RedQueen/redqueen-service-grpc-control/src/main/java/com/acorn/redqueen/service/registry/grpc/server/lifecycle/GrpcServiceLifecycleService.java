package com.acorn.redqueen.service.registry.grpc.server.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.system.prototype.Pinenut;
import com.acorn.redqueen.service.registry.grpc.server.GrpcRemoteAddressServerInterceptor;
import com.acorn.redqueen.service.registry.grpc.server.GrpcServiceControlSession;
import com.acorn.redqueen.service.registry.grpc.server.GrpcServiceControlTransport;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrameType;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlGrpc;

import io.grpc.stub.StreamObserver;

public class GrpcServiceLifecycleService extends ServiceControlGrpc.ServiceControlImplBase implements Pinenut {

    protected Logger mLogger = LoggerFactory.getLogger( this.getClass() );

    protected GrpcServiceControlTransport mTransport;

    public GrpcServiceLifecycleService( GrpcServiceControlTransport transport ) {
        this.mTransport = transport;
    }

    @Override
    public StreamObserver<ServiceControlFrame> control( StreamObserver<ServiceControlFrame> responseObserver ) {
        return new StreamObserver<ServiceControlFrame>() {

            protected GrpcServiceControlSession mSession;

            protected boolean mbDetached;

            @Override
            public void onNext( ServiceControlFrame frame ) {
                if ( frame == null ) {
                    return;
                }

                if ( frame.getFrameType() == ServiceControlFrameType.CLIENT_MUSTER ) {
                    this.bindSession( frame, responseObserver );
                    return;
                }

                if ( this.mSession == null ) {
                    responseObserver.onNext(
                            GrpcServiceLifecycleService.this.mTransport.frameMapper().errorFrame(
                                    frame,
                                    frame.getClientId(),
                                    "",
                                    "CLIENT_NOT_READY",
                                    "CLIENT_MUSTER is required before command frames."
                            )
                    );
                    return;
                }

                this.mSession.touchActive();
                try {
                    this.dispatchFrame( frame );
                }
                catch ( Exception e ) {
                    GrpcServiceLifecycleService.this.mLogger.warn(
                            "[GrpcServiceControl] [FrameDispatch] (ClientId: `{}`, Session: `{}`, Type: `{}`) <Failure>",
                            this.mSession.clientId(),
                            this.mSession.sessionGuid(),
                            frame.getFrameType(),
                            e
                    );
                    responseObserver.onNext(
                            GrpcServiceLifecycleService.this.mTransport.frameMapper().errorFrame(
                                    frame,
                                    this.mSession.clientId(),
                                    this.mSession.sessionGuid(),
                                    e.getClass().getSimpleName(),
                                    GrpcServiceLifecycleService.this.stringifyThrowable( e )
                            )
                    );
                }
            }

            @Override
            public void onError( Throwable throwable ) {
                this.detachSession( "Error", GrpcServiceLifecycleService.this.stringifyThrowable( throwable ) );
            }

            @Override
            public void onCompleted() {
                this.detachSession( "Completed", "COMPLETED" );
                responseObserver.onCompleted();
            }

            protected void bindSession( ServiceControlFrame frame, StreamObserver<ServiceControlFrame> responseObserver ) {
                if ( this.mSession != null && this.mSession.isActive() ) {
                    this.mSession.touchActive();
                    this.mSession.send(
                            GrpcServiceLifecycleService.this.mTransport.frameMapper().clientReadyFrame(
                                    frame,
                                    this.mSession
                            )
                    );
                    return;
                }

                long nClientId = frame.getClientId();
                if ( frame.hasClientMuster() && frame.getClientMuster().getClientId() > 0 ) {
                    nClientId = frame.getClientMuster().getClientId();
                }

                String szSessionGuid = GrpcServiceLifecycleService.this.mTransport.nextGuidString();
                String szRemoteAddress = GrpcRemoteAddressServerInterceptor.currentRemoteAddress();
                this.mSession = new GrpcServiceControlSession( nClientId, szSessionGuid, szRemoteAddress, responseObserver );
                GrpcServiceLifecycleService.this.mTransport.bindClientSession( this.mSession );
                responseObserver.onNext(
                        GrpcServiceLifecycleService.this.mTransport.frameMapper().clientReadyFrame( frame, this.mSession )
                );
            }

            protected void dispatchFrame( ServiceControlFrame frame ) throws Exception {
                if ( frame.getFrameType() == ServiceControlFrameType.REGISTER_SERVICE ) {
                    String szInstanceGuid = GrpcServiceLifecycleService.this.mTransport.registerService( this.mSession, frame );
                    this.mSession.send(
                            GrpcServiceLifecycleService.this.mTransport.frameMapper().registerAcceptedFrame(
                                    frame,
                                    this.mSession,
                                    szInstanceGuid
                            )
                    );
                    return;
                }

                if ( frame.getFrameType() == ServiceControlFrameType.HEARTBEAT ) {
                    GrpcServiceLifecycleService.this.mTransport.heartbeat( this.mSession, frame.getHeartbeat() );
                    return;
                }

                if ( frame.getFrameType() == ServiceControlFrameType.DEREGISTER ) {
                    GrpcServiceLifecycleService.this.mTransport.deregisterService(
                            this.mSession,
                            frame.getDeregister().getInstanceGuid()
                    );
                    this.mSession.send(
                            GrpcServiceLifecycleService.this.mTransport.frameMapper().deregisteredFrame(
                                    frame,
                                    this.mSession,
                                    frame.getDeregister().getReason()
                            )
                    );
                    this.detachSession( "Deregister", frame.getDeregister().getReason() );
                    return;
                }

                if ( frame.getFrameType() == ServiceControlFrameType.SHUTDOWN_ACCEPTED ) {
                    GrpcServiceLifecycleService.this.mLogger.info(
                            "[GrpcServiceControl] [ShutdownAccepted] (ClientId: `{}`, Session: `{}`, Instance: `{}`) <Done>",
                            this.mSession.clientId(),
                            this.mSession.sessionGuid(),
                            frame.getShutdownAccepted().getInstanceGuid()
                    );
                    return;
                }

                GrpcServiceLifecycleService.this.mLogger.info(
                        "[GrpcServiceControl] [FrameIgnored] (ClientId: `{}`, Type: `{}`) <Pass>",
                        this.mSession.clientId(),
                        frame.getFrameType()
                );
            }

            protected void detachSession( String szEvent, String szReason ) {
                if ( this.mbDetached ) {
                    return;
                }
                this.mbDetached = true;
                if ( this.mSession == null ) {
                    return;
                }

                GrpcServiceLifecycleService.this.mLogger.info(
                        "[GrpcServiceControl] [ClientDisconnected] (ClientId: `{}`, Session: `{}`, Reason: `{}`) <{}>",
                        this.mSession.clientId(),
                        this.mSession.sessionGuid(),
                        szReason,
                        szEvent
                );
                GrpcServiceLifecycleService.this.mTransport.detachClientSession( this.mSession );
            }
        };
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




