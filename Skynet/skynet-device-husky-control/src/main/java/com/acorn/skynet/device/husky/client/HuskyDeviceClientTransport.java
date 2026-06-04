package com.acorn.skynet.device.husky.client;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

import com.acorn.skynet.device.husky.client.controller.PassiveDeviceManipulatedController;
import com.acorn.skynet.device.husky.protocol.HuskyDeviceControlIface;
import com.acorn.skynet.device.husky.protocol.PassiveDeviceManipulatedIface;
import com.acorn.skynet.device.husky.transformer.HuskyDeviceControlTransformer;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.device.registry.DeviceControlRPCException;
import com.pinecone.hydra.device.registry.client.DeviceClientStateSynchronizedHandler;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientDeregisterResult;
import com.pinecone.hydra.device.registry.client.entity.DeviceClientRegisterResult;
import com.pinecone.hydra.device.registry.client.control.DeviceClientManipulationHandler;
import com.pinecone.hydra.device.registry.client.transport.DeviceClientTransport;
import com.pinecone.hydra.device.registry.client.transport.DeviceClientTransportSyncReasons;
import com.pinecone.hydra.device.registry.instruction.DeviceDeregisterInstruction;
import com.pinecone.hydra.device.registry.instruction.DeviceRegisterInstruction;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.wolf.WolvesAppointClient;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.ChannelHandleException;
import com.pinecone.hydra.umc.msg.event.ChannelEventHandler;
import com.pinecone.hydra.umc.msg.event.ChannelInactiveHandler;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.ulf.util.guid.i128.GuidAllocator128V7;

public class HuskyDeviceClientTransport implements DeviceClientTransport {

    protected static final int PassiveDeviceControlLine = 2;

    protected static final long ReconnectSynchronizationDelayMillis = 2000L;

    protected UlfClient rpcClient;

    protected final Supplier<UlfClient> rpcClientSupplier;

    protected final String connectionId;

    protected DuplexAppointClient appointClient;

    protected HuskyDeviceControlIface controlIface;

    protected final GuidAllocator guidAllocator;

    protected final HuskyDeviceControlTransformer transformer;

    protected final List<DeviceClientStateSynchronizedHandler> stateSynchronizedHandlers;

    protected final List<DeviceClientManipulationHandler> manipulationHandlers;

    protected volatile boolean terminated;

    protected volatile boolean connectionDetached;

    protected volatile UlfClient eventBoundRPCClient;

    protected volatile long connectionEventVersion;

    public HuskyDeviceClientTransport( UlfClient rpcClient ) {
        this( rpcClient, new GuidAllocator128V7() );
    }

    public HuskyDeviceClientTransport( UlfClient rpcClient, GuidAllocator guidAllocator ) {
        this( rpcClient, guidAllocator, null );
    }

    public HuskyDeviceClientTransport( UlfClient rpcClient, GuidAllocator guidAllocator, Supplier<UlfClient> rpcClientSupplier ) {
        this.rpcClient = rpcClient;
        this.rpcClientSupplier = rpcClientSupplier;
        this.connectionId = UUID.randomUUID().toString();
        this.guidAllocator = guidAllocator;
        this.transformer = new HuskyDeviceControlTransformer( guidAllocator );
        this.stateSynchronizedHandlers = new CopyOnWriteArrayList<>();
        this.manipulationHandlers = new CopyOnWriteArrayList<>();
    }

    @Override
    public void open() throws DeviceControlRPCException {
        if ( this.appointClient != null && !this.appointClient.getMessageNode().isTerminated() ) {
            return;
        }

        this.prepareRPCClient();
        if ( this.rpcClient == null ) {
            throw new DeviceControlRPCException( "Husky device RPC client is null." );
        }
        this.registerRPCClientEventHandlers();

        this.appointClient = new WolvesAppointClient( this.rpcClient );
        try {
            this.appointClient.execute();
            this.appointClient.compile( HuskyDeviceControlIface.class, false );
            this.appointClient.compile( PassiveDeviceManipulatedIface.class, false );
            this.controlIface = this.appointClient.getIface( HuskyDeviceControlIface.class );
            this.appointClient.getRouteDispatcher().registerController(
                    new PassiveDeviceManipulatedController( this.guidAllocator, this::dispatchShutdownDevice )
            );
            this.appointClient.embraces( PassiveDeviceControlLine );
            this.terminated = false;
        }
        catch ( Exception e ) {
            this.controlIface = null;
            throw new DeviceControlRPCException( e );
        }
    }

    protected void prepareRPCClient() {
        if ( this.rpcClient != null && !this.rpcClient.isTerminated() ) {
            return;
        }
        if ( this.rpcClientSupplier == null ) {
            return;
        }

        this.rpcClient = this.rpcClientSupplier.get();
    }

