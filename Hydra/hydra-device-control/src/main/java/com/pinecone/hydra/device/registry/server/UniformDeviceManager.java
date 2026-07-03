package com.pinecone.hydra.device.registry.server;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ElementNode;
import com.pinecone.hydra.device.registry.DeviceControlRPCException;
import com.pinecone.hydra.device.registry.DeviceValidationException;
import com.pinecone.hydra.device.registry.dto.DeviceRegistrationDTO;
import com.pinecone.hydra.device.registry.server.detached.DeviceDetachedObservationConfig;
import com.pinecone.hydra.device.registry.server.detached.DeviceDetachedObservationRegistry;
import com.pinecone.hydra.device.registry.server.transport.DeviceControlTransport;
import com.pinecone.hydra.device.registry.server.transport.DeviceControlTransportRegistry;
import com.pinecone.hydra.device.registry.server.transport.UniformDeviceControlTransportRegistry;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class UniformDeviceManager implements DeviceManager {

    protected final DeviceInstrument mDeviceInstrument;

    protected final DeviceControlTransportRegistry mTransportRegistry;

    protected final DeviceLifecycleService mDeviceLifecycleService;

    protected final DeviceMetaService mDeviceMetaService;

    protected final DeviceTopologyService mDeviceTopologyService;

    protected final DeviceRuntimeService mDeviceRuntimeService;

    protected final Logger mLogger;

    protected final DeviceDetachedObservationRegistry mDetachedObservationRegistry;

    protected DeviceDetachedObservationConfig mDetachedObservationConfig;

    protected ScheduledExecutorService mDetachedObservationSweeper;

    protected ExecutorService mDetachedExpirationExecutor;

    public UniformDeviceManager( DeviceInstrument deviceInstrument ) {
        if ( deviceInstrument == null ) {
            throw new DeviceValidationException( "DeviceInstrument is required." );
        }

        this.mDeviceInstrument = deviceInstrument;
        this.mTransportRegistry = new UniformDeviceControlTransportRegistry();
        this.mDeviceLifecycleService = new DeviceLifecycleService( this );
        this.mDeviceMetaService = new DeviceMetaService( this );
        this.mDeviceTopologyService = new DeviceTopologyService( this );
        this.mDeviceRuntimeService = new DeviceRuntimeService( this );
        this.mLogger = LoggerFactory.getLogger( this.getClass() );
        this.mDetachedObservationConfig = new DeviceDetachedObservationConfig();
        this.mDetachedObservationRegistry = new DeviceDetachedObservationRegistry();
    }

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }

    @Override
    public DeviceInstrument getDeviceInstrument() {
        return this.mDeviceInstrument;
    }

    @Override
    public Collection<DeviceControlTransport> getTransports() {
        return this.mTransportRegistry.fetchTransports();
    }

    @Override
    public DeviceManager addTransport( DeviceControlTransport transport ) {
        this.mTransportRegistry.addTransport( transport );
        return this;
    }

    @Override
    public DeviceManager hookTransport( DeviceControlTransport transport ) {
        this.addTransport( transport );
        transport.hookDeviceManager( this );
        return this;
    }

    @Override
    public DeviceManager vitalizeTransport( DeviceControlTransport transport ) throws DeviceControlRPCException {
        try {
            this.hookTransport( transport );
            transport.execute();
            return this;
        }
        catch ( Exception e ) {
            throw new DeviceControlRPCException( e );
        }
    }

    @Override
    public DeviceControlTransport getTransportById( Long transportId ) {
        return this.mTransportRegistry.getTransportById( transportId );
    }

    @Override
    public DeviceControlTransport evictTransportById( Long transportId ) {
        return this.mTransportRegistry.evictTransportById( transportId );
    }

    @Override
    public int transportSize() {
        return this.mTransportRegistry.size();
    }

    @Override
    public void startDeviceManager() throws DeviceControlRPCException {
        try {
            for ( DeviceControlTransport transport : this.mTransportRegistry.fetchTransports() ) {
                if ( !transport.isStarted() ) {
                    transport.execute();
                }
            }
            this.startDetachedObservationSweeper();
            this.infoLifecycle( "Device Manager RPC Subsystem Vitalization", LogStatuses.StatusDone );
        }
        catch ( Exception e ) {
            throw new DeviceControlRPCException( e );
        }
    }

    @Override
    public void stopDeviceManager() {
        this.stopDetachedObservationSweeper();
        for ( DeviceControlTransport transport : this.mTransportRegistry.fetchTransports() ) {
            try {
                transport.close();
            }
            catch ( Exception e ) {
                this.mLogger.warn(
                        "[DeviceControl] [StopDeviceManager] (Transport: `{}`) <Failure>",
                        transport == null ? null : transport.getName(),
                        e
                );
            }
        }
    }

    @Override
    public void configureDetachedObservation( DeviceDetachedObservationConfig config ) {
        if ( config == null ) {
            this.mDetachedObservationConfig = new DeviceDetachedObservationConfig();
            return;
        }
        this.mDetachedObservationConfig = config;
        this.getLogger().info(
                "[DeviceControl] [DetachedObservation] (Enable: `{}`, GraceMillis: `{}`, SweepMillis: `{}`, ExpireAsyncThreads: `{}`, MissingAfterReconnectPolicy: `{}`) <Configured>",
                this.mDetachedObservationConfig.isEnable(),
                this.mDetachedObservationConfig.getGraceMillis(),
                this.mDetachedObservationConfig.getSweepMillis(),
                this.mDetachedObservationConfig.getExpireAsyncThreads(),
                this.mDetachedObservationConfig.getMissingAfterReconnectPolicy()
        );
    }

    @Override
    public DeviceDetachedObservationConfig detachedObservationConfig() {
        return this.mDetachedObservationConfig;
    }

    @Override
    public synchronized GUID enrollDevice( DeviceRegistrationDTO registrationDTO ) {
        this.validateRegistration( registrationDTO );
        ElementNode node = this.resolveElement( registrationDTO );
        if ( node == null ) {
            throw new DeviceValidationException( "Device element must exist before enroll." );
        }

        if ( registrationDTO.getMeta() != null ) {
            registrationDTO.getMeta().applyTo( node, this.mDeviceInstrument.getGuidAllocator() );
            this.mDeviceInstrument.update( node );
        }
        return node.getGuid();
    }

    @Override
    public ElementNode queryDeviceByPath( String path ) {
        return this.mDeviceInstrument.queryElement( path );
    }

    @Override
    public ElementNode queryDeviceByGuid( GUID guid ) {
        if ( guid == null ) {
            return null;
        }
        TreeNode node = this.mDeviceInstrument.get( guid );
        return node instanceof ElementNode ? (ElementNode) node : null;
    }

    @Override
    public void updateDevice( ElementNode elementNode ) {
        if ( elementNode == null ) {
            throw new DeviceValidationException( "Device element is required." );
        }
        this.mDeviceInstrument.update( elementNode );
    }

    @Override
    public void removeDevice( GUID guid ) {
        if ( guid != null ) {
            this.mDeviceInstrument.remove( guid );
        }
    }

    @Override
    public DeviceLifecycleService deviceLifecycleService() {
        return this.mDeviceLifecycleService;
    }

    @Override
    public DeviceMetaService deviceMetaService() {
        return this.mDeviceMetaService;
    }

    @Override
    public DeviceTopologyService deviceTopologyService() {
        return this.mDeviceTopologyService;
    }

    @Override
    public DeviceRuntimeService deviceRuntimeService() {
        return this.mDeviceRuntimeService;
    }

    @Override
    public DeviceControlTransportRegistry deviceControlTransportRegistry() {
        return this.mTransportRegistry;
    }

    public DeviceDetachedObservationRegistry detachedObservationRegistry() {
        return this.mDetachedObservationRegistry;
    }

    protected void sweepDetachedDeviceInstances() {
        if ( !this.mDetachedObservationConfig.isEnable() || this.mDetachedExpirationExecutor == null ) {
            return;
        }

        for ( var entry : this.mDetachedObservationRegistry.snapshotExpired( System.currentTimeMillis() ) ) {
            this.mDetachedExpirationExecutor.submit( () -> this.mDeviceRuntimeService.settleDetachedDeviceInstance( entry ) );
        }
    }

    protected void startDetachedObservationSweeper() {
        if ( !this.mDetachedObservationConfig.isEnable() ) {
            return;
        }
        if ( this.mDetachedObservationSweeper != null ) {
            return;
        }

        this.mDetachedExpirationExecutor = Executors.newFixedThreadPool(
                this.mDetachedObservationConfig.getExpireAsyncThreads()
        );
        this.mDetachedObservationSweeper = Executors.newSingleThreadScheduledExecutor( runnable -> {
            Thread thread = new Thread( runnable, "Skynet-DeviceDetachedObservationSweeper" );
            thread.setDaemon( true );
            return thread;
        } );
        this.mDetachedObservationSweeper.scheduleWithFixedDelay(
                this::sweepDetachedDeviceInstances,
                this.mDetachedObservationConfig.getSweepMillis(),
                this.mDetachedObservationConfig.getSweepMillis(),
                TimeUnit.MILLISECONDS
        );
        this.getLogger().info(
                "[DeviceControl] [DetachedObservation] (Enable: `{}`, GraceMillis: `{}`, SweepMillis: `{}`, ExpireAsyncThreads: `{}`, MissingAfterReconnectPolicy: `{}`) <Started>",
                this.mDetachedObservationConfig.isEnable(),
                this.mDetachedObservationConfig.getGraceMillis(),
                this.mDetachedObservationConfig.getSweepMillis(),
                this.mDetachedObservationConfig.getExpireAsyncThreads(),
                this.mDetachedObservationConfig.getMissingAfterReconnectPolicy()
        );
    }

    protected void stopDetachedObservationSweeper() {
        if ( this.mDetachedObservationSweeper != null ) {
            this.mDetachedObservationSweeper.shutdownNow();
            this.mDetachedObservationSweeper = null;
        }
        if ( this.mDetachedExpirationExecutor != null ) {
            this.mDetachedExpirationExecutor.shutdownNow();
            this.mDetachedExpirationExecutor = null;
        }
    }

    protected void validateRegistration( DeviceRegistrationDTO registrationDTO ) {
        if ( registrationDTO == null ) {
            throw new DeviceValidationException( "Device registration is required." );
        }
        if ( isBlank( registrationDTO.getGuid() ) && isBlank( registrationDTO.getPath() ) ) {
            throw new DeviceValidationException( "Device guid or path is required." );
        }
    }

    protected ElementNode resolveElement( DeviceRegistrationDTO registrationDTO ) {
        if ( !isBlank( registrationDTO.getGuid() ) ) {
            return this.queryDeviceByGuid( this.mDeviceInstrument.getGuidAllocator().parse( registrationDTO.getGuid() ) );
        }
        if ( !isBlank( registrationDTO.getPath() ) ) {
            return this.queryDeviceByPath( registrationDTO.getPath() );
        }
        return null;
    }

    protected boolean isBlank( String value ) {
        return value == null || value.trim().isEmpty();
    }
}
