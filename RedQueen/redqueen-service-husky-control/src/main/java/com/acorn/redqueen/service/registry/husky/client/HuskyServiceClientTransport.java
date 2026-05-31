package com.acorn.redqueen.service.registry.husky.client;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

import com.acorn.redqueen.service.registry.husky.client.controller.PassiveServiceManipulatedController;
import com.acorn.redqueen.service.registry.husky.client.transformer.HuskyServiceLifecycleTransformer;
import com.acorn.redqueen.service.registry.husky.client.port.HuskyServiceLifecyclePort;
import com.acorn.redqueen.service.registry.husky.client.port.HuskyServiceMetaPort;
import com.acorn.redqueen.service.registry.husky.protocol.PassiveServiceManipulatedIface;
import com.acorn.redqueen.service.registry.husky.protocol.ServiceLifecycleIface;
import com.acorn.redqueen.service.registry.husky.protocol.ServiceMetaManipulationIface;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.registry.client.ServiceClientStateSynchronizedHandler;
import com.pinecone.hydra.service.registry.client.control.ServiceClientManipulationHandler;
import com.pinecone.hydra.service.registry.client.port.ServicePort;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransport;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportException;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportState;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportSyncReasons;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportType;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.wolf.WolvesAppointClient;
import com.pinecone.hydra.umc.wolf.client.UlfClient;

public class HuskyServiceClientTransport implements ServiceClientTransport {

    protected static final int PassiveServiceControlLine = 2;

    protected UlfClient mRPCClient;

    protected DuplexAppointClient mDuplexAppointClient;

    protected GuidAllocator mGuidAllocator;

    protected ServiceLifecycleIface mLifecycleIface;

    protected ServiceMetaManipulationIface mMetaIface;

    protected HuskyServiceClientTransportConfig mConfig;

    protected Supplier<UlfClient> mRPCClientSupplier;

    protected HuskyServiceLifecycleTransformer mLifecycleTransformer;

    protected HuskyServiceLifecyclePort mLifecyclePort;

    protected HuskyServiceMetaPort mMetaPort;

    protected List<ServiceClientManipulationHandler> mManipulationHandlers;

    protected List<ServiceClientStateSynchronizedHandler> mStateSynchronizedHandlers;

    protected volatile ServiceClientTransportState mState;

    protected volatile boolean mbTerminated;

    public HuskyServiceClientTransport(
            UlfClient rpcClient,
            GuidAllocator guidAllocator,
            HuskyServiceClientTransportConfig config
    ) {
        this( rpcClient, guidAllocator, config, null );
    }

    public HuskyServiceClientTransport(
            UlfClient rpcClient,
            GuidAllocator guidAllocator,
            HuskyServiceClientTransportConfig config,
            Supplier<UlfClient> rpcClientSupplier
    ) {
        this.mRPCClient = rpcClient;
        this.mGuidAllocator = guidAllocator;
        this.mConfig = config == null ? new HuskyServiceClientTransportConfig() : config;
        this.mRPCClientSupplier = rpcClientSupplier;
        this.mLifecycleTransformer = new HuskyServiceLifecycleTransformer( guidAllocator );
        this.mManipulationHandlers = new CopyOnWriteArrayList<>();
        this.mStateSynchronizedHandlers = new CopyOnWriteArrayList<>();
        this.mState = ServiceClientTransportState.New;
    }

    @Override
    public long getClientId() {
        return this.mRPCClient.getMessageNodeId();
    }

    @Override
    public ServiceClientTransportType transportType() {
        return ServiceClientTransportType.Husky;
    }

    @Override
    public boolean isReady() {
        return this.mDuplexAppointClient != null
                && !this.mDuplexAppointClient.getMessageNode().isTerminated()
                && this.mLifecycleIface != null
                && this.mMetaIface != null;
    }

    @Override
    public void connect() throws ServiceClientTransportException {
        if ( this.isReady() ) {
            return;
        }

        try {
            this.mState = ServiceClientTransportState.Starting;
            this.prepareRPCClient();
            this.mDuplexAppointClient = new WolvesAppointClient( this.mRPCClient );
            this.mDuplexAppointClient.execute();
            this.mDuplexAppointClient.compile( ServiceLifecycleIface.class, false );
            this.mDuplexAppointClient.compile( ServiceMetaManipulationIface.class, false );
            this.mDuplexAppointClient.compile( PassiveServiceManipulatedIface.class, false );
            this.mLifecycleIface = this.mDuplexAppointClient.getIface( ServiceLifecycleIface.class );
            this.mMetaIface = this.mDuplexAppointClient.getIface( ServiceMetaManipulationIface.class );
            this.mDuplexAppointClient.getRouteDispatcher().registerController(
                    new PassiveServiceManipulatedController( this.mGuidAllocator, this::dispatchShutdownService )
            );
            this.mDuplexAppointClient.embraces( PassiveServiceControlLine );
            this.bindPorts();
            this.mState = ServiceClientTransportState.Ready;
        }
        catch ( Exception e ) {
            this.mState = ServiceClientTransportState.Disconnected;
            this.mLifecycleIface = null;
            this.mMetaIface = null;
            throw new ServiceClientTransportException( e );
        }
    }

