package com.acorn.redqueen.service.registry.husky.client;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.acorn.redqueen.service.registry.husky.client.controller.PassiveServiceManipulatedController;
import com.acorn.redqueen.service.registry.husky.client.transformer.HuskyServiceLifecycleTransformer;
import com.acorn.redqueen.service.registry.husky.client.port.HuskyServiceLifecyclePort;
import com.acorn.redqueen.service.registry.husky.client.port.HuskyServiceMetaPort;
import com.acorn.redqueen.service.registry.husky.protocol.PassiveServiceManipulatedIface;
import com.acorn.redqueen.service.registry.husky.protocol.ServiceLifecycleIface;
import com.acorn.redqueen.service.registry.husky.protocol.ServiceMetaManipulationIface;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.registry.client.control.ServiceClientManipulationHandler;
import com.pinecone.hydra.service.registry.client.port.ServicePort;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransport;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportException;
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

    protected HuskyServiceLifecycleTransformer mLifecycleTransformer;

    protected HuskyServiceLifecyclePort mLifecyclePort;

    protected HuskyServiceMetaPort mMetaPort;

    protected List<ServiceClientManipulationHandler> mManipulationHandlers;

    public HuskyServiceClientTransport(
            UlfClient rpcClient,
            GuidAllocator guidAllocator,
            HuskyServiceClientTransportConfig config
    ) {
        this.mRPCClient = rpcClient;
        this.mGuidAllocator = guidAllocator;
        this.mConfig = config == null ? new HuskyServiceClientTransportConfig() : config;
        this.mLifecycleTransformer = new HuskyServiceLifecycleTransformer( guidAllocator );
        this.mManipulationHandlers = new CopyOnWriteArrayList<>();
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
            this.mLifecyclePort = new HuskyServiceLifecyclePort(
                    this.getClientId(),
                    this.mLifecycleIface,
                    this.mLifecycleTransformer
            );
            this.mMetaPort = new HuskyServiceMetaPort( this.mMetaIface );
        }
        catch ( Exception e ) {
            this.mLifecycleIface = null;
            this.mMetaIface = null;
            this.mLifecyclePort = null;
            this.mMetaPort = null;
            throw new ServiceClientTransportException( e );
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
            this.mLifecyclePort = null;
            this.mMetaPort = null;
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
