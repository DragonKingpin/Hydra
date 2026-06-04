package com.acorn.skynet.device.grpc.client;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrame;
import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleFrameType;
import com.acorn.skynet.device.grpc.protocol.lifecycle.proto.DeviceLifecycleGrpc;
import com.acorn.skynet.device.grpc.transformer.GrpcDeviceLifecycleTransformer;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.device.registry.DeviceControlRPCException;
import com.pinecone.hydra.device.registry.client.DeviceClientStateSynchronizedHandler;
import com.pinecone.hydra.device.registry.client.control.DeviceClientManipulationHandler;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientDeregisterResult;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientRegisterResult;
import com.pinecone.hydra.device.registry.client.transport.DeviceClientTransport;
import com.pinecone.hydra.device.registry.client.transport.DeviceClientTransportState;
import com.pinecone.hydra.device.registry.client.transport.DeviceClientTransportSyncReasons;
import com.pinecone.hydra.device.registry.instruction.DeviceDeregisterInstruction;
import com.pinecone.hydra.device.registry.instruction.DeviceRegisterInstruction;
import com.pinecone.hydra.device.registry.instruction.DeviceShutdownInstruction;
import com.pinecone.hydra.grpc.client.GrpcAppointClient;

import io.grpc.stub.StreamObserver;

public class GrpcDeviceClientTransport implements DeviceClientTransport {

    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

    protected final String name;

    protected final GrpcAppointClient grpcAppointClient;

    protected final GuidAllocator guidAllocator;

    protected final GrpcDeviceClientTransportConfig config;

    protected final GrpcDeviceLifecycleTransformer transformer;

    protected final GrpcCorrelationWaiter<DeviceLifecycleFrame> correlationWaiter;

    protected final GrpcDeviceClientStateSynchronizer stateSynchronizer;

    protected final List<DeviceClientStateSynchronizedHandler> stateSynchronizedHandlers;

    protected final List<DeviceClientManipulationHandler> manipulationHandlers;

    protected volatile DeviceClientTransportState state;

    protected volatile GrpcDeviceLifecycleStream lifecycleStream;

    protected volatile String sessionGuid;

    protected volatile boolean terminated;

    public GrpcDeviceClientTransport(
            String name,
            GrpcAppointClient grpcAppointClient,
            GuidAllocator guidAllocator,
            GrpcDeviceClientTransportConfig config
    ) {
        this.name = name;
        this.grpcAppointClient = grpcAppointClient;
        this.guidAllocator = guidAllocator;
        this.config = config == null ? new GrpcDeviceClientTransportConfig() : config;
        this.transformer = new GrpcDeviceLifecycleTransformer( guidAllocator );
        this.correlationWaiter = new GrpcCorrelationWaiter<>();
        this.stateSynchronizer = new GrpcDeviceClientStateSynchronizer( this );
        this.stateSynchronizedHandlers = new CopyOnWriteArrayList<>();
        this.manipulationHandlers = new CopyOnWriteArrayList<>();
        this.state = DeviceClientTransportState.Closed;
    }

    public GrpcDeviceClientTransport(
            GrpcAppointClient grpcAppointClient,
            GuidAllocator guidAllocator,
            GrpcDeviceClientTransportConfig config
    ) {
        this( grpcAppointClient.getName(), grpcAppointClient, guidAllocator, config );
    }

    public long getClientId() {
        return this.grpcAppointClient.getClientId();
    }

    public boolean isTerminated() {
        return this.terminated;
    }

    public boolean isLifecycleReady() {
        return this.state == DeviceClientTransportState.Open
                && this.lifecycleStream != null
                && this.lifecycleStream.isActive();
    }

    @Override
    public void open() throws DeviceControlRPCException {
        try {
            this.state = DeviceClientTransportState.Closed;
            if ( this.grpcAppointClient.isShutdown() ) {
                this.grpcAppointClient.execute();
            }
            this.synchronizeLifecycleState( DeviceClientTransportSyncReasons.Startup );
            this.terminated = false;
        }
        catch ( Exception e ) {
            throw new DeviceControlRPCException( e );
        }
    }

    public void synchronizeLifecycleState( String reason ) throws DeviceControlRPCException {
        if ( !this.stateSynchronizer.synchronizeBlocking( reason ) ) {
            throw new DeviceControlRPCException( "gRPC device lifecycle synchronization failed: " + reason );
        }
    }

    @Override
    public void requestControlStateSynchronization( String reason ) {
        if ( this.terminated ) {
            return;
        }
        this.stateSynchronizer.requestSynchronize( reason );
    }

