package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.UniformServiceInstrument;
import com.pinecone.hydra.service.registry.ServiceLifecycleIface;
import com.pinecone.hydra.service.registry.ServiceMetaManipulationIface;
import com.pinecone.hydra.service.registry.UniformServiceManager;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.uma.wolf.WolvesAppointClient;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.pinecone.tritium.Tritium;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;

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




        WolfMCServer          wolfKing = new WolfMCServer( "", this, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );
        WolvesAppointServer wolfServer = new WolvesAppointServer( wolfKing, HuskyDuplexExpress.class );
        UniformServiceManager serviceManager = new UniformServiceManager( servicesTree, wolfServer );
        wolfKing.execute();

        Debug.sleep( 500 );


        DuplexAppointClient wolf = new WolvesAppointClient(
                new WolfMCClient( 2048, "", this, this.getMiddlewareDirector().getMiddlewareConfig().queryJSONObject( "Messagers.Messagers.WolfMCKingpin" ) )
        );
        wolf.execute();
        wolf.compile( ServiceLifecycleIface.class, false );
        wolf.compile( ServiceMetaManipulationIface.class, false );
        this.testServiceRegister( wolf );

    }

    public void testServiceRegister( DuplexAppointClient client ) {
        ServiceLifecycleIface iface = client.getIface( ServiceLifecycleIface.class );
        ServiceMetaManipulationIface metaIface = client.getIface(ServiceMetaManipulationIface.class);

        RegisterServiceDTO serviceDTO1 = new RegisterServiceDTO();
        serviceDTO1.setServiceId( "1769872-0002d2-0003-cc" );
        serviceDTO1.setClientId( 1234L );

        RegisterServiceDTO serviceDTO2 = new RegisterServiceDTO();
        serviceDTO2.setServiceId( "181e9e6-000395-0000-94" );
        serviceDTO2.setClientId(1235L);

        iface.registerService( serviceDTO1 );
        iface.registerService( serviceDTO2 );

        List<ServiceMetaDTO> serviceMetaDTOS = metaIface.fetchServiceInsMetaByServiceId( "1769872-0002d2-0003-cc" );
        Debug.trace( serviceMetaDTOS );

        iface.deregisterServiceByServiceId( "181e9e6-000395-0000-94" );
//
        Debug.trace(iface.hasOwnedServiceByServiceId( "181e9e6-000395-0000-94" ));

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
