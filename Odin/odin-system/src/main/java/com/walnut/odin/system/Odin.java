package com.walnut.odin.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.io.Tracer;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.MapStructure;
import com.pinecone.hydra.grpc.server.GrpcAppointServer;
import com.pinecone.hydra.grpc.server.GrpcServerConfig;
import com.pinecone.framework.system.IrrationalProvokedException;
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
import com.walnut.odin.proc.RemoteProcessServiceRPCException;
import com.walnut.odin.proc.server.RavenRemoteProcessManagerServer;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.proc.server.transport.grpc.GrpcRemoteProcessControlEventHooker;
import com.walnut.odin.proc.server.transport.grpc.GrpcRemoteProcessControlTransportFactory;
import com.walnut.odin.proc.server.transport.husky.HuskyRemoteProcessControlTransportFactory;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.GenericRavenTaskConfig;
import com.walnut.odin.task.RavenTaskConfig;
import com.walnut.odin.task.RavenTaskInstrument;
import com.walnut.odin.task.mapper.OdinUniformTaskMappingDriver;

public class Odin extends ArchModularizedSubsystem implements TaskCentralControl {

    private CollectiveTaskRegiment  mTaskRegiment;

    private LayerInstrument         mLayerInstrument;
    private RuntimeAtlasInstrument  mAtlasInstrument;

    private UniformTaskScheduler    mTaskScheduler;

    private List<GrpcAppointServer> mAutonomousGrpcServers = new ArrayList<>();

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

        ProcessManager pm = (ProcessManager) sys.getDispenserCenter().getInstanceDispenser().getRegisteredInstance( this.mszProcessManagerKey );
        if ( pm == null ) {
            throw new IrrationalProvokedException( "ProcessManager `" + this.mszProcessManagerKey + "` does not exist." );
        }
        RemoteProcessManagerServer server = new RavenRemoteProcessManagerServer( pm );
        this.prepare_remote_process_control_transports( sys, server );
        this.mTaskRegiment = new RavenCollectiveTaskRegiment( (ProcessManagerSystema) sys, taskInstrument, server );
        this.infoLifecycle( "<Odin> Constructing component `TaskRegiment`.", LogStatuses.StatusDone );