    protected void registerRPCClientEventHandlers() {
        if ( this.rpcClient == null || this.eventBoundRPCClient == this.rpcClient ) {
            return;
        }
        this.eventBoundRPCClient = this.rpcClient;

        this.rpcClient.registerChannelInactiveHandler( new ChannelInactiveHandler() {
            @Override
            public boolean afterChannelInactive( ChannelControlBlock ccb, Object context ) throws ChannelHandleException {
                if ( !HuskyDeviceClientTransport.this.terminated ) {
                    HuskyDeviceClientTransport.this.connectionDetached = true;
                    HuskyDeviceClientTransport.this.markConnectionEvent();
                }
                return false;
            }
        } );

        this.rpcClient.registerChannelConnectedHandler( new ChannelEventHandler() {
            @Override
            public void afterEventTriggered( ChannelControlBlock block, Object context ) {
                HuskyDeviceClientTransport.this.afterRPCChannelConnected();
            }
        } );
    }

    protected void afterRPCChannelConnected() {
        if ( this.terminated || !this.connectionDetached ) {
            return;
        }

        this.scheduleControlStateSynchronized(
                DeviceClientTransportSyncReasons.StreamError,
                this.markConnectionEvent()
        );
    }

    protected synchronized long markConnectionEvent() {
        return ++this.connectionEventVersion;
    }

    protected void scheduleControlStateSynchronized( String reason, long eventVersion ) {
        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
                boolean notify = false;
                try {
                    Thread.sleep( ReconnectSynchronizationDelayMillis );
                    synchronized ( HuskyDeviceClientTransport.this ) {
                        if ( !HuskyDeviceClientTransport.this.terminated
                                && HuskyDeviceClientTransport.this.connectionDetached
                                && HuskyDeviceClientTransport.this.connectionEventVersion == eventVersion ) {
                            HuskyDeviceClientTransport.this.connectionDetached = false;
                            notify = true;
                        }
                    }
                    if ( notify ) {
                        HuskyDeviceClientTransport.this.notifyControlStateSynchronized( reason );
                    }
                }
                catch ( InterruptedException e ) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "skynet-husky-device-control-reconnected" );
        thread.setDaemon( true );
        thread.start();
    }

    @Override
    public DeviceClientRegisterResult registerDevice( DeviceRegisterInstruction instruction ) throws DeviceControlRPCException {
        try {
            this.requireOpened();
            return this.transformer.decodeRegisterResult(
                    this.controlIface.registerDevice( this.connectionId, this.transformer.encodeRegisterInstruction( instruction ) )
            );
        }
        catch ( Exception e ) {
            throw new DeviceControlRPCException( e );
        }
    }

    @Override
    public DeviceClientDeregisterResult deregisterDevice( DeviceDeregisterInstruction instruction ) throws DeviceControlRPCException {
        try {
            this.requireOpened();
            return this.transformer.decodeDeregisterResult(
                    this.controlIface.deregisterDevice( this.connectionId, this.transformer.encodeDeregisterInstruction( instruction ) )
            );
        }
        catch ( Exception e ) {
            throw new DeviceControlRPCException( e );
        }
    }

    @Override
    public void close() {
        this.terminated = true;
        if ( this.controlIface != null ) {
            try {
                this.controlIface.detachDevice( this.connectionId );
            }
            catch ( Exception ignore ) {
            }
        }
        if ( this.appointClient != null ) {
            this.appointClient.close();
            this.appointClient = null;
        }
        this.controlIface = null;
    }

    public void requestControlStateSynchronization( String reason ) {
        if ( this.terminated ) {
            return;
        }

        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
                HuskyDeviceClientTransport.this.synchronizeControlState( reason );
            }
        }, "skynet-husky-device-control-sync" );
        thread.setDaemon( true );
        thread.start();
    }

    public void synchronizeControlState( String reason ) {
        try {
            if ( this.controlIface != null && !DeviceClientTransportSyncReasons.StreamError.equals( reason ) ) {
                this.notifyControlStateSynchronized( reason );
                return;
            }

            this.closeAppointClientOnly();
            this.open();
            this.notifyControlStateSynchronized( reason );
        }
        catch ( Exception ignore ) {
        }
    }

    protected void closeAppointClientOnly() {
        if ( this.appointClient != null ) {
            this.appointClient.close();
        }
        this.appointClient = null;
        this.controlIface = null;
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

    protected void notifyControlStateSynchronized( String reason ) {
        for ( DeviceClientStateSynchronizedHandler handler : this.stateSynchronizedHandlers ) {
            handler.afterDeviceClientStateSynchronized( reason );
        }
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

    protected void dispatchShutdownDevice(
            com.pinecone.hydra.device.registry.instruction.DeviceShutdownInstruction instruction
    ) {
        for ( DeviceClientManipulationHandler handler : this.manipulationHandlers ) {
            handler.shutdownDevice( instruction );
        }
    }

    protected void requireOpened() throws DeviceControlRPCException {
        if ( this.controlIface == null ) {
            throw new DeviceControlRPCException( "Husky device client transport is not opened." );
        }
    }
}
