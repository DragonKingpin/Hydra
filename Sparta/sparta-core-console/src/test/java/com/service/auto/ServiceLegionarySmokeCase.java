package com.service.auto;

import java.util.List;

import com.acorn.redqueen.service.conduct.RedCollectiveServiceLegionary;
import com.acorn.redqueen.service.conduct.RedCollectiveServiceRegiment;
import com.acorn.redqueen.service.conduct.ServiceLegionaryJoinRequest;
import com.acorn.redqueen.service.conduct.ServiceLegionaryJoinResponse;
import com.acorn.redqueen.service.conduct.ServiceLegionaryState;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.UniformServiceInstrument;
import com.pinecone.hydra.service.registry.client.port.ServiceMetaPort;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;
import com.pinecone.hydra.service.registry.server.UniformServiceManager;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.tritium.Tritium;

public class ServiceLegionarySmokeCase implements Pinenut {

    protected static final String ServicePath = "root/test/app/test1";

    protected Tritium mSystem;

    public ServiceLegionarySmokeCase( Tritium system ) {
        this.mSystem = system;
    }

    public void run( ServiceLegionaryTransportScenario scenario ) throws Exception {
        ServiceLegionarySmokeContext context = this.createStartedContext( scenario );
        try {
            this.prepareLegionary( context );
            ServiceLegionaryJoinResponse response = context.legionary.joinRegiment();
            context.probe.record( "Smoke", 0, "JOINED", context.legionary );
            ServiceLegionaryAssertions.assertNotNull( response.getInstanceGuid(), "Join response instance guid is null." );
            ServiceLegionaryAssertions.assertOnline( context.legionary );
            this.assertServiceMetaVisible( context );
            this.passiveShutdown( context );
            Debug.greenfs( "[ServiceLegionarySmoke] " + scenario.name() + " PASS" );
        }
        finally {
            context.cleanup();
        }
    }

    protected ServiceLegionarySmokeContext createStartedContext( ServiceLegionaryTransportScenario scenario ) throws Exception {
        ServiceLegionarySmokeContext context = new ServiceLegionarySmokeContext( this.mSystem, scenario );
        return this.createStartedContext( context );
    }

    protected ServiceLegionarySmokeContext createStartedContext( ServiceLegionarySmokeContext context ) throws Exception {
        KOIMappingDriver driver = new ServiceMappingDriver(
                this.mSystem,
                (IbatisClient) this.mSystem.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ),
                this.mSystem.getDispenserCenter()
        );
        context.serviceInstrument = new UniformServiceInstrument( driver );
        context.serviceManager = new UniformServiceManager( context.serviceInstrument );
        if ( context.detachedObservationConfig != null ) {
            context.serviceManager.configureDetachedObservation( context.detachedObservationConfig );
        }
        context.scenario.hookServerTransport( context );
        context.regiment = new RedCollectiveServiceRegiment( this.mSystem, context.serviceInstrument, context.serviceManager );
        context.regiment.startServiceManage();
        context.serviceClient = context.scenario.createServiceClient( context );
        return context;
    }

    protected void prepareLegionary( ServiceLegionarySmokeContext context ) throws Exception {
        context.serviceClient.startService();
        ServiceMetaDTO meta = this.prepareServiceMeta( context );
        context.serviceMeta = meta;

        ServiceLegionaryJoinRequest joinRequest = new ServiceLegionaryJoinRequest();
        joinRequest.setServiceGuid( context.serviceInstrument.getGuidAllocator().parse( meta.getGuid() ) );
        joinRequest.setEndpointProtocol( context.scenario.endpointProtocol() );
        joinRequest.setEndpointHost( "127.0.0.1" );
        joinRequest.setEndpointPort( context.scenario.endpointPort() );
        joinRequest.setEndpointPath( "/test1" );
        joinRequest.setVersion( "smoke" );
        joinRequest.setZone( "local" );
        joinRequest.setWeight( 100 );
        joinRequest.setMetadataJson( "{source:\"service-legionary-smoke\"}" );

        context.legionary = new RedCollectiveServiceLegionary(
                context.scenario.name() + "-service-legionary",
                context.serviceClient,
                joinRequest
        );
        context.legionary.startService();
        context.probe.record( "Smoke", 0, "CONTROL_READY", context.legionary );
    }

    protected ServiceMetaDTO prepareServiceMeta( ServiceLegionarySmokeContext context ) {
        ServiceMetaPort metaPort = context.serviceClient.meta();
        metaPort.evalCreationStatement(
                "{ root: { test: { app: { metaType: ApplicationElement, alias:as, services: { test1: { metaType: ServiceElement, type: Microservice } } } } } }"
        );
        ServiceMetaDTO meta = metaPort.queryServiceMetaByPath( ServicePath );
        ServiceLegionaryAssertions.assertNotNull( meta, "Service meta is null: " + ServicePath );
        ServiceLegionaryAssertions.assertNotNull( meta.getGuid(), "Service meta guid is null: " + ServicePath );
        return meta;
    }

    protected void assertServiceMetaVisible( ServiceLegionarySmokeContext context ) {
        List<ServiceMetaDTO> metas = context.serviceClient.meta().fetchServiceInsMetaByServiceId( context.serviceMeta.getGuid() );
        ServiceLegionaryAssertions.assertServiceMetaVisible( metas );
    }

    protected void passiveShutdown( ServiceLegionarySmokeContext context ) throws Exception {
        context.serviceManager.shutdownServiceInstance( context.legionary.getInstanceGuid(), context.scenario.name() + "SmokePassiveShutdown" );
        ServiceLegionaryAssertions.awaitTrue(
                context.scenario.name() + " legionary did not terminate after passive shutdown.",
                5000L,
                () -> context.legionary.getState() == ServiceLegionaryState.Terminated
        );
        ServiceLegionaryAssertions.assertTerminated( context.legionary );
        context.probe.record( "Smoke", 0, "TERMINATED", context.legionary );
    }
}
