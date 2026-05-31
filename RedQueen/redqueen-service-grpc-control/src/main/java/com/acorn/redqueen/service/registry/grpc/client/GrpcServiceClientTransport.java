package com.acorn.redqueen.service.registry.grpc.client;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acorn.redqueen.service.registry.grpc.client.port.GrpcServiceLifecyclePort;
import com.acorn.redqueen.service.registry.grpc.client.port.GrpcServiceMetaPort;
import com.acorn.redqueen.service.registry.grpc.client.transformer.GrpcServiceLifecycleTransformer;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.grpc.client.GrpcAppointClient;
import com.pinecone.hydra.service.registry.client.ServiceClientStateSynchronizedHandler;
import com.pinecone.hydra.service.registry.client.control.ServiceClientManipulationHandler;
import com.pinecone.hydra.service.registry.client.control.ServiceClientShutdownInstruction;
import com.pinecone.hydra.service.registry.client.instruction.ServiceClientDeregisterInstruction;
import com.pinecone.hydra.service.registry.client.instruction.ServiceClientRegisterInstruction;
import com.pinecone.hydra.service.registry.client.entity.ServiceClientDeregisterResult;
import com.pinecone.hydra.service.registry.client.entity.ServiceClientRegisterResult;
import com.pinecone.hydra.service.registry.client.port.ServicePort;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransport;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportException;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportState;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportSyncReasons;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportType;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrame;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlFrameType;
import com.acorn.redqueen.service.registry.grpc.protocol.lifecycle.proto.ServiceControlGrpc;

import io.grpc.stub.StreamObserver;

public class GrpcServiceClientTransport implements ServiceClientTransport {

    protected Logger mLogger = LoggerFactory.getLogger( this.getClass() );

    protected String mszName;

    protected GrpcAppointClient mGrpcAppointClient;

    protected GuidAllocator mGuidAllocator;

    protected GrpcServiceClientTransportConfig mConfig;

    protected GrpcServiceLifecycleTransformer mFrameMapper;

    protected GrpcCorrelationWaiter<ServiceControlFrame> mCorrelationWaiter;

    protected GrpcServiceClientStateSynchronizer mStateSynchronizer;

    protected GrpcServiceLifecyclePort mLifecyclePort;

    protected GrpcServiceMetaPort mMetaPort;

    protected List<ServiceClientStateSynchronizedHandler> mStateSynchronizedHandlers;

    protected List<ServiceClientManipulationHandler> mManipulationHandlers;

    protected volatile ServiceClientTransportState mState;

    protected volatile GrpcServiceControlStream mControlStream;

    protected volatile String mszSessionGuid;

    protected volatile GUID mInstanceGuid;

    protected volatile boolean mbTerminated;

    protected ScheduledExecutorService mHeartbeatExecutor;

    public GrpcServiceClientTransport(
            String szName,
            GrpcAppointClient grpcAppointClient,
            GuidAllocator guidAllocator,
            GrpcServiceClientTransportConfig config
    ) {
        this.mszName = szName;
        this.mGrpcAppointClient = grpcAppointClient;
        this.mGuidAllocator = guidAllocator;
        this.mConfig = config == null ? new GrpcServiceClientTransportConfig() : config;
        this.mFrameMapper = new GrpcServiceLifecycleTransformer( guidAllocator );
        this.mCorrelationWaiter = new GrpcCorrelationWaiter<>();
        this.mStateSynchronizer = new GrpcServiceClientStateSynchronizer( this );
        this.mStateSynchronizedHandlers = new CopyOnWriteArrayList<>();
        this.mManipulationHandlers = new CopyOnWriteArrayList<>();
        this.mState = ServiceClientTransportState.New;
        this.mLifecyclePort = new GrpcServiceLifecyclePort( this );
        this.mMetaPort = new GrpcServiceMetaPort();
    }

    public GrpcServiceClientTransport(
            GrpcAppointClient grpcAppointClient,
            GuidAllocator guidAllocator,
            GrpcServiceClientTransportConfig config
    ) {
        this( grpcAppointClient.getName(), grpcAppointClient, guidAllocator, config );
    }

    @Override
    public long getClientId() {
        return this.mGrpcAppointClient.getClientId();
    }

    public String getName() {
        return this.mszName;
    }

    public boolean isTerminated() {
        return this.mbTerminated;
    }

    public boolean isControlReady() {
        return this.mState == ServiceClientTransportState.Ready
                && this.mControlStream != null
                && this.mControlStream.isActive();
    }

