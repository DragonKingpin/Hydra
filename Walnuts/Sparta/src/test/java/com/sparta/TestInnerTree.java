package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
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
import com.pinecone.ulf.util.id.GUID72;
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



        //this.testRegistry();
        this.testTask();
    }

    private void testRegistry() {
        KOIMappingDriver koiMappingDriver = new RegistryMappingDriver(
                this, (IbatisClient)this.getMiddlewareManager().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );

        DistributedRegistry distributedRegistry = new GenericDistributeRegistry( koiMappingDriver );

        Debug.trace( distributedRegistry.getPath( GUIDs.GUID72("1f391ed2-0002d8-0000-e4") ) );
        Debug.fmp( 2, distributedRegistry.get( GUIDs.GUID72( "1f419c8c-000018-0000-a8" ) ) );
        Debug.hhf();
        Debug.fmp( 2, distributedRegistry.getNodeByPath( "ns1.ns2.ns3" ) );
        Debug.trace(distributedRegistry.getProperties(GUIDs.GUID72("1f419c8c-000018-0000-a8")));
        Debug.trace(distributedRegistry.selectByName("ns3"));
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
