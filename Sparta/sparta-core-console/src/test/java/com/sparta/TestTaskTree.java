package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.GenericTaskElement;
import com.pinecone.hydra.task.kom.marshaling.TaskJSONDecoder;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.GUIDs;
import com.pinecone.radium.Radium;
import com.walnut.odin.task.RavenTaskInstrument;
import com.walnut.odin.task.dto.CategoryTag;
import com.walnut.odin.task.dto.GenericCategoryTag;
import com.walnut.odin.task.mapper.OdinUniformTaskMappingDriver;
import com.walnut.odin.task.service.CategoryService;


class Randy extends Radium {
    public Randy( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Randy( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        OdinUniformTaskMappingDriver categoryMappingDriver = new OdinUniformTaskMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        RavenTaskInstrument ravenTaskInstrument = new RavenTaskInstrument( categoryMappingDriver );

        //this.testCategory( ravenTaskInstrument );

        this.testInsert( ravenTaskInstrument );
        //this.testGet( ravenTaskInstrument );
        //this.testDelete( instrument );

    }

    private void testCategory( RavenTaskInstrument instrument ) {
        CategoryService categoryService = instrument.getCategoryService();

        CategoryTag tag = new GenericCategoryTag();
        tag.setCategoryName( "Data" );
        tag.setCategoryType( "System" );
        Debug.greenfs( categoryService.setCategoryTag( "root/test/job/task", tag ) );
    }

    private void testInsert( TaskInstrument instrument ) {
//        GenericNamespace namespace = new GenericNamespace();
//        namespace.setName( "Test1" );
//        instrument.put( namespace );

        //Debug.trace( instrument.get( GUIDs.GUID72("03c2f90-000133-000 0-44") ) );


//        GenericApplicationElement applicationNode = new GenericApplicationElement(
//                new JSONMaptron( "{ name:specialApp, alias:jesus, deploymentMethod:Container, path:'/xxx/xxx/ggg', resourceType:human," +
//                        "type:Social, description: 'This is jesus', extraInformation: 'more', level:'L1', primaryImplLang: java, scenario:'/scenario/dragon/king'  }" )
//        );
//
//        applicationNode.apply( new JSONMaptron( "{ name:specialApp2, deploymentMethod:VM }" ) );
//        instrument.put( applicationNode );

        GenericTaskElement taskElement = new GenericTaskElement(
                new JSONMaptron( "{ name:'特殊服务8', alias:jesus, serviceType:System, resourceType:human," +
                        "type:Social, description: 'This is special', extraInformation: 'more', level:'L1', primaryImplLang: java, scenario:'/scenario/dragon/king'  }" )
        );
        instrument.put( taskElement );
    }

    private void testGet( TaskInstrument instrument ){
        //Debug.trace( instrument.queryGUIDByPath( "规则1/很好的服务/我的世界" ) );
        //Debug.trace( instrument.getPath(GUIDs.GUID72( "03c4a36-000381-0000-48" ) ) );
        //Debug.trace( instrument.get( GUIDs.GUID72("03e60e8-0000ae-0000-20") ) );
        //Debug.trace( instrument.get( GUIDs.GUID72("03e60e8-0000c5-0000-48") ) );
        //Debug.trace( instrument.get( GUIDs.GUID72("03e60e8-000117-0000-18") ) );
//        Debug.trace( instrument.get( GUIDs.GUID72( "02be396-0001e9-0000-e4" ) ) );
        //Debug.trace( instrument.affirmApplication( "Test1/App1" ) );

//        Debug.trace( instrument.affirmService( "root/特殊服务" ) );
//        Debug.trace( instrument.affirmApplication( "root/species/orc" ) );
//        Debug.trace( instrument.affirmNamespace("root/species") );
//        Debug.trace( instrument.affirmNamespace( "root" ).fetchChildren() );
//
//        instrument.affirmApplication( "root/species/orc" ).addChild( new GenericServiceElement( new JSONMaptron( "{ name: slaughter }" ) ) );
//
//        Debug.trace( instrument.affirmApplication( "root/species/orc" ).fetchChildren() );
//
//        Debug.trace( instrument.queryElement( "root/species/orc/slaughter" ).toJSONObject() );
//
//        instrument.affirmNamespace( "root" ).addChild( new GenericNamespace( new JSONMaptron( "{ name: weapon, scenario: s1, description: d1, level:L1, primaryImplLang:Java }" ) ) );
//
//        Debug.fmp( 2, instrument.queryElement( "root/weapon" ).evinceNamespace().toJSONDetails() );




        TaskJSONDecoder decoder = new TaskJSONDecoder( instrument );
        decoder.decode( new JSONMaptron( "{ root: { test: { job: { metaType: JobElement, type:SysJob, tasks: { task: { metaType: TaskElement, type: SparkTask } } } } } }" ) );

        Debug.fmp( 2, instrument.queryElement( "root" ).toJSONObject() );
        //Debug.trace(deployInstrument.getPath( GUIDs.GUID72("181e9e4-000395-0000-d4") ));
    }

    private void testDelete( TaskInstrument instrument ) {
        instrument.remove( GUIDs.GUID72("181e9e4-000395-0000-d4") );
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
