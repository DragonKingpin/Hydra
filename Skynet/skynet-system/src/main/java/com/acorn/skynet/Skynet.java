package com.acorn.skynet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.acorn.skynet.device.conduct.CollectiveDeviceRegiment;
import com.acorn.skynet.device.conduct.SkyCollectiveDeviceRegiment;
import com.acorn.skynet.device.grpc.server.GrpcDeviceLifecycleTransportFactory;
import com.acorn.skynet.device.husky.server.HuskyDeviceControlTransportFactory;
import com.acorn.skynet.system.SkynetSubsystem;
import com.pinecone.framework.system.IrrationalProvokedException;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.io.Tracer;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.MapStructure;
import com.pinecone.hydra.device.registry.server.transport.DeviceControlTransport;
import com.pinecone.hydra.device.registry.DeviceControlException;
import com.pinecone.hydra.grpc.server.GrpcAppointServer;
import com.pinecone.hydra.grpc.server.GrpcServerConfig;
import com.pinecone.hydra.device.ibatis.hydranium.DeviceMappingDriver;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.UniformDeviceInstrument;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.device.registry.server.UniformDeviceManager;
import com.pinecone.hydra.device.registry.server.detached.DeviceDetachedObservationConfig;
import com.pinecone.hydra.system.ArchModularizedSubsystem;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umc.wolf.server.UlfServer;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.tritium.system.TritiumSystem;

public class Skynet extends ArchModularizedSubsystem implements SkynetSubsystem {

    protected CollectiveDeviceRegiment      mDeviceRegiment;

    protected DeviceInstrument              mDeviceInstrument;

    protected DeviceManager                 mDeviceManager;

    protected List<DeviceControlTransport>  mDeviceControlTransports = new ArrayList<>();

    @MapStructure("metaDependent.deviceInstrument")
    private String                          mszDeviceInstrumentKey;

    @MapStructure("metaDependent.deviceControlRPCDriver")
    private String                          mszDeviceControlRPCDriverKey;

    @MapStructure("metaDependent.processManager")
    private String                          mszProcessManagerKey;

    public Skynet( Hydrogen primarySystem, String name, PatriarchalConfig config ) {
        super( primarySystem, name, config );

        TritiumSystem sys = (TritiumSystem) this.parentSystem();
        sys.getPrimaryConfigScope().autoInject( Skynet.class, config, this );
    }

    @Override
    protected void traceWelcomeInfo() {
        Tracer console = this.mPrimarySystem.console();
        console.getOut().print( "---------------------------------------------------------------\n" );
        console.getOut().print( "\u001B[31mBean Nuts Acorn Skynet\u001B[0m\n" );
        console.getOut().print( "\u001B[31mSkynet cloud computing infrastructure \u001B[0m\n" );
        console.getOut().print( "\u001B[32mCopyright(C) 2008-2028 Bean Nuts Foundation. All rights reserved.\u001B[0m\n" );
        console.getOut().print( "---------------------------------------------------------------\n" );
    }

    protected void init() {
        this.getLogger().info( "<Skynet> >>> System Booting..." );

        this.infoLifecycle( "<Skynet> Domain Subsystem Initialization", LogStatuses.StatusStart );
        this.traceWelcomeInfo();
        this.prepare_system_skeleton();

        this.infoLifecycle( "<Skynet> Welcome to the Skynet cloud computing!", LogStatuses.StatusReady );
        this.infoLifecycle( "<Skynet> Domain Subsystem Initialization", LogStatuses.StatusReady );
    }

    protected void prepare_instrumentation() {
        this.infoLifecycle( "<Skynet> Constructing components `Instrumentation`.", LogStatuses.StatusStart );

        if ( !this.isDeviceRegimentEnabled() ) {
            this.infoLifecycle( "<Skynet> Component `DeviceRegiment` disabled by config.", LogStatuses.StatusDone );
            return;
        }

        TritiumSystem sys = (TritiumSystem) this.parentSystem();
        DeviceMappingDriver deviceMappingDriver = new DeviceMappingDriver(
                sys, (IbatisClient) sys.getMiddlewareDirector().getRDBManager().getRDBClientByName( this.mszDeviceInstrumentKey ),
                sys.getDispenserCenter()
        );
        this.mDeviceInstrument = new UniformDeviceInstrument( deviceMappingDriver );
        this.infoLifecycle( "<Skynet> Constructing component `DeviceInstrument`.", LogStatuses.StatusDone );

        this.mDeviceManager = new UniformDeviceManager( this.mDeviceInstrument );
        this.configure_device_detached_observation();
        this.infoLifecycle( "<Skynet> Constructing component `DeviceManager`.", LogStatuses.StatusDone );

        this.prepare_device_control_transports();

        this.mDeviceRegiment = new SkyCollectiveDeviceRegiment( this.mDeviceInstrument, this.mDeviceManager );
        try {
            this.mDeviceRegiment.startDeviceManager();
        }
        catch ( DeviceControlException e ) {
            throw new IrrationalProvokedException( e );
        }
        this.infoLifecycle( "<Skynet> Constructing component `DeviceRegiment`.", LogStatuses.StatusDone );

        this.infoLifecycle( "<Skynet> Constructing components `Instrumentation`.", LogStatuses.StatusDone );
    }

