package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.registry.DistributedRegistry;
import com.pinecone.hydra.registry.GenericDistributeRegistry;
import com.pinecone.hydra.registry.ibatis.hydranium.RegistryMappingDriver;
import com.pinecone.hydra.scenario.ibatis.hydranium.ScenarioMappingDriver;
import com.pinecone.hydra.scenario.tree.DistributedScenarioMetaTree;
import com.pinecone.hydra.scenario.tree.GenericDistributedScenarioMetaTree;
import com.pinecone.hydra.service.ibatis.hydranium.ServiceMappingDriver;
import com.pinecone.hydra.service.tree.DistributedScopeServiceTree;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.task.ibatis.hydranium.TaskMappingDriver;
import com.pinecone.hydra.task.tree.DistributedTaskMetaTree;
import com.pinecone.hydra.task.tree.GenericDistributedTaskMetaTree;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.id.GUIDs;
import com.sauron.radium.Radium;


class LadyGaga extends Radium {
    public LadyGaga( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public LadyGaga( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
//        Sparta sparta = new Sparta( "Sparta", this );
//        sparta.execute();
//
//        Thread shutdowner = new Thread(()->{
//            Debug.sleep( 5000 );
//            sparta.terminate();
//        });
//        //shutdowner.start();
//
//        this.getTaskManager().add( sparta );
//        this.getTaskManager().syncWaitingTerminated();



        this.testRegistry();
        //this.testTask();
    }

    private void testRegistry() {
        KOIMappingDriver koiMappingDriver = new RegistryMappingDriver(
                this, (IbatisClient)this.getMiddlewareManager().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );

        DistributedRegistry registry = new GenericDistributeRegistry( koiMappingDriver );

        registry.putProperties( "game/minecraft/wizard1", new JSONMaptron( "{ name:ken, age:22, species:human, job:wizard }" ) );
        registry.putProperties( "game/minecraft/sorcerer1", new JSONMaptron( "{ name:dragonking, age:666, species:dragon, job:sorcerer }" ) );
        registry.putProperties( "game/terraria/mob1", new JSONMaptron( "{ name:lural, age:666, species:cthulhu, job:mob }" ) );
        registry.putProperties( "game/witcher/mob2", new JSONMaptron( "{ name:wxsdw, age:666, species:cthulhu, job:mob }" ) );
        registry.putProperties( "game/witcher/mob3", new JSONMaptron( "{ name:mob3, age:661, species:cthulhu2, job:mob2 }" ) );
        registry.putProperties( "game/witcher/people/xxx", new JSONMaptron( "{ name:xxxx, age:999, species:elf, job:warrior }" ) );
        registry.putProperties( "game/witcher/people/xx2", new JSONMaptron( "{ name:xxx2, age:992, species:elf, job:warrior }" ) );
        registry.putTextValue( "game/witcher/jesus", "json", "{k:p}" );


//
//        registry.putProperties( "movie.terraria.mob1", new JSONMaptron( "{ name:lural, age:666, species:cthulhu, job:mob }" ) );



        //registry.remove( "game" );
        //registry.remove( "game.witcher" );
        //registry.remove( "game.minecraft" );
        //registry.remove("game.terraria");
        //registry.remove("game.witcher");
        Debug.fmp( 2, registry.getProperties( registry.queryGUIDByFN( "game.witcher.mob3" ) ).getValue( "name" ) );


        //registry.remove( "game" );


       // registry.affirmPropertyConfig( "泰拉瑞亚.灾厄.至尊灾厄" );

        // registry.remove( "泰拉瑞亚.灾厄.至尊灾厄" );

        //Debug.fmp( 4, registry.getProperties( "泰拉瑞亚.灾厄.至尊灾厄" ).toJSONObject() );


//        Debug.trace( registry.getPath( GUIDs.GUID72("1f391ed2-0002d8-0000-e4") ) );
//        RegistryTreeNode registryTreeNode = registry.get(GUIDs.GUID72("1f419c8c-000018-0000-a8"));
//        Debug.trace(registryTreeNode.evinceProperties());
//        Debug.trace( registryTreeNode.evinceProperties().values()  );
//        Debug.hhf();
//        Debug.trace( 2, registry.getNodeByPath( "ns1.ns2.ns3" ) );
//        Debug.hhf();
//        Debug.trace(registry.getProperties( GUIDs.GUID72("1f419c8c-000018-0000-a8")) );
//        Debug.hhf();
//        Debug.trace(registry.selectByName("ns3"));
//        Debug.hhf();
//        RegistryTreeNode namespace = registry.get(GUIDs.GUID72("1f39293c-0002e2-0000-c4"));
//        NamespaceNode namespaceNode = namespace.evinceNamespaceNode();
//        Debug.trace(namespaceNode.listItem());
//        Debug.hhf();
//        Debug.trace(namespaceNode.entrySet());
//        RegistryTreeNode propertyNode = registry.get(GUIDs.GUID72("1f419c8c-000018-0000-a8"));
//        PropertiesNode propertiesNode = propertyNode.evinceProperties();
//        Debug.hhf();
//        Debug.trace(propertiesNode.values());
//        Debug.hhf();
//        Debug.trace(propertiesNode.isEmpty());
//        Debug.hhf();
//        Debug.trace(propertiesNode.entrySet());


//        Debug.fmp( 4, registry.getNamespaceNode( "ns1.ns2.ns3" ).listItem() );
//        Debug.fmp( 4, registry.getProperties( "conf3" ).toJSONObject() );

        //registry.get( registry.queryGUIDByPath( "conf3" )  ).evinceProperties().put( (new JSONMaptron( "{ species: 'human' }" )).entrySet() );



        //ConfigNode cn = registry.getConfigNode( GUIDs.GUID72("1f419c8c-000018-0000-a8") );

        //Debug.trace( cn.keySet() );
        //Debug.trace( cn.size() );
        //Debug.trace( cn.isEmpty() );
    }

    private void testTask(){
        KOIMappingDriver koiMappingDriver = new TaskMappingDriver(
                this, (IbatisClient)this.getMiddlewareManager().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        DistributedTaskMetaTree distributedScenarioMetaTree = new GenericDistributedTaskMetaTree(koiMappingDriver);
        Debug.trace( distributedScenarioMetaTree.get(GUIDs.GUID72("1f4eda64-00023c-0002-e8")));
    }

    private void testService(){
        KOIMappingDriver koiMappingDriver = new ServiceMappingDriver(
                this, (IbatisClient)this.getMiddlewareManager().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        DistributedScopeServiceTree distributedScopeServiceTree = new DistributedScopeServiceTree(koiMappingDriver);

        Debug.trace(distributedScopeServiceTree.getNode(GUIDs.GUID72("f83ccfc-0002f9-0000-b4")).toString());
        Debug.trace(distributedScopeServiceTree.getPath(GUIDs.GUID72("f83ccfc-0002f9-0000-b4")));
    }

    private void testScenario(){
        KOIMappingDriver koiMappingDriver = new ScenarioMappingDriver(
                this, (IbatisClient)this.getMiddlewareManager().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        DistributedScenarioMetaTree distributedScenarioMetaTree = new GenericDistributedScenarioMetaTree(koiMappingDriver);
//        GenericNamespaceNode genericNamespaceNode = new GenericNamespaceNode();
//        genericNamespaceNode.setName("瘟疫公司");
//        genericNamespaceNode.setNamespaceNodeMeta(new GenericNamespaceNodeMeta());
//        genericNamespaceNode.setScenarioCommonData(new GenericScenarioCommonData());
//        distributedScenarioMetaTree.insert(genericNamespaceNode);
        distributedScenarioMetaTree.get(GUIDs.GUID72("1f5bced8-000315-0002-70"));
    }
}


public class TestInnerTree {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            LadyGaga ladyGaga = (LadyGaga) Pinecone.sys().getTaskManager().add( new LadyGaga( args, Pinecone.sys() ) );
            ladyGaga.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
