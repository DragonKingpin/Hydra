package com.service;

import java.util.List;

import com.acorn.redqueen.service.conduct.RedCollectiveServiceLegionary;
import com.acorn.redqueen.service.conduct.ServiceLegionaryJoinRequest;
import com.acorn.redqueen.service.registry.husky.client.HuskyServiceClientTransport;
import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.UniformServiceInstrument;
import com.pinecone.hydra.service.registry.client.UniformServiceClient;
import com.pinecone.hydra.service.registry.client.port.ServiceMetaPort;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.tritium.Tritium;
import com.pinecone.ulf.util.guid.i64.GuidAllocator72V2;

class ServiceLegionaryClientBrian extends Tritium {

    private static final String HUSKY_CLIENT_CONFIG = "{host: \"localhost\",\n" +
            "port: 5771, SocketTimeout: 800, KeepAliveTimeout: 10,\n" +
            "ParallelChannels: 5, AutoReconnect: true, EnableHeartbeat: false, HeartbeatInterval: 2000}";

    protected UniformServiceClient mServiceClient;

    protected RedCollectiveServiceLegionary mLegionary;

    public ServiceLegionaryClientBrian( String[] args, CascadeSystem parent ) {
        super( args, null, parent );
    }

    @Override
    public void vitalize() throws Exception {
        KOIMappingDriver koiMappingDriver = new ServiceMappingDriver(
                this,
                (IbatisClient) this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ),
                this.getDispenserCenter()
        );

        UniformServiceInstrument servicesTree = new UniformServiceInstrument( koiMappingDriver );
        long clientId = new GuidAllocator72V2().nextGUIDi64();
        UlfClient ulfClient = new WolfMCClient( clientId, "", this, new JSONMaptron( HUSKY_CLIENT_CONFIG ) );
        HuskyServiceClientTransport transport = new HuskyServiceClientTransport( ulfClient, servicesTree.getGuidAllocator(), null );
        this.mServiceClient = new UniformServiceClient( servicesTree.getGuidAllocator(), transport );

        ServiceMetaDTO service = this.prepareTestServiceMeta();
        ServiceLegionaryJoinRequest joinRequest = new ServiceLegionaryJoinRequest();
        joinRequest.setServiceGuid( servicesTree.getGuidAllocator().parse( service.getGuid() ) );
        joinRequest.setEndpointProtocol( "Husky" );
        joinRequest.setEndpointHost( "127.0.0.1" );
        joinRequest.setEndpointPort( 5771 );
        joinRequest.setEndpointPath( "/test1" );
        joinRequest.setVersion( "manual" );
        joinRequest.setZone( "local" );
        joinRequest.setWeight( 100 );
        joinRequest.setMetadataJson( "{source:\"service-legionary-manual\"}" );

        this.mLegionary = new RedCollectiveServiceLegionary(
                "test-service-legionary",
                this.mServiceClient,
                joinRequest
        );
        this.mLegionary.startService();
        this.mLegionary.joinRegiment();

        List<ServiceMetaDTO> serviceInstances = this.mServiceClient.meta().fetchServiceInsMetaByServiceId( service.getGuid() );
        Debug.bluefs( serviceInstances );

        Debug.greenfs( "[ServiceLegionaryClient] clientId => " + clientId );
        Debug.greenfs( "[ServiceLegionaryClient] serviceGuid => " + this.mLegionary.getServiceGuid() );
        Debug.greenfs( "[ServiceLegionaryClient] instanceGuid => " + this.mLegionary.getInstanceGuid() );
        Debug.greenfs( "[ServiceLegionaryClient] keep running for manual reconnect test." );
    }

    protected ServiceMetaDTO prepareTestServiceMeta() throws Exception {
        this.mServiceClient.startService();
        ServiceMetaPort metaIface = this.mServiceClient.meta();
        ServiceMetaDTO app = metaIface.queryServiceMetaByPath( "root/test/app/ser" );
        Debug.greenfs( app );

        metaIface.evalCreationStatement( "{ root: { test: { app: { metaType: ApplicationElement, alias:as, services: { test1: { metaType: ServiceElement, type: Microservice } } } } } }" );
        ServiceMetaDTO service = metaIface.queryServiceMetaByPath( "root/test/app/test1" );
        Debug.greenfs( service );

        return service;
    }
}

public class TestServiceLegionary {

    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object... cfg) -> {
            ServiceLegionaryClientBrian brian = (ServiceLegionaryClientBrian) Pinecone.sys().getTaskManager().add(
                    new ServiceLegionaryClientBrian( args, Pinecone.sys() )
            );
            brian.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
