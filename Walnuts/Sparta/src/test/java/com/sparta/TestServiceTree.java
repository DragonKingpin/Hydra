package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.kom.UniformServicesInstrument;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.entity.GenericApplicationElement;
import com.pinecone.hydra.service.kom.entity.GenericNamespace;
import com.pinecone.hydra.service.kom.entity.GenericServiceElement;
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

        UniformServicesInstrument servicesTree = new UniformServicesInstrument( koiMappingDriver );
        //this.testInsert( servicesTree );
        this.testGet( servicesTree );
        //this.testDelete( servicesTree );
    }

    private void testInsert( ServicesInstrument servicesInstrument){

        GenericNamespace namespace = new GenericNamespace();
        namespace.setName( "Test1" );
        servicesInstrument.put( namespace );

        //Debug.trace( servicesInstrument.get( GUIDs.GUID72("03c2f90-000133-0000-44") ) );


        GenericApplicationElement applicationNode = new GenericApplicationElement();
        applicationNode.setName( "很好的服务" );
        applicationNode.setAlias("我是别名");
        applicationNode.setDeploymentMethod("我是部署方式");
        applicationNode.setPath("我是路径");
        applicationNode.setResourceType("我是资源类型");
        applicationNode.setType("我是类型");
        applicationNode.setDescription("我是描述");
        applicationNode.setExtraInformation("我是额外信息");
        applicationNode.setLevel("我是等级");
        applicationNode.setPrimaryImplLang("JAVA");
        applicationNode.setScenario("我是场景");
        servicesInstrument.put( applicationNode );
//
        GenericServiceElement serviceNode = new GenericServiceElement();
        serviceNode.setGuid( servicesInstrument.getGuidAllocator().nextGUID72() );
        serviceNode.setName("我的世界");
        serviceNode.setAlias("我是别名");
        serviceNode.setDescription("我是描述");
        serviceNode.setLevel("我是等级");
        serviceNode.setScenario("我是场景");
        serviceNode.setResourceType("我是资源类型");
        serviceNode.setServiceType("我是服务类型");
        serviceNode.setExtraInformation("我是额外信息");
        serviceNode.setPrimaryImplLang("JAVA");
        servicesInstrument.put( serviceNode );
    }

    private void testGet( ServicesInstrument servicesInstrument){
        //Debug.trace( servicesInstrument.queryGUIDByPath( "规则1/很好的服务/我的世界" ) );
        //Debug.trace( servicesInstrument.getPath(GUIDs.GUID72( "03c4a36-000381-0000-48" ) ) );
        //Debug.trace( servicesInstrument.get( GUIDs.GUID72("03e60e8-0000ae-0000-20") ) );
        //Debug.trace( servicesInstrument.get( GUIDs.GUID72("03e60e8-0000c5-0000-48") ) );
        //Debug.trace( servicesInstrument.get( GUIDs.GUID72("03e60e8-000117-0000-18") ) );
//        Debug.trace( servicesTree.get( GUIDs.GUID72( "02be396-0001e9-0000-e4" ) ) );
        Debug.trace( servicesInstrument.affirmApplication( "Test1/特殊服务" ) );
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