    @Override
    public ServiceClientTransportType transportType() {
        return ServiceClientTransportType.Grpc;
    }

    @Override
    public boolean isReady() {
        return this.isControlReady();
    }

    @Override
    public void connect() throws ServiceClientTransportException {
        try {
            this.mState = ServiceClientTransportState.Starting;
            if ( this.mGrpcAppointClient.isShutdown() ) {
                this.mGrpcAppointClient.execute();
            }
            this.synchronizeControlState( ServiceClientTransportSyncReasons.Startup );
            this.startHeartbeatExecutor();
        }
        catch ( Exception e ) {
            throw new GrpcServiceClientTransportException( e );
        }
    }

    public void synchronizeControlState( String reason ) throws ServiceClientTransportException {
        if ( !this.mStateSynchronizer.synchronizeBlocking( reason ) ) {
            throw new GrpcServiceClientTransportException( "gRPC service control synchronization failed: " + reason );
        }
    }

    public void requestControlStateSynchronization( String reason ) {
        if ( this.mbTerminated ) {
            return;
        }
        this.mStateSynchronizer.requestSynchronize( reason );
    }

    public boolean synchronizeControlStateOnce( String szReason ) {
        try {
            this.mState = ServiceClientTransportState.Synchronizing;
            this.ensureControlStream();
            ServiceControlFrame muster = this.mFrameMapper.clientMusterFrame( this.getClientId(), this.mszName );
            ServiceControlFrame ready = this.sendAndAwait( muster, this.mConfig.getControlSyncTimeoutMillis() );
            if ( ready == null || ready.getFrameType() != ServiceControlFrameType.CLIENT_READY ) {
                this.warnUnexpectedFrame( "ClientMuster", ready, szReason );
                this.markDisconnected();
                return false;
            }

            this.mszSessionGuid = ready.getClientReady().getSessionGuid();
            this.mState = ServiceClientTransportState.Ready;
            this.notifyControlStateSynchronized( szReason );
            this.mLogger.info(
                    "[GrpcServiceClientTransport] [ControlReady] (ClientId: `{}`, Session: `{}`, Reason: `{}`) <Done>",
                    this.getClientId(),
                    this.mszSessionGuid,
                    szReason
            );
            return true;
        }
        catch ( Exception e ) {
            this.mLogger.warn(
                    "[GrpcServiceClientTransport] [ControlSync] (ClientId: `{}`, Reason: `{}`) <Failure>",
                    this.getClientId(),
                    szReason,
                    e
            );
            this.markDisconnected();
            return false;
        }
    }

    public ServiceClientRegisterResult register(
            ServiceClientRegisterInstruction instruction
    ) throws ServiceClientTransportException {
        try {
            this.ensureControlReady();
            ServiceControlFrame frame = this.mFrameMapper.registerFrame( this.getClientId(), this.mszSessionGuid, instruction );
            ServiceControlFrame response = this.sendAndAwait( frame, this.mConfig.getCommandTimeoutMillis() );
            if ( response == null || response.getFrameType() != ServiceControlFrameType.REGISTER_ACCEPTED ) {
                throw new GrpcServiceClientTransportException( this.describeUnexpectedFrame( "REGISTER_ACCEPTED", response ) );
            }

            ServiceClientRegisterResult registerResult = this.mFrameMapper.toRegisterResult(
                    response.getRegisterAccepted()
            );
            this.mInstanceGuid = registerResult.getInstanceGuid();
            return registerResult;
        }
        catch ( Exception e ) {
            throw new GrpcServiceClientTransportException( e );
        }
    }

    public ServiceClientDeregisterResult deregister(
            ServiceClientDeregisterInstruction command
    ) throws ServiceClientTransportException {
        try {
            if ( !this.isControlReady() ) {
                return this.mFrameMapper.toDeregisterResult( command, "DISCONNECTED", "Control stream is not ready." );
            }

            ServiceControlFrame frame = this.mFrameMapper.deregisterFrame( this.getClientId(), this.mszSessionGuid, command );
            ServiceControlFrame response = this.sendAndAwait( frame, this.mConfig.getCommandTimeoutMillis() );
            if ( response == null || response.getFrameType() != ServiceControlFrameType.DEREGISTERED ) {
                throw new GrpcServiceClientTransportException( this.describeUnexpectedFrame( "DEREGISTERED", response ) );
            }
            this.mInstanceGuid = null;
            return this.mFrameMapper.toDeregisterResult(
                    command,
                    response.getDeregistered().getStatus(),
                    response.getDeregistered().getReason()
            );
        }
        catch ( Exception e ) {
            throw new GrpcServiceClientTransportException( e );
        }
    }