    protected void prepareRPCClient() {
        if ( this.mRPCClient != null && !this.mRPCClient.isTerminated() ) {
            return;
        }
        if ( this.mRPCClientSupplier == null ) {
            return;
        }

        this.mRPCClient = this.mRPCClientSupplier.get();
    }

    protected void bindPorts() {
        if ( this.mLifecyclePort == null ) {
            this.mLifecyclePort = new HuskyServiceLifecyclePort(
                    this.getClientId(),
                    this.mLifecycleIface,
                    this.mLifecycleTransformer
            );
        }
        else {
            this.mLifecyclePort.bind( this.getClientId(), this.mLifecycleIface );
        }

        if ( this.mMetaPort == null ) {
            this.mMetaPort = new HuskyServiceMetaPort( this.mMetaIface );
        }
        else {
            this.mMetaPort.bind( this.mMetaIface );
        }
    }

    protected void dispatchShutdownService(
            com.pinecone.hydra.service.registry.client.control.ServiceClientShutdownInstruction instruction
    ) {
        for ( ServiceClientManipulationHandler handler : this.mManipulationHandlers ) {
            handler.shutdownService( instruction );
        }
    }

    @Override
    public <T extends ServicePort> T getPort( Class<T> portClass ) throws ServiceClientTransportException {
        this.ensureReady();
        if ( portClass.isInstance( this.mLifecyclePort ) ) {
            return portClass.cast( this.mLifecyclePort );
        }
        if ( portClass.isInstance( this.mMetaPort ) ) {
            return portClass.cast( this.mMetaPort );
        }

        throw new ServiceClientTransportException( "Unsupported Husky service port: " + portClass.getName() );
    }

    @Override
    public void disconnect() {
        this.mbTerminated = true;
        try {
            if ( this.mDuplexAppointClient != null ) {
                this.mDuplexAppointClient.close();
            }
            else if ( this.mRPCClient != null ) {
                this.mRPCClient.close();
            }
        }
        finally {
            this.mDuplexAppointClient = null;
            this.mLifecycleIface = null;
            this.mMetaIface = null;
            this.mState = ServiceClientTransportState.Terminated;
        }
    }

    public void requestControlStateSynchronization( String szReason ) {
        if ( this.mbTerminated ) {
            return;
        }

        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
                HuskyServiceClientTransport.this.synchronizeControlState( szReason );
            }
        }, "redqueen-husky-service-control-sync" );
        thread.setDaemon( true );
        thread.start();
    }

    public void synchronizeControlState( String szReason ) {
        try {
            if ( this.isReady() && !ServiceClientTransportSyncReasons.StreamError.equals( szReason ) ) {
                this.mState = ServiceClientTransportState.Ready;
                this.notifyControlStateSynchronized( szReason );
                return;
            }

            this.mState = ServiceClientTransportState.Synchronizing;
            this.closeAppointClientOnly();
            this.connect();
            this.notifyControlStateSynchronized( szReason );
        }
        catch ( Exception e ) {
            this.mState = ServiceClientTransportState.Disconnected;
        }
    }

    protected void closeAppointClientOnly() {
        if ( this.mDuplexAppointClient != null ) {
            this.mDuplexAppointClient.close();
        }
        this.mDuplexAppointClient = null;
        this.mLifecycleIface = null;
        this.mMetaIface = null;
    }

    @Override
    public void registerStateSynchronizedHandler( ServiceClientStateSynchronizedHandler handler ) {
        if ( handler == null || this.mStateSynchronizedHandlers.contains( handler ) ) {
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

    protected void notifyControlStateSynchronized( String szReason ) {
        for ( ServiceClientStateSynchronizedHandler handler : this.mStateSynchronizedHandlers ) {
            handler.afterServiceClientStateSynchronized( szReason );
        }
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

    public DuplexAppointClient duplexAppointClient() {
        return this.mDuplexAppointClient;
    }

    protected void ensureReady() throws ServiceClientTransportException {
        if ( this.isReady() ) {
            return;
        }
        this.connect();
    }

}