        this.infoLifecycle( "<Odin> Constructing components `Instrumentation`.", LogStatuses.StatusDone );
    }

    protected void prepare_remote_process_control_transports( TritiumSystem sys, RemoteProcessManagerServer server ) {
        JSONObject controlConfig = ( (JSONObject) this.mSubsystemConfig ).optJSONObject( "remoteProcessControl" );
        JSONArray transportConfigs = null;
        if ( controlConfig != null ) {
            transportConfigs = controlConfig.optJSONArray( "transports" );
        }

        if ( transportConfigs == null || transportConfigs.isEmpty() ) {
            this.hook_husky_remote_process_control_transport( sys, server, this.mszControlRPCDriverKey );
            return;
        }

        for ( int i = 0; i < transportConfigs.length(); i++ ) {
            JSONObject transportConfig = transportConfigs.optJSONObject( i );
            if ( transportConfig == null ) {
                throw new IrrationalProvokedException( "Remote process control transport config at index `" + i + "` is not object." );
            }
            if ( !transportConfig.optBoolean( "enable", true ) ) {
                continue;
            }

            String szType = transportConfig.optString( "type", "" ).toLowerCase( Locale.ROOT );
            if ( "husky".equals( szType ) ) {
                String szDriver = transportConfig.optString( "driver", this.mszControlRPCDriverKey );
                this.hook_husky_remote_process_control_transport( sys, server, szDriver );
                continue;
            }
            if ( "grpc".equals( szType ) ) {
                this.hook_grpc_remote_process_control_transport( server, transportConfig );
                continue;
            }

            throw new IrrationalProvokedException( "Unknown remote process control transport type `" + szType + "`." );
        }
    }

    protected void hook_husky_remote_process_control_transport( TritiumSystem sys, RemoteProcessManagerServer server, String szDriver ) {
        UlfServer rpcServer = this.resolve_husky_rpc_server( sys, szDriver );
        server.hookTransport( HuskyRemoteProcessControlTransportFactory.create( server, rpcServer ) );
        this.getLogger().info( "[RemoteProcessControlTransport] [Husky] (Driver: `{}`) <Hooked>", szDriver );
    }

    protected UlfServer resolve_husky_rpc_server( TritiumSystem sys, String szDriver ) {
        Object component = null;
        MessageNode messageNode = sys.getMiddlewareDirector().getMessagersManager().getMessageNodeByName( szDriver );
        if ( messageNode != null ) {
            component = messageNode;
        }
        if ( component == null ) {
            component = sys.getDispenserCenter().getInstanceDispenser().getRegisteredInstance( szDriver );
        }
        if ( component instanceof UlfServer ) {
            return (UlfServer) component;
        }

        throw new IrrationalProvokedException( "Control RPC driver `" + szDriver + "` does not exist or is not UlfServer." );
    }

    protected void hook_grpc_remote_process_control_transport( RemoteProcessManagerServer server, JSONObject transportConfig ) {
        GrpcServerConfig grpcConfig = new GrpcServerConfig( transportConfig );
        if ( !grpcConfig.isEnabled() ) {
            return;
        }

        String szName = transportConfig.optString( "name", "OdinGrpcControlServer" );
        long nMessageNodeId = transportConfig.optLong( "messageNodeId", grpcConfig.getPort() );
        GrpcAppointServer grpcServer = new GrpcAppointServer( szName, nMessageNodeId, grpcConfig );

        server.hookTransport(
                new GrpcRemoteProcessControlTransportFactory().create(
                        server,
                        grpcServer,
                        new GrpcRemoteProcessControlEventHooker( server.transportRegistry() )
                )
        );
        this.mAutonomousGrpcServers.add( grpcServer );
        this.getLogger().info( "[RemoteProcessControlTransport] [gRPC] (Name: `{}`, Port: `{}`) <Hooked>", szName, grpcConfig.getPort() );
    }

    protected void prepare_remote_process_server() {
        if ( this.mTaskRegiment == null ) {
            return;
        }

        try {
            this.mTaskRegiment.startRemoteProcessServer();
        }
        catch ( RemoteProcessServiceRPCException e ) {
            throw new IrrationalProvokedException( e );
        }
    }

    protected void prepare_scheduler() {
        this.infoLifecycle( "<Odin> Constructing component `TaskScheduler`.", LogStatuses.StatusStart );

        this.mTaskScheduler = new RavenTaskScheduler(
                this.mTaskRegiment.taskInstrument(), this.mAtlasInstrument, this.mTaskRegiment.taskDispatcher()
        );

        this.infoLifecycle( "<Odin> Constructing component `TaskScheduler`.", LogStatuses.StatusDone );
    }

    protected void prepare_scheduler_cycle_engine() {
        if ( this.mTaskScheduler == null ) {
            return;
        }

        RavenTaskConfig config = this.mTaskScheduler.ravenTaskConfig();
        if ( !config.isSchedulerEnabled() ) {
            this.getLogger().info( "[OdinScheduler] [CycleEngineDisabled] (Reason: `scheduler-disabled`, ManualPulse: `true`) <Pass>" );
            return;
        }
        if ( !"single-master".equals( config.getSchedulerMode().toLowerCase( Locale.ROOT ) ) ) {
            this.getLogger().info(
                    "[OdinScheduler] [CycleEngineDisabled] (Reason: `unsupported-mode`, Mode: `{}`, ManualPulse: `true`) <Pass>",
                    config.getSchedulerMode()
            );
            return;
        }
        if ( !config.isSchedulerCycleEngineEnabled() ) {
            this.getLogger().info( "[OdinScheduler] [CycleEngineDisabled] (Reason: `cycle-engine-disabled`, ManualPulse: `true`) <Pass>" );
            return;
        }

        this.infoLifecycle( "<Odin> Starting component `TaskSchedulerCycleEngine`.", LogStatuses.StatusStart );
        this.traceSchedulerCycleEngineBanner( config );
        this.mTaskScheduler.startService();
        this.infoLifecycle( "<Odin> Starting component `TaskSchedulerCycleEngine`.", LogStatuses.StatusReady );
    }

    protected void traceSchedulerCycleEngineBanner( RavenTaskConfig config ) {
        Tracer console = this.mPrimarySystem.console();
        console.getOut().print( "---------------------------------------------------------------\n" );
        console.getOut().print( "\u001B[31mBean Nuts Acorn Odin Scheduler Cycle Engine\u001B[0m\n" );
        console.getOut().print( "Mode       : " + config.getSchedulerMode() + "\n" );
        console.getOut().print( "Partition  : " + config.getSchedulePartitionName() + "\n" );
        console.getOut().print( "Node       : " + config.getSchedulerNodeId() + "\n" );
        console.getOut().print( "Tick       : " + Math.max( 1L, config.getScheduleCycleEngineTickMillis() ) + " ms\n" );
        console.getOut().print( "Hourly     : " + config.getScheduleCycleEngineHourlyPulseMillis() + " ms\n" );
        console.getOut().print( "Daily      : " + config.getScheduleCycleEngineDailyPulseMillis() + " ms\n" );
        console.getOut().print( "Recovery   : " + config.getScheduleCycleEngineRecoveryPulseMillis() + " ms\n" );
        console.getOut().print( "---------------------------------------------------------------\n" );
    }

    protected void prepare_system_skeleton() {
        this.infoLifecycle( "<Odin> Preparing system skeleton.", LogStatuses.StatusStart );

        this.prepare_instrumentation();
        this.prepare_remote_process_server();
        this.prepare_scheduler();
        this.prepare_scheduler_cycle_engine();


        this.infoLifecycle( "<Odin> Preparing system skeleton.", LogStatuses.StatusDone );
    }

    @Override
    public void vitalize() {
        this.init();
    }

    @Override
    public void terminate() {
        if ( this.mTaskScheduler != null ) {
            this.mTaskScheduler.terminateService();
        }
        if ( this.mTaskRegiment != null ) {
            this.mTaskRegiment.remoteProcessManagerServer().terminateService();
        }
        for ( GrpcAppointServer grpcServer : this.mAutonomousGrpcServers ) {
            grpcServer.shutdown();
        }
        this.mAutonomousGrpcServers.clear();
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