    @Override
    public void registerStateSynchronizedHandler( ServiceClientStateSynchronizedHandler handler ) {
        if ( handler == null ) {
            return;
        }
        this.mStateSynchronizedHandlers.add( handler );
    }

    @Override
    public void deregisterStateSynchronizedHandler( ServiceClientStateSynchronizedHandler handler ) {
        if ( handler == null ) {
            return;
        }
        this.mStateSynchronizedHandlers.remove( handler );
    }

    @Override
    public void registerManipulationHandler( ServiceClientManipulationHandler handler ) {
        if ( handler == null || this.mManipulationHandlers.contains( handler ) ) {
            return;
        }
        this.mManipulationHandlers.add( handler );
    }

    @Override
    public void deregisterManipulationHandler( ServiceClientManipulationHandler handler ) {
        if ( handler == null ) {
            return;
        }
        this.mManipulationHandlers.remove( handler );
    }

    @Override
    public <T extends ServicePort> T getPort( Class<T> portClass ) throws ServiceClientTransportException {
        if ( portClass.isInstance( this.mLifecyclePort ) ) {
            return portClass.cast( this.mLifecyclePort );
        }
        if ( portClass.isInstance( this.mMetaPort ) ) {
            return portClass.cast( this.mMetaPort );
        }
        throw new ServiceClientTransportException( "Unsupported gRPC service port: " + portClass.getName() );
    }

    @Override
    public void disconnect() {
        this.mbTerminated = true;
        this.stopHeartbeatExecutor();
        GrpcServiceControlStream stream = this.mControlStream;
        this.mControlStream = null;
        if ( stream != null ) {
            stream.closeQuietly();
        }
        this.mGrpcAppointClient.close();
        this.mState = ServiceClientTransportState.Terminated;
    }

    protected synchronized void ensureControlStream() {
        if ( this.mControlStream != null && this.mControlStream.isActive() ) {
            return;
        }

        ServiceControlGrpc.ServiceControlStub stub = ServiceControlGrpc.newStub( this.mGrpcAppointClient.getChannel() );
        StreamObserver<ServiceControlFrame> requestObserver = stub.control( new StreamObserver<ServiceControlFrame>() {
            @Override
            public void onNext( ServiceControlFrame frame ) {
                GrpcServiceClientTransport.this.acceptServerFrame( frame );
            }

            @Override
            public void onError( Throwable throwable ) {
                GrpcServiceClientTransport.this.handleStreamBroken( throwable );
            }

            @Override
            public void onCompleted() {
                GrpcServiceClientTransport.this.handleStreamBroken(
                        new IllegalStateException( "gRPC service control stream completed." )
                );
            }
        } );
        this.mControlStream = new GrpcServiceControlStream( requestObserver );
    }

    protected void ensureControlReady() throws ServiceClientTransportException {
        if ( this.isControlReady() ) {
            return;
        }
        this.synchronizeControlState( ServiceClientTransportSyncReasons.StreamRecovered );
    }

    protected ServiceControlFrame sendAndAwait( ServiceControlFrame frame, long nTimeoutMillis ) throws Exception {
        this.mCorrelationWaiter.prepare( frame.getFrameGuid() );
        try {
            this.mControlStream.send( frame );
            return this.mCorrelationWaiter.await( frame.getFrameGuid(), nTimeoutMillis );
        }
        catch ( Exception e ) {
            this.mCorrelationWaiter.completeExceptionally( frame.getFrameGuid(), e );
            throw e;
        }
    }

    protected void acceptServerFrame( ServiceControlFrame frame ) {
        if ( frame == null ) {
            return;
        }

        if ( frame.getFrameType() == ServiceControlFrameType.ERROR ) {
            this.mCorrelationWaiter.completeExceptionally(
                    frame.getCorrelationGuid(),
                    new GrpcServiceClientTransportException( frame.getError().getMessage() )
            );
            return;
        }

        if ( frame.getFrameType() == ServiceControlFrameType.SHUTDOWN_SERVICE ) {
            this.acceptShutdownServiceFrame( frame );
            return;
        }

        this.mCorrelationWaiter.complete( frame.getCorrelationGuid(), frame );
    }

