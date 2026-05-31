package com.service;

import com.acorn.redqueen.service.conduct.RedCollectiveServiceRegiment;
import com.acorn.redqueen.service.registry.husky.client.HuskyServiceClientTransport;
import com.acorn.redqueen.service.registry.husky.server.HuskyServiceControlTransportFactory;
import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.UniformServiceInstrument;
import com.pinecone.hydra.service.registry.client.UniformServiceClient;
import com.pinecone.hydra.service.registry.client.control.ServiceClientManipulationHandler;
import com.pinecone.hydra.service.registry.client.control.ServiceClientShutdownInstruction;
import com.pinecone.hydra.service.registry.client.port.ServiceMetaPort;
import com.pinecone.hydra.service.registry.server.UniformServiceManager;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
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

    @Override
    public void vitalize () throws Exception {
        KOIMappingDriver koiMappingDriver = new ServiceMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );

        UniformServiceInstrument servicesTree = new UniformServiceInstrument( koiMappingDriver );

        WolfMCServer wolfKing = new WolfMCServer( "", this, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );

        UniformServiceManager serviceManager = new UniformServiceManager( servicesTree );
        serviceManager.transportRegistry().hookTransport(
                HuskyServiceControlTransportFactory.create( serviceManager, wolfKing )
        );
        RedCollectiveServiceRegiment serviceRegiment = new RedCollectiveServiceRegiment(this, servicesTree, serviceManager);

        serviceRegiment.startServiceManage();


        UlfClient ulfClient = new WolfMCClient(
                new GuidAllocator72V2().nextGUIDi64(), "", this, this.getMiddlewareDirector().getMiddlewareConfig().queryJSONObject( "Messagers.Messagers.WolfMCKingpin" )
        );
        HuskyServiceClientTransport transport = new HuskyServiceClientTransport( ulfClient, servicesTree.getGuidAllocator(), null );
        UniformServiceClient managerClient = new UniformServiceClient( servicesTree.getGuidAllocator(), transport );
        managerClient.startService();

        GUID instanceGuid = this.testUniformServiceRegister_Proactive( managerClient );
        this.testShutdown_Passive( serviceManager, managerClient, instanceGuid );

        //this.oldTest( servicesTree );
    }

    public GUID testUniformServiceRegister_Proactive( UniformServiceClient managerClient ) throws Exception {
        ServiceMetaPort metaIface = managerClient.meta();
        ServiceMetaDTO meta = metaIface.queryServiceMetaByPath( "root/test/app/ser" );
        Debug.greenfs( meta );

        String guid = metaIface.evalCreationStatement( "{ root: { test: { app: { metaType: ApplicationElement, alias:as, services: { test1: { metaType: ServiceElement, type: Microservice } } } } } }" );
        ServiceMetaDTO meta1 = metaIface.queryServiceMetaByPath( "root/test/app/test1" );
        Debug.greenfs( meta1 );

        GUID instanceGuid = managerClient.registerService( managerClient.getGuidAllocator().parse(meta1.getGuid()), null );

        List<ServiceMetaDTO> serviceMetaDTOS = metaIface.fetchServiceInsMetaByServiceId( meta1.getGuid() );
        Debug.bluefs( serviceMetaDTOS );

        //managerClient.deregister();
        //managerClient.terminateService();

        //Debug.trace(iface.hasOwnedServiceByServiceId( "181e9e6-000395-0000-94" ));
        return instanceGuid;
    }

    public void testShutdown_Passive( UniformServiceManager serviceManager, UniformServiceClient managerClient, GUID instanceGuid ) throws Exception {
        Thread[] shutdownThreadRef = new Thread[1];
        managerClient.registerManipulationHandler( new ServiceClientManipulationHandler() {
            @Override
            public void shutdownService( ServiceClientShutdownInstruction instruction ) {
                Debug.redfs( instruction.getReason() );
                Thread shutdownThread = new Thread( new Runnable() {
                    @Override
                    public void run() {
                        managerClient.terminateService();
                        serviceManager.terminateService();
                    }
                }, "test-service-passive-shutdown" );
                shutdownThreadRef[0] = shutdownThread;
                shutdownThread.start();
            }
        } );
        serviceManager.shutdownServiceInstance( instanceGuid, "PassiveShutdownTest" );
        if ( shutdownThreadRef[0] != null ) {
            shutdownThreadRef[0].join( 3000 );
        }
        Debug.sleep( 500 );
    }

}

public class  TestServiceManager{
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Brian brian = (Brian) Pinecone.sys().getTaskManager().add( new Brian( args, Pinecone.sys() ) );
            brian.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