    public boolean synchronizeLifecycleStateOnce( String reason ) {
        try {
            this.state = DeviceClientTransportState.Closed;
            this.ensureLifecycleStream();
            DeviceLifecycleFrame muster = this.transformer.clientMusterFrame( this.getClientId(), this.name );
            DeviceLifecycleFrame ready = this.sendAndAwait( muster, this.config.getLifecycleSyncTimeoutMillis() );
            if ( ready == null || ready.getFrameType() != DeviceLifecycleFrameType.CLIENT_READY ) {
                this.markDisconnected();
                return false;
            }

            this.sessionGuid = ready.getClientReady().getSessionGuid();
            this.state = DeviceClientTransportState.Open;
            this.notifyControlStateSynchronized( reason );
            this.logger.info(
                    "[GrpcDeviceClientTransport] [LifecycleReady] (ClientId: `{}`, Session: `{}`, Reason: `{}`) <Done>",
                    this.getClientId(),
                    this.sessionGuid,
                    reason
            );
            return true;
        }
        catch ( Exception e ) {
            this.logger.warn(
                    "[GrpcDeviceClientTransport] [LifecycleSync] (ClientId: `{}`, Reason: `{}`) <Failure>",
                    this.getClientId(),
                    reason,
                    e
            );
            this.markDisconnected();
            return false;
        }
    }

    @Override
    public DeviceClientRegisterResult registerDevice( DeviceRegisterInstruction instruction ) throws DeviceControlRPCException {
        try {
            this.ensureLifecycleReady();
            DeviceLifecycleFrame frame = this.transformer.registerFrame( this.getClientId(), this.sessionGuid, instruction );
            DeviceLifecycleFrame response = this.sendAndAwait( frame, this.config.getCommandTimeoutMillis() );
            if ( response == null || response.getFrameType() != DeviceLifecycleFrameType.REGISTER_ACCEPTED ) {
                throw new DeviceControlRPCException( this.describeUnexpectedFrame( "REGISTER_ACCEPTED", response ) );
            }
            return this.transformer.toRegisterResult( response.getRegisterAccepted() );
        }
        catch ( Exception e ) {
            throw new DeviceControlRPCException( e );
        }
    }

    @Override
    public DeviceClientDeregisterResult deregisterDevice( DeviceDeregisterInstruction instruction ) throws DeviceControlRPCException {
        try {
            if ( !this.isLifecycleReady() ) {
                DeviceClientDeregisterResult result = new DeviceClientDeregisterResult();
                result.setInstanceGuid( instruction == null ? null : instruction.getInstanceGuid() );
                return result;
            }

            DeviceLifecycleFrame frame = this.transformer.deregisterFrame( this.getClientId(), this.sessionGuid, instruction );
            DeviceLifecycleFrame response = this.sendAndAwait( frame, this.config.getCommandTimeoutMillis() );
            if ( response == null || response.getFrameType() != DeviceLifecycleFrameType.DEREGISTERED ) {
                throw new DeviceControlRPCException( this.describeUnexpectedFrame( "DEREGISTERED", response ) );
            }
            return this.transformer.toDeregisterResult( response.getDeregistered() );
        }
        catch ( Exception e ) {
            throw new DeviceControlRPCException( e );
        }
    }

    @Override
    public void registerStateSynchronizedHandler( DeviceClientStateSynchronizedHandler handler ) {
        if ( handler == null || this.stateSynchronizedHandlers.contains( handler ) ) {
            return;
        }
        this.stateSynchronizedHandlers.add( handler );
    }

    @Override
    public void deregisterStateSynchronizedHandler( DeviceClientStateSynchronizedHandler handler ) {
        if ( handler == null ) {
            return;
        }
        this.stateSynchronizedHandlers.remove( handler );
    }

    @Override
    public void registerManipulationHandler( DeviceClientManipulationHandler handler ) {
        if ( handler == null || this.manipulationHandlers.contains( handler ) ) {
            return;
        }
        this.manipulationHandlers.add( handler );
    }

    @Override
    public void deregisterManipulationHandler( DeviceClientManipulationHandler handler ) {
        if ( handler == null ) {
            return;
        }
        this.manipulationHandlers.remove( handler );
    }

    @Override
    public void close() {
        this.terminated = true;
        GrpcDeviceLifecycleStream stream = this.lifecycleStream;
        this.lifecycleStream = null;
        if ( stream != null ) {
            stream.closeQuietly();
        }
        this.grpcAppointClient.close();
        this.state = DeviceClientTransportState.Closed;
    }

