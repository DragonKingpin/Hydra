package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.registry.GenericKOMRegistry;
import com.pinecone.hydra.registry.KOMRegistry;
import com.pinecone.hydra.registry.ibatis.hydranium.RegistryMappingDriver;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.CentralServicesTree;
import com.pinecone.hydra.service.kom.GenericNamespaceRules;
import com.pinecone.hydra.service.kom.ServicesTree;
import com.pinecone.hydra.service.kom.meta.GenericServiceNodeMeta;
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

        CentralServicesTree servicesTree = new CentralServicesTree( koiMappingDriver );
        //this.testInsert( servicesTree );
        this.testGet( servicesTree );
        //this.testDelete( servicesTree );
    }
    private void testInsert( ServicesTree servicesTree ){

        GenericNamespace namespace = new GenericNamespace();
        namespace.setName( "规则1" );
        servicesTree.put( namespace );

        GenericApplicationNode applicationNode = new GenericApplicationNode();
        applicationNode.setName( "很好的服务" );
        servicesTree.put( applicationNode );

        GenericServiceNode serviceNode = new GenericServiceNode();
        serviceNode.setGuid( servicesTree.getGuidAllocator().nextGUID72() );
        serviceNode.setName("我的世界");
        servicesTree.put( serviceNode );
    }

    private void testGet( ServicesTree servicesTree ){
        Debug.trace( servicesTree.queryGUIDByPath( "规则1/很好的服务/我的世界" ) );
        //Debug.trace( servicesTree.getPath(GUIDs.GUID72( "02c43ae-0003ac-0000-d8" ) ) );
//        Debug.trace( servicesTree.get( GUIDs.GUID72("02be396-000308-0001-4c") ) );
//        Debug.trace( servicesTree.get( GUIDs.GUID72( "02be396-0001e9-0000-e4" ) ) );
    }

    private void testDelete( ServicesTree servicesTree ){
        servicesTree.remove( GUIDs.GUID72("02c3bf8-000223-0001-24") );
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
