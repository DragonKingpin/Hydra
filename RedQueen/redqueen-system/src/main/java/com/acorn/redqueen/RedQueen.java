package com.acorn.redqueen;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.acorn.redqueen.service.conduct.CollectiveServiceRegiment;
import com.acorn.redqueen.service.conduct.RedCollectiveServiceRegiment;
import com.acorn.redqueen.service.purge.PurgeService;
import com.pinecone.framework.system.IrrationalProvokedException;
import com.acorn.redqueen.system.ServiceCentralControl;
import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.framework.util.io.Tracer;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.homotype.MapStructure;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.UniformServiceInstrument;
import com.pinecone.hydra.service.registry.ServiceControlException;
import com.pinecone.hydra.service.registry.server.detached.ServiceDetachedObservationConfig;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.server.inspection.ServiceControlInspection;
import com.pinecone.hydra.service.registry.server.UniformServiceManager;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransport;
import com.acorn.redqueen.service.registry.grpc.server.GrpcServiceControlTransportFactory;
import com.acorn.redqueen.service.registry.husky.server.HuskyServiceControlTransportFactory;
import com.pinecone.hydra.grpc.server.GrpcAppointServer;
import com.pinecone.hydra.grpc.server.GrpcServerConfig;
import com.pinecone.hydra.system.ArchModularizedSubsystem;
import com.pinecone.hydra.system.Hydrogen;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umc.wolf.server.UlfServer;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.tritium.system.TritiumSystem;

public class RedQueen extends ArchModularizedSubsystem implements ServiceCentralControl {

    protected CollectiveServiceRegiment  mServiceRegiment;

    protected ServiceInstrument          mServiceInstrument;

    protected ServiceManager             mServiceManager;

    protected List<ServiceControlTransport> mServiceControlTransports = new ArrayList<>();

    @MapStructure("metaDependent.serviceDatabase")
    protected String                     mszServiceDatabaseKey;

    @MapStructure("metaDependent.serviceInstrument")
    protected String                     mszServiceInstrumentKey;

    @MapStructure("metaDependent.controlRPCDriver")
    protected String                     mszControlRPCDriverKey;

    @MapStructure("metaDependent.processManager")
    protected String                     mszProcessManagerKey;

    public RedQueen( Hydrogen primarySystem, String name, PatriarchalConfig config ) {
        super( primarySystem, name, config );

        TritiumSystem sys = (TritiumSystem) this.parentSystem();
        sys.getPrimaryConfigScope().autoInject( RedQueen.class, config, this );
    }

    @Override
    protected void traceWelcomeInfo() {
        Tracer console = this.mPrimarySystem.console();
        console.getOut().print( "---------------------------------------------------------------\n" );
        console.getOut().print( "\u001B[31mBean Nuts Acorn Red Queen\u001B[0m\n" );
        console.getOut().print( "\u001B[31mMassive Parallel Computing Orchestration System \u001B[0m\n" );
        console.getOut().print( "\u001B[32mCopyright(C) 2008-2028 Bean Nuts Foundation. All rights reserved.\u001B[0m\n" );
        console.getOut().print( "---------------------------------------------------------------\n" );
    }

    protected void init() {
        this.getLogger().info( "<RedQueen> >>> System Booting..." );

        this.infoLifecycle( "<RedQueen> Domain Subsystem Initialization", LogStatuses.StatusStart );
        this.traceWelcomeInfo();
        this.prepare_system_skeleton();

        this.infoLifecycle( "<RedQueen> Welcome to the Red Queen super computing!", LogStatuses.StatusReady );
        this.infoLifecycle( "<RedQueen> Domain Subsystem Initialization", LogStatuses.StatusReady );
    }