    protected void acceptShutdownServiceFrame( ServiceControlFrame frame ) {
        ServiceClientShutdownInstruction instruction = this.mFrameMapper.toShutdownInstruction( frame.getShutdownService() );
        for ( ServiceClientManipulationHandler handler : this.mManipulationHandlers ) {
            try {
                handler.shutdownService( instruction );
            }
            catch ( Exception e ) {
                this.mLogger.warn(
                        "[GrpcServiceClientTransport] [ShutdownHandler] (ClientId: `{}`) <Failure>",
                        this.getClientId(),
                        e
                );
            }
        }

        if ( this.isControlReady() ) {
            this.mControlStream.send(
                    this.mFrameMapper.shutdownAcceptedFrame( this.getClientId(), this.mszSessionGuid, frame )
            );
        }
    }

    protected void handleStreamBroken( Throwable throwable ) {
        if ( this.mbTerminated ) {
            return;
        }

        this.mLogger.warn(
                "[GrpcServiceClientTransport] [StreamBroken] (ClientId: `{}`, Reason: `{}`) <SyncRequested>",
                this.getClientId(),
                throwable == null ? "UNKNOWN" : throwable.getMessage()
        );
        this.markDisconnected();
        this.mCorrelationWaiter.completeAllExceptionally(
                throwable == null ? new IllegalStateException( "gRPC stream broken." ) : throwable
        );
        this.requestControlStateSynchronization( ServiceClientTransportSyncReasons.StreamError );
    }

    protected void markDisconnected() {
        GrpcServiceControlStream stream = this.mControlStream;
        if ( stream != null ) {
            stream.markInactive();
        }
        this.mControlStream = null;
        this.mszSessionGuid = null;
        this.mState = ServiceClientTransportState.Disconnected;
    }

    protected void notifyControlStateSynchronized( String szReason ) {
        for ( ServiceClientStateSynchronizedHandler handler : this.mStateSynchronizedHandlers ) {
            try {
                handler.afterServiceClientStateSynchronized( szReason );
            }
            catch ( Exception e ) {
                this.mLogger.warn(
                        "[GrpcServiceClientTransport] [SyncHandler] (Reason: `{}`) <Failure>",
                        szReason,
                        e
                );
            }
        }
    }

    protected void startHeartbeatExecutor() {
        if ( !this.mConfig.isEnableHeartbeat() ) {
            return;
        }
        if ( this.mConfig.getHeartbeatIntervalMillis() <= 0 || this.mHeartbeatExecutor != null ) {
            return;
        }
        this.mHeartbeatExecutor = Executors.newSingleThreadScheduledExecutor( runnable -> {
            Thread thread = new Thread( runnable, "redqueen-grpc-service-control-heartbeat" );
            thread.setDaemon( true );
            return thread;
        } );
        this.mHeartbeatExecutor.scheduleAtFixedRate(
                this::sendHeartbeatQuietly,
                this.mConfig.getHeartbeatIntervalMillis(),
                this.mConfig.getHeartbeatIntervalMillis(),
                TimeUnit.MILLISECONDS
        );
    }

    protected void stopHeartbeatExecutor() {
        if ( this.mHeartbeatExecutor == null ) {
            return;
        }
        this.mHeartbeatExecutor.shutdownNow();
        this.mHeartbeatExecutor = null;
    }

    protected void sendHeartbeatQuietly() {
        try {
            if ( !this.isControlReady() ) {
                return;
            }
            this.mControlStream.send(
                    this.mFrameMapper.heartbeatFrame( this.getClientId(), this.mszSessionGuid, this.mInstanceGuid )
            );
        }
        catch ( Exception e ) {
            this.mLogger.debug(
                    "[GrpcServiceClientTransport] [Heartbeat] (ClientId: `{}`) <Failure>",
                    this.getClientId(),
                    e
            );
        }
    }

    protected void warnUnexpectedFrame( String szStepName, ServiceControlFrame frame, String szReason ) {
        this.mLogger.warn(
                "[GrpcServiceClientTransport] [{}] (ClientId: `{}`, Reason: `{}`, Response: `{}`) <Unexpected>",
                szStepName,
                this.getClientId(),
                szReason,
                frame == null ? "null" : frame.getFrameType()
        );
    }

    protected String describeUnexpectedFrame( String szExpected, ServiceControlFrame frame ) {
        if ( frame == null ) {
            return "Expected `" + szExpected + "`, but got null frame.";
        }
        return "Expected `" + szExpected + "`, but got `" + frame.getFrameType() + "`.";
    }

}
