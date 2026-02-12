package com.rpc;

import com.acorn.redqueen.service.conduct.RedCollectiveServiceRegiment;
import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.grpc.client.GrpcAppointClient;
import com.pinecone.hydra.grpc.client.GrpcClientConfig;
import com.pinecone.hydra.grpc.server.GrpcAppointServer;
import com.pinecone.hydra.grpc.server.GrpcServerConfig;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.UniformServiceInstrument;
import com.pinecone.hydra.service.registry.grpc.client.GrpcServiceClient;
import com.pinecone.hydra.service.registry.grpc.server.GrpcServiceAppointServer;
import com.pinecone.hydra.service.registry.server.ServiceMetaManipulationIface;
import com.pinecone.hydra.service.registry.server.UniformServiceManager;
import com.pinecone.hydra.service.registry.client.HuskyServiceClient;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;
import com.pinecone.hydra.service.registry.ulf.HuskyServiceAppointServer;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.pinecone.tritium.Tritium;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.i64.GuidAllocator72V2;

import java.util.List;

class Brian extends Tritium {
    public Brian( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Brian( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }


    public void vitalize () throws Exception {
        KOIMappingDriver koiMappingDriver = new ServiceMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );

        UniformServiceInstrument servicesTree = new UniformServiceInstrument( koiMappingDriver );

        UniformServiceManager serviceManager = new UniformServiceManager( servicesTree );
        GrpcServiceAppointServer grpcServer = new GrpcServiceAppointServer(
                new GrpcAppointServer( new GrpcServerConfig( new JSONMaptron( "{ port: 5888 }" ) ))
        );

        serviceManager.hookAppointServer(grpcServer);
        RedCollectiveServiceRegiment serviceRegiment = new RedCollectiveServiceRegiment(this, servicesTree, serviceManager);
        serviceRegiment.startServiceManage();


        GrpcServiceClient client = new GrpcServiceClient(
                new GrpcAppointClient( new GuidAllocator72V2().nextGUIDi64(), new GrpcClientConfig( new JSONMaptron( "{ host: 'localhost', port: 5888 }" ) ) ),
                servicesTree.getGuidAllocator()
        );
        client.startService();

        testUniformServiceRegister_Proactive(client, serviceManager);

    }

    public static void testUniformServiceRegister_Proactive( GrpcServiceClient client, UniformServiceManager serviceManager ) throws Exception {
        ServiceMetaDTO meta = client.getMetaManipulation().queryServiceMetaByPath("root/test/app/ser");
        Debug.bluef("Meta: " + meta);

        String guid = client.getMetaManipulation().evalCreationStatement( "{ root: { test: { app: { metaType: ApplicationElement } } } }");
        Debug.bluef("Creation GUID: " + guid);

        meta = client.getMetaManipulation().queryServiceMetaByPath("root/test/app/test1");
        Debug.greenfs( meta );

        client.registerService( client.getGuidAllocator().parse(meta.getGuid()), null );

        List<ServiceMetaDTO> serviceMetaDTOS = client.getMetaManipulation().fetchServiceInsMetaByServiceId(meta.getGuid());
        Debug.bluefs( serviceMetaDTOS );

        client.getAppointNodus().close();
    }



    public void vitalize1 () throws Exception {
        KOIMappingDriver koiMappingDriver = new ServiceMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );

        UniformServiceInstrument servicesTree = new UniformServiceInstrument( koiMappingDriver );

        WolfMCServer wolfKing = new WolfMCServer( "", this, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );

        UniformServiceManager serviceManager = new UniformServiceManager( servicesTree );
        serviceManager.hookAppointServer( new HuskyServiceAppointServer( new WolvesAppointServer( wolfKing, HuskyDuplexExpress.class ) ));
        RedCollectiveServiceRegiment serviceRegiment = new RedCollectiveServiceRegiment(this, servicesTree, serviceManager);

        serviceRegiment.startServiceManage();


        UlfClient ulfClient = new WolfMCClient(
                new GuidAllocator72V2().nextGUIDi64(), "", this, this.getMiddlewareDirector().getMiddlewareConfig().queryJSONObject( "Messagers.Messagers.WolfMCKingpin" )
        );
        HuskyServiceClient managerClient = new HuskyServiceClient( ulfClient, servicesTree.getGuidAllocator() );
        managerClient.startService();

        this.testUniformServiceRegister_Proactive( managerClient );

        //this.oldTest( servicesTree );
    }

    public void testUniformServiceRegister_Proactive( HuskyServiceClient managerClient ) throws Exception {
        DuplexAppointClient client = managerClient.getAppointNodus();
        ServiceMetaManipulationIface metaIface = client.getIface(ServiceMetaManipulationIface.class);
        ServiceMetaDTO meta = metaIface.queryServiceMetaByPath( "root/test/app/ser" );
        Debug.greenfs( meta );

        String guid = metaIface.evalCreationStatement( "{ root: { test: { app: { metaType: ApplicationElement, alias:as, services: { test1: { metaType: ServiceElement, type: Microservice } } } } } }" );
        ServiceMetaDTO meta1 = metaIface.queryServiceMetaByPath( "root/test/app/test1" );
        Debug.greenfs( meta1 );

        managerClient.registerService( managerClient.getGuidAllocator().parse(meta1.getGuid()), null );

        List<ServiceMetaDTO> serviceMetaDTOS = metaIface.fetchServiceInsMetaByServiceId( meta1.getGuid() );
        Debug.bluefs( serviceMetaDTOS );

        //managerClient.deregister();
        client.close();

        //Debug.trace(iface.hasOwnedServiceByServiceId( "181e9e6-000395-0000-94" ));
    }

}

public class TestGrpcService {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Brian brian = (Brian) Pinecone.sys().getTaskManager().add( new Brian( args, Pinecone.sys() ) );
            brian.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