    protected void prepare_service_instrumentation() {
        this.infoLifecycle( "<RedQueen> Constructing component `ServiceInstrument`.", LogStatuses.StatusStart );

        TritiumSystem sys = (TritiumSystem) this.parentSystem();
        String szDatabaseKey = this.resolveServiceInstrumentDatabaseKey();
        KOIMappingDriver serviceMappingDriver = new ServiceMappingDriver(
                sys,
                (IbatisClient) sys.getMiddlewareDirector().getRDBManager().getRDBClientByName( szDatabaseKey ),
                sys.getDispenserCenter()
        );

        this.mServiceInstrument = new UniformServiceInstrument( serviceMappingDriver );
        this.mServiceManager = new UniformServiceManager( this.mServiceInstrument );
        this.configure_service_detached_observation();

        this.infoLifecycle( "<RedQueen> Constructing component `ServiceInstrument`.", LogStatuses.StatusDone );
    }

    protected void configure_service_detached_observation() {
        JSONObject controlConfig = ( (JSONObject) this.mSubsystemConfig ).optJSONObject( "serviceControl" );
        JSONObject detachedObservationConfig = null;
        if ( controlConfig != null ) {
            detachedObservationConfig = controlConfig.optJSONObject( "detachedObservation" );
        }
        ServiceDetachedObservationConfig config = new ServiceDetachedObservationConfig( detachedObservationConfig );
        this.mServiceManager.configureDetachedObservation( config );
        this.getLogger().info(
                "[ServiceControl] [DetachedObservation] (Enable: `{}`, GraceMillis: `{}`, SweepMillis: `{}`, ExpireAsyncThreads: `{}`, StartupRecoveryPageSize: `{}`, MissingAfterReconnectPolicy: `{}`) <Configured>",
                config.isEnable(),
                config.getGraceMillis(),
                config.getSweepMillis(),
                config.getExpireAsyncThreads(),
                config.getStartupRecoveryPageSize(),
                config.getMissingAfterReconnectPolicy()
        );
    }

    protected String resolveServiceInstrumentDatabaseKey() {
        if ( this.mszServiceInstrumentKey != null && !this.mszServiceInstrumentKey.isBlank() ) {
            return this.mszServiceInstrumentKey;
        }

        if ( this.mszServiceDatabaseKey != null && !this.mszServiceDatabaseKey.isBlank() ) {
            return this.mszServiceDatabaseKey;
        }

        throw new IrrationalProvokedException( "RedQueen service database key does not configured." );
    }

    protected void prepare_service_control_transports() {
        JSONObject controlConfig = ( (JSONObject) this.mSubsystemConfig ).optJSONObject( "serviceControl" );
        boolean bEnableServiceRPC = true;
        JSONArray transportConfigs = null;
        if ( controlConfig != null ) {
            bEnableServiceRPC = controlConfig.optBoolean( "enableServiceRPC", true );
            transportConfigs = controlConfig.optJSONArray( "transports" );
        }
        if ( !bEnableServiceRPC ) {
            this.getLogger().info( "[ServiceControlTransport] Disabled by config. <Pass>" );
            return;
        }

        if ( transportConfigs == null || transportConfigs.isEmpty() ) {
            this.hook_husky_service_control_transport( this.mszControlRPCDriverKey );
            return;
        }

        for ( int i = 0; i < transportConfigs.length(); i++ ) {
            JSONObject transportConfig = transportConfigs.optJSONObject( i );
            if ( transportConfig == null ) {
                throw new IrrationalProvokedException( "Service control transport config at index `" + i + "` is not object." );
            }
            if ( !transportConfig.optBoolean( "enable", true ) ) {
                continue;
            }

            String szType = transportConfig.optString( "type", "" ).toLowerCase( Locale.ROOT );
            if ( "husky".equals( szType ) ) {
                String szDriver = transportConfig.optString( "driver", this.mszControlRPCDriverKey );
                this.hook_husky_service_control_transport( szDriver );
                continue;
            }
            if ( "grpc".equals( szType ) ) {
                this.hook_grpc_service_control_transport( transportConfig );
                continue;
            }

            throw new IrrationalProvokedException( "Unknown service control transport type `" + szType + "`." );
        }
    }

    protected void hook_husky_service_control_transport( String szDriver ) {
        UlfServer rpcServer = this.resolve_husky_rpc_server( szDriver );
        ServiceControlTransport transport = HuskyServiceControlTransportFactory.create( this.mServiceManager, rpcServer );
        this.mServiceManager.transportRegistry().hookTransport( transport );
        this.mServiceControlTransports.add( transport );
        this.getLogger().info( "[ServiceControlTransport] [Husky] (Driver: `{}`) <Hooked>", szDriver );
    }