    protected synchronized void ensureLifecycleStream() {
        if ( this.lifecycleStream != null && this.lifecycleStream.isActive() ) {
            return;
        }
        try {
            if ( this.grpcAppointClient.isShutdown() ) {
                this.grpcAppointClient.execute();
            }
        }
        catch ( Exception e ) {
            throw new IllegalStateException( "gRPC device lifecycle channel cannot be opened.", e );
        }

        DeviceLifecycleGrpc.DeviceLifecycleStub stub = DeviceLifecycleGrpc.newStub( this.grpcAppointClient.getChannel() );
        StreamObserver<DeviceLifecycleFrame> requestObserver = stub.lifecycle( new StreamObserver<DeviceLifecycleFrame>() {
            @Override
            public void onNext( DeviceLifecycleFrame frame ) {
                GrpcDeviceClientTransport.this.acceptServerFrame( frame );
            }

            @Override
            public void onError( Throwable throwable ) {
                GrpcDeviceClientTransport.this.handleStreamBroken( throwable );
            }

            @Override
            public void onCompleted() {
                GrpcDeviceClientTransport.this.handleStreamBroken(
                        new IllegalStateException( "gRPC device lifecycle stream completed." )
                );
            }
        } );
        this.lifecycleStream = new GrpcDeviceLifecycleStream( requestObserver );
    }

    protected void ensureLifecycleReady() throws DeviceControlRPCException {
        if ( this.isLifecycleReady() ) {
            return;
        }
        this.synchronizeLifecycleState( DeviceClientTransportSyncReasons.StreamRecovered );
    }

    protected DeviceLifecycleFrame sendAndAwait( DeviceLifecycleFrame frame, long timeoutMillis ) throws Exception {
        this.correlationWaiter.prepare( frame.getFrameGuid() );
        try {
            this.lifecycleStream.send( frame );
            return this.correlationWaiter.await( frame.getFrameGuid(), timeoutMillis );
        }
        catch ( Exception e ) {
            this.correlationWaiter.completeExceptionally( frame.getFrameGuid(), e );
            throw e;
        }
    }

    protected void acceptServerFrame( DeviceLifecycleFrame frame ) {
        if ( frame == null ) {
            return;
        }

        if ( frame.getFrameType() == DeviceLifecycleFrameType.ERROR ) {
            this.correlationWaiter.completeExceptionally(
                    frame.getCorrelationGuid(),
                    new DeviceControlRPCException( frame.getError().getMessage() )
            );
            return;
        }

        if ( frame.getFrameType() == DeviceLifecycleFrameType.SHUTDOWN_DEVICE ) {
            this.acceptShutdownDeviceFrame( frame );
            return;
        }

        this.correlationWaiter.complete( frame.getCorrelationGuid(), frame );
    }

    protected void acceptShutdownDeviceFrame( DeviceLifecycleFrame frame ) {
        DeviceShutdownInstruction instruction = this.transformer.toShutdownInstruction( frame.getShutdownDevice() );
        for ( DeviceClientManipulationHandler handler : this.manipulationHandlers ) {
            handler.shutdownDevice( instruction );
        }

        if ( this.isLifecycleReady() ) {
            this.lifecycleStream.send(
                    this.transformer.shutdownAcceptedFrame( this.getClientId(), this.sessionGuid, frame )
            );
        }
    }

    protected void handleStreamBroken( Throwable throwable ) {
        if ( this.terminated ) {
            return;
        }

        this.logger.warn(
                "[GrpcDeviceClientTransport] [StreamBroken] (ClientId: `{}`, Reason: `{}`) <SyncRequested>",
                this.getClientId(),
                throwable == null ? "UNKNOWN" : throwable.getMessage()
        );
        this.markDisconnected();
        this.correlationWaiter.completeAllExceptionally(
                throwable == null ? new IllegalStateException( "gRPC stream broken." ) : throwable
        );
        this.requestControlStateSynchronization( DeviceClientTransportSyncReasons.StreamError );
    }

    protected void markDisconnected() {
        GrpcDeviceLifecycleStream stream = this.lifecycleStream;
        if ( stream != null ) {
            stream.markInactive();
        }
        this.lifecycleStream = null;
        this.sessionGuid = null;
        this.state = DeviceClientTransportState.Error;
    }

    protected void notifyControlStateSynchronized( String reason ) {
        for ( DeviceClientStateSynchronizedHandler handler : this.stateSynchronizedHandlers ) {
            handler.afterDeviceClientStateSynchronized( reason );
        }
    }

    protected String describeUnexpectedFrame( String expected, DeviceLifecycleFrame frame ) {
        if ( frame == null ) {
            return "Expected `" + expected + "`, but got null frame.";
        }
        return "Expected `" + expected + "`, but got `" + frame.getFrameType() + "`.";
    }
}