    protected void prepare_device_control_transports() {
        JSONObject controlConfig = ( (JSONObject) this.mSubsystemConfig ).optJSONObject( "deviceControl" );
        boolean bEnableDeviceRPC = true;
        JSONArray transportConfigs = null;
        if ( controlConfig != null ) {
            bEnableDeviceRPC = controlConfig.optBoolean( "enableDeviceRPC", true );
            transportConfigs = controlConfig.optJSONArray( "transports" );
        }
        if ( !bEnableDeviceRPC ) {
            this.getLogger().info( "[DeviceControlTransport] Disabled by config. <Pass>" );
            return;
        }

        if ( transportConfigs == null || transportConfigs.isEmpty() ) {
            this.hook_husky_device_control_transport( this.mszDeviceControlRPCDriverKey );
            return;
        }

        for ( int i = 0; i < transportConfigs.length(); i++ ) {
            JSONObject transportConfig = transportConfigs.optJSONObject( i );
            if ( transportConfig == null ) {
                throw new IrrationalProvokedException( "Device control transport config at index `" + i + "` is not object." );
            }
            if ( !transportConfig.optBoolean( "enable", true ) ) {
                continue;
            }

            String szType = transportConfig.optString( "type", "" ).toLowerCase( Locale.ROOT );
            if ( "husky".equals( szType ) ) {
                String szDriver = transportConfig.optString( "driver", this.mszDeviceControlRPCDriverKey );
                this.hook_husky_device_control_transport( szDriver );
                continue;
            }
            if ( "grpc".equals( szType ) ) {
                this.hook_grpc_device_control_transport( transportConfig );
                continue;
            }

            throw new IrrationalProvokedException( "Unknown device control transport type `" + szType + "`." );
        }
    }

    protected void configure_device_detached_observation() {
        JSONObject controlConfig = ( (JSONObject) this.mSubsystemConfig ).optJSONObject( "deviceControl" );
        JSONObject detachedObservationConfig = null;
        if ( controlConfig != null ) {
            detachedObservationConfig = controlConfig.optJSONObject( "detachedObservation" );
        }
        this.mDeviceManager.configureDetachedObservation( new DeviceDetachedObservationConfig( detachedObservationConfig ) );
    }

    protected void hook_husky_device_control_transport( String szDriver ) {
        UlfServer rpcServer = this.resolve_husky_rpc_server( szDriver );
        DeviceControlTransport transport = new HuskyDeviceControlTransportFactory().create(
                this.mDeviceManager,
                new WolvesAppointServer( rpcServer )
        );
        this.mDeviceManager.addTransport( transport );
        this.mDeviceControlTransports.add( transport );
        this.getLogger().info( "[DeviceControlTransport] [Husky] (Driver: `{}`) <Hooked>", szDriver );
    }

    protected void hook_grpc_device_control_transport( JSONObject transportConfig ) {
        GrpcServerConfig grpcConfig = new GrpcServerConfig( transportConfig );
        if ( !grpcConfig.isEnabled() ) {
            return;
        }

        String szName = transportConfig.optString( "name", "SkynetGrpcDeviceControlServer" );
        long nMessageNodeId = transportConfig.optLong( "messageNodeId", grpcConfig.getPort() );
        GrpcAppointServer grpcServer = new GrpcAppointServer( szName, nMessageNodeId, grpcConfig );
        DeviceControlTransport transport = new GrpcDeviceLifecycleTransportFactory().create( this.mDeviceManager, grpcServer );
        this.mDeviceManager.addTransport( transport );
        this.mDeviceControlTransports.add( transport );
        this.getLogger().info(
                "[DeviceControlTransport] [gRPC] (Name: `{}`, Port: `{}`) <Hooked>",
                szName,
                grpcConfig.getPort()
        );
    }

    protected UlfServer resolve_husky_rpc_server( String szDriver ) {
        TritiumSystem sys = (TritiumSystem) this.parentSystem();
        Object component = null;
        MessageNode messageNode = sys.getMiddlewareDirector().getMessagersManager().getMessageNodeByName( szDriver );
        if ( messageNode != null ) {
            component = messageNode;
        }
        if ( component == null ) {
            component = sys.getDispenserCenter().getInstanceDispenser().getRegisteredInstance( szDriver );
        }
        if ( component instanceof UlfServer ) {
            this.ensure_husky_rpc_server_not_started( szDriver, (MessageNode) component );
            return (UlfServer) component;
        }

        throw new IrrationalProvokedException( "Device control RPC driver `" + szDriver + "` does not exist or is not UlfServer." );
    }

    protected void ensure_husky_rpc_server_not_started( String szDriver, MessageNode messageNode ) {
        if ( messageNode == null || messageNode.isTerminated() ) {
            return;
        }

        throw new IrrationalProvokedException(
                "Husky RPC driver `" + szDriver + "` is already running. Please set CentralManage=false and let Skynet start it after controller registration."
        );
    }

    protected void prepare_system_skeleton() {
        this.infoLifecycle( "<Skynet> Preparing system skeleton.", LogStatuses.StatusStart );

        this.prepare_instrumentation();

        this.infoLifecycle( "<Skynet> Preparing system skeleton.", LogStatuses.StatusDone );
    }

    @Override
    public void vitalize() {
        this.init();
    }

    @Override
    public void terminate() {
        if ( this.mDeviceRegiment != null ) {
            this.mDeviceRegiment.stopDeviceManager();
        }
        this.mDeviceControlTransports.clear();
    }

    @Override
    public CollectiveDeviceRegiment deviceRegiment() {
        return this.mDeviceRegiment;
    }

    @Override
    public DeviceInstrument deviceInstrument() {
        return this.mDeviceInstrument;
    }

    @Override
    public DeviceManager deviceManager() {
        return this.mDeviceManager;
    }

    protected boolean isDeviceRegimentEnabled() {
        JSONObject controlConfig = ( (JSONObject) this.mSubsystemConfig ).optJSONObject( "deviceControl" );
        if ( controlConfig != null ) {
            return controlConfig.optBoolean( "enableDeviceRegiment", true );
        }
        return true;
    }
}
