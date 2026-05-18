package com.acorn.skynet;

import com.acorn.skynet.device.conduct.CollectiveDeviceRegiment;
import com.acorn.skynet.device.conduct.SkyCollectiveDeviceRegiment;
import com.acorn.skynet.system.SkynetSubsystem;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.io.Tracer;
import com.pinecone.framework.util.json.homotype.MapStructure;
import com.pinecone.hydra.device.ibatis.hydranium.DeviceMappingDriver;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.UniformDeviceInstrument;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.device.registry.server.UniformDeviceManager;
import com.pinecone.hydra.device.registry.ulf.HuskyDeviceAppointServer;
import com.pinecone.hydra.system.ArchModularizedSubsystem;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umc.wolf.server.UlfServer;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.tritium.system.TritiumSystem;

public class Skynet extends ArchModularizedSubsystem implements SkynetSubsystem {

    protected CollectiveDeviceRegiment      mDeviceRegiment;

    protected DeviceInstrument              mDeviceInstrument;

    protected DeviceManager                 mDeviceManager;

    @MapStructure("metaDependent.deviceInstrument")
    private String                          mszDeviceInstrumentKey;

    @MapStructure("metaDependent.deviceControlRPCDriver")
    private String                          mszDeviceControlRPCDriverKey;

    @MapStructure("metaDependent.processManager")
    private String                          mszProcessManagerKey;

    @MapStructure("kernelConfig.enableDeviceRegiment")
    private Boolean                         mbEnableDeviceRegiment;

    @MapStructure("kernelConfig.enableDeviceRPC")
    private Boolean                         mbEnableDeviceRPC;

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

        if ( !this.isEnabled( this.mbEnableDeviceRegiment ) ) {
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
        this.infoLifecycle( "<Skynet> Constructing component `DeviceManager`.", LogStatuses.StatusDone );

        if ( this.isEnabled( this.mbEnableDeviceRPC ) ) {
            this.prepare_device_rpc_server( sys );
        }

        this.mDeviceRegiment = new SkyCollectiveDeviceRegiment( this.mDeviceInstrument, this.mDeviceManager );
        this.infoLifecycle( "<Skynet> Constructing component `DeviceRegiment`.", LogStatuses.StatusDone );

        this.infoLifecycle( "<Skynet> Constructing components `Instrumentation`.", LogStatuses.StatusDone );
    }

    protected void prepare_device_rpc_server( TritiumSystem sys ) {
        MessageNode messageNode = sys.getMiddlewareDirector().getMessagersManager().getMessageNodeByName(
                this.mszDeviceControlRPCDriverKey
        );
        if ( messageNode == null ) {
            messageNode = (MessageNode) sys.getDispenserCenter().getInstanceDispenser().getRegisteredInstance(
                    this.mszDeviceControlRPCDriverKey
            );
        }
        if ( messageNode == null ) {
            this.getLogger().warn(
                    "<Skynet> Device RPC driver `{}` not found, DeviceManager RPC is not hooked.",
                    this.mszDeviceControlRPCDriverKey
            );
            return;
        }
        if ( !(messageNode instanceof UlfServer) ) {
            this.getLogger().warn(
                    "<Skynet> Device RPC driver `{}` is not UlfServer, DeviceManager RPC is not hooked.",
                    this.mszDeviceControlRPCDriverKey
            );
            return;
        }

        DuplexAppointServer appointServer = new WolvesAppointServer( (UlfServer) messageNode );
        this.mDeviceManager.hookAppointServer( new HuskyDeviceAppointServer( appointServer ) );
        this.infoLifecycle( "<Skynet> Hooking component `DeviceRPCServer`.", LogStatuses.StatusDone );
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

    protected boolean isEnabled( Boolean bValue ) {
        return bValue == null || bValue;
    }
}
