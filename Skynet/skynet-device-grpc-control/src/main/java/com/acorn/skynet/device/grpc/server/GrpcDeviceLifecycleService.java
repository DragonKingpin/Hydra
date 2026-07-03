package com.acorn.skynet.device.grpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame;
import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrameType;
import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleGrpc;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.device.kom.instance.DeviceInstanceEntry;

import io.grpc.stub.StreamObserver;

public class GrpcDeviceLifecycleService extends DeviceLifecycleGrpc.DeviceLifecycleImplBase implements Pinenut {

    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

    protected final GrpcDeviceLifecycleTransport transport;

    public GrpcDeviceLifecycleService( GrpcDeviceLifecycleTransport transport ) {
        this.transport = transport;
    }

    @Override
    public StreamObserver<DeviceLifecycleFrame> lifecycle( StreamObserver<DeviceLifecycleFrame> responseObserver ) {
        return new StreamObserver<DeviceLifecycleFrame>() {

            protected GrpcDeviceLifecycleSession session;

            protected boolean detached;

            @Override
            public void onNext( DeviceLifecycleFrame frame ) {
                if ( frame == null ) {
                    return;
                }

                if ( frame.getFrameType() == DeviceLifecycleFrameType.CLIENT_MUSTER ) {
                    this.bindSession( frame, responseObserver );
                    return;
                }

                if ( this.session == null ) {
                    responseObserver.onNext(
                            GrpcDeviceLifecycleService.this.transport.transformer().errorFrame(
                                    frame,
                                    frame.getClientId(),
                                    "",
                                    "CLIENT_NOT_READY",
                                    "CLIENT_MUSTER is required before lifecycle command frames."
                            )
                    );
                    return;
                }

                this.session.touchActive();
                try {
                    this.dispatchFrame( frame );
                }
                catch ( Exception e ) {
                    GrpcDeviceLifecycleService.this.logger.warn(
                            "[GrpcDeviceLifecycle] [FrameDispatch] (ClientId: `{}`, Session: `{}`, Type: `{}`) <Failure>",
                            this.session.clientId(),
                            this.session.sessionGuid(),
                            frame.getFrameType(),
                            e
                    );
                    responseObserver.onNext(
                            GrpcDeviceLifecycleService.this.transport.transformer().errorFrame(
                                    frame,
                                    this.session.clientId(),
                                    this.session.sessionGuid(),
                                    e.getClass().getSimpleName(),
                                    GrpcDeviceLifecycleService.this.stringifyThrowable( e )
                            )
                    );
                }
            }

            @Override
            public void onError( Throwable throwable ) {
                this.detachSession( "Error", GrpcDeviceLifecycleService.this.stringifyThrowable( throwable ) );
            }

            @Override
            public void onCompleted() {
                this.detachSession( "Completed", "COMPLETED" );
                responseObserver.onCompleted();
            }

            protected void bindSession( DeviceLifecycleFrame frame, StreamObserver<DeviceLifecycleFrame> responseObserver ) {
                if ( this.session != null && this.session.isActive() ) {
                    this.session.touchActive();
                    this.session.send(
                            GrpcDeviceLifecycleService.this.transport.transformer().clientReadyFrame( frame, this.session )
                    );
                    return;
                }

                long clientId = frame.getClientId();
                if ( frame.hasClientMuster() && frame.getClientMuster().getClientId() > 0 ) {
                    clientId = frame.getClientMuster().getClientId();
                }

                String sessionGuid = GrpcDeviceLifecycleService.this.transport.nextGuidString();
                String remoteAddress = GrpcRemoteAddressServerInterceptor.currentRemoteAddress();
                this.session = new GrpcDeviceLifecycleSession( clientId, sessionGuid, remoteAddress, responseObserver );
                GrpcDeviceLifecycleService.this.transport.bindClientSession( this.session );
                responseObserver.onNext(
                        GrpcDeviceLifecycleService.this.transport.transformer().clientReadyFrame( frame, this.session )
                );
            }

            protected void dispatchFrame( DeviceLifecycleFrame frame ) {
                if ( frame.getFrameType() == DeviceLifecycleFrameType.REGISTER_DEVICE ) {
                    DeviceInstanceEntry instance = GrpcDeviceLifecycleService.this.transport.registerDevice( this.session, frame );
                    this.session.send(
                            GrpcDeviceLifecycleService.this.transport.transformer().registerAcceptedFrame(
                                    frame,
                                    this.session,
                                    instance
                            )
                    );
                    return;
                }

                if ( frame.getFrameType() == DeviceLifecycleFrameType.DEREGISTER_DEVICE ) {
                    GrpcDeviceLifecycleService.this.transport.deregisterDevice( this.session, frame );
                    this.session.send(
                            GrpcDeviceLifecycleService.this.transport.transformer().deregisteredFrame(
                                    frame,
                                    this.session,
                                    frame.getDeregisterDevice().getReason()
                            )
                    );
                    this.detachSession( "Deregister", frame.getDeregisterDevice().getReason() );
                    return;
                }

                if ( frame.getFrameType() == DeviceLifecycleFrameType.SHUTDOWN_ACCEPTED ) {
                    GrpcDeviceLifecycleService.this.logger.info(
                            "[GrpcDeviceLifecycle] [ShutdownAccepted] (ClientId: `{}`, Session: `{}`, Instance: `{}`) <Done>",
                            this.session.clientId(),
                            this.session.sessionGuid(),
                            frame.getShutdownAccepted().getInstanceGuid()
                    );
                    return;
                }

                GrpcDeviceLifecycleService.this.logger.info(
                        "[GrpcDeviceLifecycle] [FrameIgnored] (ClientId: `{}`, Type: `{}`) <Pass>",
                        this.session.clientId(),
                        frame.getFrameType()
                );
            }

            protected void detachSession( String event, String reason ) {
                if ( this.detached ) {
                    return;
                }
                this.detached = true;
                if ( this.session == null ) {
                    return;
                }

                GrpcDeviceLifecycleService.this.logger.info(
                        "[GrpcDeviceLifecycle] [ClientDisconnected] (ClientId: `{}`, Session: `{}`, Reason: `{}`) <{}>",
                        this.session.clientId(),
                        this.session.sessionGuid(),
                        reason,
                        event
                );
                GrpcDeviceLifecycleService.this.transport.detachClientSession( this.session );
            }
        };
    }

    protected String stringifyThrowable( Throwable throwable ) {
        if ( throwable == null ) {
            return "UNKNOWN";
        }
        String message = throwable.getMessage();
        if ( message == null || message.trim().isEmpty() ) {
            return throwable.getClass().getName();
        }
        return throwable.getClass().getName() + ": " + message;
    }
}
