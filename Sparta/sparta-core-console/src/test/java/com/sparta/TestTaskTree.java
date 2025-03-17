package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.task.ibatis.hydranium.TaskMappingDriver;
import com.pinecone.hydra.task.kom.UniformTasksInstrument;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.GUIDs;
import com.pinecone.radium.Radium;


class Randy extends Radium {
    public Randy( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Randy( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        KOIMappingDriver koiMappingDriver = new TaskMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );

        UniformTasksInstrument servicesTree = new UniformTasksInstrument( koiMappingDriver );
        this.testInsert( servicesTree );
        //this.testGet( servicesTree );
        //this.testDelete( servicesTree );
    }

    private void testInsert( UniformTasksInstrument servicesInstrument ){
//        GenericNamespace namespace = new GenericNamespace();
//        namespace.setName( "Test1" );
//        serviceInstrument.put( namespace );

        //Debug.trace( serviceInstrument.get( GUIDs.GUID72("03c2f90-000133-000 0-44") ) );


//        GenericApplicationElement applicationNode = new GenericApplicationElement(
//                new JSONMaptron( "{ name:specialApp, alias:jesus, deploymentMethod:Container, path:'/xxx/xxx/ggg', resourceType:human," +
//                        "type:Social, description: 'This is jesus', extraInformation: 'more', level:'L1', primaryImplLang: java, scenario:'/scenario/dragon/king'  }" )
//        );
//
//        applicationNode.apply( new JSONMaptron( "{ name:specialApp2, deploymentMethod:VM }" ) );
//        serviceInstrument.put( applicationNode );

        GenericTaskElement taskElement = new GenericTaskElement(
                new JSONMaptron( "{ name:'特殊服务', alias:jesus, serviceType:System, path:'/xxx/xxx/ggg', resourceType:human," +
                        "type:Social, description: 'This is special', extraInformation: 'more', level:'L1', primaryImplLang: java, scenario:'/scenario/dragon/king'  }" )
        );
        servicesInstrument.put( taskElement );
    }

    private void testGet( UniformTasksInstrument servicesInstrument ){
        //Debug.trace( serviceInstrument.queryGUIDByPath( "规则1/很好的服务/我的世界" ) );
        //Debug.trace( serviceInstrument.getPath(GUIDs.GUID72( "03c4a36-000381-0000-48" ) ) );
        //Debug.trace( serviceInstrument.get( GUIDs.GUID72("03e60e8-0000ae-0000-20") ) );
        //Debug.trace( serviceInstrument.get( GUIDs.GUID72("03e60e8-0000c5-0000-48") ) );
        //Debug.trace( serviceInstrument.get( GUIDs.GUID72("03e60e8-000117-0000-18") ) );
//        Debug.trace( servicesTree.get( GUIDs.GUID72( "02be396-0001e9-0000-e4" ) ) );
        //Debug.trace( serviceInstrument.affirmApplication( "Test1/App1" ) );

//        Debug.trace( serviceInstrument.affirmService( "root/特殊服务" ) );
//        Debug.trace( serviceInstrument.affirmApplication( "root/species/orc" ) );
//        Debug.trace( serviceInstrument.affirmNamespace("root/species") );
//        Debug.trace( serviceInstrument.affirmNamespace( "root" ).fetchChildren() );
//
//        serviceInstrument.affirmApplication( "root/species/orc" ).addChild( new GenericServiceElement( new JSONMaptron( "{ name: slaughter }" ) ) );
//
//        Debug.trace( serviceInstrument.affirmApplication( "root/species/orc" ).fetchChildren() );
//
//        Debug.trace( serviceInstrument.queryElement( "root/species/orc/slaughter" ).toJSONObject() );
//
//        serviceInstrument.affirmNamespace( "root" ).addChild( new GenericNamespace( new JSONMaptron( "{ name: weapon, scenario: s1, description: d1, level:L1, primaryImplLang:Java }" ) ) );
//
//        Debug.fmp( 2, serviceInstrument.queryElement( "root/weapon" ).evinceNamespace().toJSONDetails() );




//        ServicesJSONDecoder decoder = new ServicesJSONDecoder( serviceInstrument );
//        decoder.decode( new JSONMaptron( "{ root: { test: { app: { metaType: ApplicationElement, alias:as, services: { ser: { metaType: ServiceElement, type: Microservice } } } } } }" ) );
//
//        Debug.fmp( 2, serviceInstrument.queryElement( "root" ).toJSONObject() );
        Debug.trace(servicesInstrument.getPath( GUIDs.GUID72("181e9e4-000395-0000-d4") ));
    }

    private void testDelete( UniformTasksInstrument servicesInstrument ){
        servicesInstrument.remove( GUIDs.GUID72("181e9e4-000395-0000-d4") );
    }
}

public class TestTaskTree {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Randy Jesse = (Randy) Pinecone.sys().getTaskManager().add( new Randy( args, Pinecone.sys() ) );
            Jesse.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
