package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.CentralServicesInstrument;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.nodes.GenericApplicationNode;
import com.pinecone.hydra.service.kom.nodes.GenericNamespace;
import com.pinecone.hydra.service.kom.nodes.GenericServiceNode;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.id.GUIDs;
import com.sauron.radium.Radium;


class Jesse extends Radium {
    public Jesse( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Jesse( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        KOIMappingDriver koiMappingDriver = new ServiceMappingDriver(
                this, (IbatisClient)this.getMiddlewareManager().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );

        CentralServicesInstrument servicesTree = new CentralServicesInstrument( koiMappingDriver );
        this.testInsert( servicesTree );
        //this.testGet( servicesTree );
        //this.testDelete( servicesTree );
    }

    private void testInsert( ServicesInstrument servicesInstrument){

//        GenericNamespace namespace = new GenericNamespace();
//        namespace.setName( "Test1" );
//        servicesInstrument.put( namespace );

        Debug.trace( servicesInstrument.get( GUIDs.GUID72("03a33a2-0001f0-0001-20") ) );


//        GenericApplicationNode applicationNode = new GenericApplicationNode();
//        applicationNode.setName( "很好的服务" );
//        servicesInstrument.put( applicationNode );
//
//        GenericServiceNode serviceNode = new GenericServiceNode();
//        serviceNode.setGuid( servicesInstrument.getGuidAllocator().nextGUID72() );
//        serviceNode.setName("我的世界");
//        servicesInstrument.put( serviceNode );
    }

    private void testGet( ServicesInstrument servicesInstrument){
        //Debug.trace( servicesInstrument.queryGUIDByPath( "规则1/很好的服务/我的世界" ) );
        //Debug.trace( servicesInstrument.getPath(GUIDs.GUID72( "039338e-0002ba-0000-4c" ) ) );
        //Debug.trace( servicesInstrument.get( GUIDs.GUID72("039338e-0002ba-0000-4c") ) );
//        Debug.trace( servicesTree.get( GUIDs.GUID72( "02be396-0001e9-0000-e4" ) ) );
    }

    private void testDelete( ServicesInstrument servicesInstrument){
        servicesInstrument.remove( GUIDs.GUID72("039338c-0003cc-0001-70") );
    }
}
public class TestServiceTree {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Jesse Jesse = (Jesse) Pinecone.sys().getTaskManager().add( new Jesse( args, Pinecone.sys() ) );
            Jesse.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