    protected void hook_grpc_service_control_transport( JSONObject transportConfig ) {
        GrpcServerConfig grpcConfig = new GrpcServerConfig( transportConfig );
        if ( !grpcConfig.isEnabled() ) {
            return;
        }

        String szName = transportConfig.optString( "name", "RedQueenGrpcServiceControlServer" );
        long nMessageNodeId = transportConfig.optLong( "messageNodeId", grpcConfig.getPort() );
        GrpcAppointServer grpcServer = new GrpcAppointServer( szName, nMessageNodeId, grpcConfig );
        ServiceControlTransport transport = new GrpcServiceControlTransportFactory().create( this.mServiceManager, grpcServer );
        this.mServiceManager.transportRegistry().hookTransport( transport );
        this.mServiceControlTransports.add( transport );
        this.getLogger().info(
                "[ServiceControlTransport] [gRPC] (Name: `{}`, Port: `{}`) <Hooked>",
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

        throw new IrrationalProvokedException( "Control RPC driver `" + szDriver + "` does not exist or is not UlfServer." );
    }

    protected void ensure_husky_rpc_server_not_started( String szDriver, MessageNode messageNode ) {
        if ( messageNode == null || messageNode.isTerminated() ) {
            return;
        }

        throw new IrrationalProvokedException(
                "Husky RPC driver `" + szDriver + "` is already running. Please set CentralManage=false and let RedQueen start it after controller registration."
        );
    }

    protected void prepare_service_regiment() {
        JSONObject controlConfig = ( (JSONObject) this.mSubsystemConfig ).optJSONObject( "serviceControl" );
        if ( controlConfig != null && !controlConfig.optBoolean( "enableServiceRegiment", true ) ) {
            this.getLogger().info( "[ServiceRegiment] Disabled by config. <Pass>" );
            return;
        }

        this.mServiceRegiment = new RedCollectiveServiceRegiment(
                this.mPrimarySystem, this.mServiceInstrument, this.mServiceManager
        );

        try {
            this.mServiceRegiment.startServiceManage();
        }
        catch ( ServiceControlException e ) {
            throw new IrrationalProvokedException( e );
        }
    }

    protected void prepare_system_skeleton() {
        this.infoLifecycle( "<RedQueen> Preparing system skeleton.", LogStatuses.StatusStart );

        this.prepare_service_instrumentation();
        this.prepare_service_control_transports();
        this.prepare_service_regiment();

        this.infoLifecycle( "<RedQueen> Preparing system skeleton.", LogStatuses.StatusDone );
    }

    @Override
    public void vitalize() {
        this.init();
    }

    @Override
    public void terminate() {
        if ( this.mServiceRegiment != null ) {
            try {
                this.mServiceRegiment.stopServiceManage();
            }
            catch ( ServiceControlException e ) {
                throw new IrrationalProvokedException( e );
            }
        }

        this.mServiceControlTransports.clear();
    }

    @Override
    public ServiceControlInspection inspectServiceControl() {
        if ( this.mServiceRegiment != null ) {
            return this.mServiceRegiment.inspectServiceControl();
        }
        if ( this.mServiceManager != null ) {
            return this.mServiceManager.inspectServiceControl();
        }

        ServiceControlInspection status = new ServiceControlInspection();
        status.setOverallStatus( "Stopped" );
        status.setDiagnosticMessage( "RedQueen service regiment is not initialized." );
        return status;
    }

    @Override
    public ServiceInstrument serviceInstrument() {
        return this.mServiceInstrument;
    }

    @Override
    public ServiceManager serviceManager() {
        return this.mServiceManager;
    }

    @Override
    public CollectiveServiceRegiment serviceRegiment() {
        return this.mServiceRegiment;
    }

    @Override
    public PurgeService servicePurgeService() {
        if ( this.mServiceRegiment != null ) {
            return this.mServiceRegiment.purgeService();
        }
        return null;
    }
}
