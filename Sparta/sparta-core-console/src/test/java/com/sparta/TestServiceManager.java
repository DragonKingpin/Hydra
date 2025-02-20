package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.entity.BindUSII;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.UniformServicesInstrument;
import com.pinecone.hydra.service.registry.ServiceLifecycleIface;
import com.pinecone.hydra.service.registry.ServiceMetaManipulationIface;
import com.pinecone.hydra.service.registry.UniformServiceManager;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.uma.wolf.WolvesAppointClient;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;
import com.pinecone.hydra.umc.wolfmc.client.WolfMCClient;
import com.pinecone.hydra.umc.wolfmc.server.WolfMCServer;
import com.pinecone.radium.Radium;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.GUIDs;

import java.util.Collection;

class Brian extends Radium {
    public Brian( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Brian( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        KOIMappingDriver koiMappingDriver = new ServiceMappingDriver(
                this, (IbatisClient)this.getMiddlewareManager().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );

        UniformServicesInstrument servicesTree = new UniformServicesInstrument( koiMappingDriver );




        WolfMCServer          wolfKing = new WolfMCServer( "", this, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );
        WolvesAppointServer wolfServer = new WolvesAppointServer( wolfKing, HuskyDuplexExpress.class );
        UniformServiceManager serviceManager = new UniformServiceManager( servicesTree, wolfServer );
        wolfKing.execute();

        Debug.sleep( 500 );


        DuplexAppointClient wolf = new WolvesAppointClient(
                new WolfMCClient( 2048, "", this, this.getMiddlewareManager().getMiddlewareConfig().queryJSONObject( "Messagers.Messagers.WolfMCKingpin" ) )
        );
        wolf.execute();
        wolf.compile( ServiceLifecycleIface.class, false );
        wolf.compile( ServiceMetaManipulationIface.class, false );
        this.testServiceRegister( wolf );
    }

    public void testServiceRegister( DuplexAppointClient client ) {
        ServiceLifecycleIface iface = client.getIface( ServiceLifecycleIface.class );
        ServiceMetaManipulationIface metaIface = client.getIface(ServiceMetaManipulationIface.class);

        RegisterServiceDTO serviceDTO = new RegisterServiceDTO();
        serviceDTO.setServiceId( "1769872-0002d2-0003-cc" );
        serviceDTO.setClientId( 1234L );

        iface.registerService( serviceDTO );

        Collection<ServiceInstance> serviceInstances = metaIface.queryServiceInstanceByClientId(1234L);
        Debug.trace( serviceInstances );
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
