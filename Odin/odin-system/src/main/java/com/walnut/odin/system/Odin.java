package com.walnut.odin.system;

import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.io.Tracer;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.MapStructure;
import com.pinecone.hydra.layer.ibatis.hydranium.LayerMappingDriver;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.ProcessManagerSystema;
import com.pinecone.hydra.system.ArchModularizedSubsystem;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umc.wolf.server.UlfServer;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;
import com.pinecone.hydra.unit.vgraph.layer.VLayerInstrument;
import com.pinecone.hydra.unit.vgraph.source.AtlasMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.tritium.system.TritiumSystem;
import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.atlas.graph.UniformRuntimeAtlas;
import com.walnut.odin.atlas.mapper.OdinAtlasMappingDriver;
import com.walnut.odin.conduct.CollectiveTaskRegiment;
import com.walnut.odin.conduct.RavenCollectiveTaskRegiment;
import com.walnut.odin.conduct.schedule.RavenTaskScheduler;
import com.walnut.odin.conduct.schedule.UniformTaskScheduler;
import com.walnut.odin.proc.server.RavenRemoteProcessManagerServer;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.GenericRavenTaskConfig;
import com.walnut.odin.task.RavenTaskInstrument;
import com.walnut.odin.task.mapper.OdinUniformTaskMappingDriver;

public class Odin extends ArchModularizedSubsystem implements TaskCentralControl {

    private CollectiveTaskRegiment  mTaskRegiment;

    private LayerInstrument         mLayerInstrument;
    private RuntimeAtlasInstrument  mAtlasInstrument;

    private UniformTaskScheduler    mTaskScheduler;

    @MapStructure("metaDependent.atlasDatabase")
    private String                  mszAtlasDatabaseKey;

    @MapStructure("metaDependent.taskInstrument")
    private String                  mszTaskInstrumentKey;;

    @MapStructure("metaDependent.controlRPCDriver")
    private String                  mszControlRPCDriverKey;

    @MapStructure("metaDependent.processManager")
    private String                  mszProcessManagerKey;

    public Odin( Hydrogen primarySystem, String name, PatriarchalConfig config ) {
        super( primarySystem, name, config );

        TritiumSystem sys = (TritiumSystem) this.parentSystem();
        sys.getPrimaryConfigScope().autoInject( Odin.class, config, this );
    }

    @Override
    protected void traceWelcomeInfo() {
        Tracer console = this.mPrimarySystem.console();
        console.getOut().print( "---------------------------------------------------------------\n" );
        console.getOut().print( "\u001B[31mBean Nuts Acorn Odin\u001B[0m\n" );
        console.getOut().print( "\u001B[31mMassive Task Orchestration System \u001B[0m\n" );
        console.getOut().print( "\u001B[32mCopyright(C) 2008-2028 Bean Nuts Foundation. All rights reserved.\u001B[0m\n" );
        console.getOut().print( "---------------------------------------------------------------\n" );
    }

    protected void init() {
        this.getLogger().info( "<Odin> >>> System Booting..." );

        this.infoLifecycle( "<Odin> Domain Subsystem Initialization", LogStatuses.StatusStart );
        this.traceWelcomeInfo();
        this.prepare_system_skeleton();

        this.infoLifecycle( "<Odin> Welcome to the Odin task central control!", LogStatuses.StatusReady );
        this.infoLifecycle( "<Odin> Domain Subsystem Initialization", LogStatuses.StatusReady );
    }

    protected void prepare_instrumentation() {
        this.infoLifecycle( "<Odin> Constructing components `Instrumentation`.", LogStatuses.StatusStart );


        TritiumSystem sys = (TritiumSystem) this.parentSystem();
        KOIMappingDriver layerMappingDriver = new LayerMappingDriver(
                sys, (IbatisClient) sys.getMiddlewareDirector().getRDBManager().getRDBClientByName( this.mszAtlasDatabaseKey ),
                sys.getDispenserCenter()
        );

        AtlasMappingDriver atlasMappingDriver = new OdinAtlasMappingDriver(
                sys, (IbatisClient) sys.getMiddlewareDirector().getRDBManager().getRDBClientByName( this.mszAtlasDatabaseKey ),
                sys.getDispenserCenter()
        );

        KOIMappingDriver taskDriver = new OdinUniformTaskMappingDriver(
                sys, (IbatisClient) sys.getMiddlewareDirector().getRDBManager().getRDBClientByName( this.mszTaskInstrumentKey ),
                sys.getDispenserCenter()
        );


        CentralizedTaskInstrument taskInstrument = new RavenTaskInstrument(
                taskDriver, new GenericRavenTaskConfig( (JSONObject) this.mSubsystemConfig )
        );
        this.infoLifecycle( "<Odin> Constructing component `TaskInstrument`.", LogStatuses.StatusDone );

        this.mLayerInstrument = new VLayerInstrument( layerMappingDriver );
        this.mAtlasInstrument = new UniformRuntimeAtlas( atlasMappingDriver, taskInstrument, this.mLayerInstrument );
        this.infoLifecycle( "<Odin> Constructing component `AtlasInstrument`.", LogStatuses.StatusDone );

        MessageNode messageNode = sys.getMiddlewareDirector().getMessagersManager().getMessageNodeByName( this.mszControlRPCDriverKey );
        if ( messageNode == null ) {
            messageNode = (MessageNode) sys.getDispenserCenter().getInstanceDispenser().getRegisteredInstance( this.mszControlRPCDriverKey );
        }
        UlfServer rpcServer = (UlfServer) messageNode;
        if ( rpcServer != null ) {
            ProcessManager pm = (ProcessManager) sys.getDispenserCenter().getInstanceDispenser().getRegisteredInstance( this.mszProcessManagerKey );
            RemoteProcessManagerServer server = new RavenRemoteProcessManagerServer( pm, rpcServer );
            this.mTaskRegiment = new RavenCollectiveTaskRegiment( (ProcessManagerSystema) sys, taskInstrument, server );
        }
        this.infoLifecycle( "<Odin> Constructing component `TaskRegiment`.", LogStatuses.StatusDone );


        this.infoLifecycle( "<Odin> Constructing components `Instrumentation`.", LogStatuses.StatusDone );
    }

    protected void prepare_scheduler() {
        this.infoLifecycle( "<Odin> Constructing component `TaskScheduler`.", LogStatuses.StatusStart );

        this.mTaskScheduler = new RavenTaskScheduler(
                this.mTaskRegiment.taskInstrument(), this.mAtlasInstrument, this.mTaskRegiment.taskExecutionLauncher()
        );

        this.infoLifecycle( "<Odin> Constructing component `TaskScheduler`.", LogStatuses.StatusDone );
    }

    protected void prepare_system_skeleton() {
        this.infoLifecycle( "<Odin> Preparing system skeleton.", LogStatuses.StatusStart );

        this.prepare_instrumentation();
        this.prepare_scheduler();


        this.infoLifecycle( "<Odin> Preparing system skeleton.", LogStatuses.StatusDone );
    }

    @Override
    public void vitalize() {
        this.init();
    }

    @Override
    public void terminate() {

    }


    public LayerInstrument layerInstrument() {
        return this.mLayerInstrument;
    }

    public RuntimeAtlasInstrument atlasInstrument() {
        return this.mAtlasInstrument;
    }

    public CollectiveTaskRegiment taskRegiment() {
        return this.mTaskRegiment;
    }

    public UniformTaskScheduler taskScheduler() {
        return this.mTaskScheduler;
    }


}