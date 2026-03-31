package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.system.regime.arch.Lord;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.walnut.odin.atlas.advance.GenericTapedBFSGraphAdvancer;
import com.walnut.odin.atlas.advance.strategy.AtlasPriorityProcessStrategy;
import com.walnut.odin.atlas.advance.strategy.MegaInDegreeFirstStrategy;
import com.walnut.odin.atlas.graph.UniformRuntimeAtlas;
import com.walnut.odin.atlas.mapper.OdinAtlasMappingDriver;
import com.pinecone.hydra.layer.ibatis.hydranium.LayerMappingDriver;
import com.pinecone.hydra.queue.ibatis.hydranium.QueueMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.task.ibatis.hydranium.TaskMappingDriver;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.unit.iqueue.ConfigurableMegaDeflectPriorityQueueMeta;
import com.pinecone.hydra.unit.iqueue.ConfigurableMegaStratumQueueMeta;
import com.pinecone.hydra.unit.iqueue.MagnitudeDPQueue;
import com.pinecone.hydra.unit.iqueue.MegaDPStratumQueue;
import com.pinecone.hydra.unit.iqueue.MegaDeflectPriorityQueueMeta;
import com.pinecone.hydra.unit.iqueue.MegaStratumQueueMeta;
import com.pinecone.hydra.unit.vgraph.MagnitudeVectorDAG;
import com.pinecone.hydra.unit.vgraph.VectorDAG;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;
import com.pinecone.hydra.unit.vgraph.layer.VLayerInstrument;
import com.pinecone.hydra.unit.vgraph.source.AtlasMappingDriver;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.archcraft.ender.EnderHydra;
import com.walnut.odin.conduct.CollectiveTaskRegiment;
import com.walnut.odin.conduct.RavenCollectiveTaskRegiment;
import com.walnut.odin.conduct.dag.RavenTaskGraphOrchestrator;
import com.walnut.odin.conduct.schedule.RavenTaskScheduler;
import com.walnut.odin.system.Odin;
import com.walnut.odin.task.CentralizedTaskInstrument;
import com.walnut.odin.task.GenericRavenTaskConfig;
import com.walnut.odin.task.RavenTaskInstrument;
import com.walnut.odin.task.mapper.OdinUniformTaskMappingDriver;

class Rick extends EnderHydra {
    public Rick( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Rick( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {

        Lord lord = this.getLordFederation().instantiate( "KernelOdinLord", "./system/setup/lords/odin.json5" );

        Odin odin = (Odin) lord;
        odin.vitalize();


        AtlasMappingDriver atlasMappingDriver = new OdinAtlasMappingDriver(
                this,(IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ),this.getDispenserCenter()
        );

        KOIMappingDriver koiMappingDriver = new QueueMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );

        KOIMappingDriver driver = new OdinUniformTaskMappingDriver(
                this,(IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ),this.getDispenserCenter()
        );

        KOIMappingDriver layerMappingDriver = new LayerMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );
        VLayerInstrument layerInstrument = new VLayerInstrument(layerMappingDriver);
        CentralizedTaskInstrument uniformTaskInstrument = new RavenTaskInstrument( driver, new GenericRavenTaskConfig() );

        UniformRuntimeAtlas uniformRuntimeAtlas = new UniformRuntimeAtlas(atlasMappingDriver, uniformTaskInstrument, layerInstrument);




        //this.testInsert(uniformRuntimeAtlas);
        //this.testQuery( uniformRuntimeAtlas );
        //this.testTape( uniformRuntimeAtlas, koiMappingDriver );
        //this.testAdvancer( uniformRuntimeAtlas, koiMappingDriver,layerInstrument );
        this.testOrchestrator( layerInstrument, uniformRuntimeAtlas,koiMappingDriver, uniformTaskInstrument );
    }

    public void testInsert(UniformRuntimeAtlas uniformRuntimeAtlas) {
        GuidAllocator guidAllocator = uniformRuntimeAtlas.getGuidAllocator();

//        for( int i = 1; i<=12; i++ ) {
//            TaskAtlasNode taskAtlasNode = new TaskAtlasNode();
//            taskAtlasNode.setName("测试图节点" + i);
//            uniformRuntimeAtlas.put( taskAtlasNode );
//        }

        //uniformRuntimeAtlas.put( GUIDs.GUID72("252386a-0000ca-0001-f0"),taskAtlasNode );
        uniformRuntimeAtlas.addChild(GUIDs.GUID128("01972f7e-1642-75c5-aa70-82a752fd5e05"),GUIDs.GUID128("01972f7e-164e-7f80-8e67-a22060a3afd7"));
        //uniformRuntimeAtlas.put(GUIDs.GUID72("20dc3d8-00007b-0000-50"), taskAtlasNode);
    }

    public void testQuery(UniformRuntimeAtlas uniformRuntimeAtlas) {
//        GuidAllocator guidAllocator = uniformRuntimeAtlas.getGuidAllocator();
//        TaskGraphNode query = uniformRuntimeAtlas.query(GUIDs.GUID72("20dc3d8-00007b-0000-50"));
//        Debug.trace(query.toJSONString());
//        List<String> path = uniformRuntimeAtlas.getPath(GUIDs.GUID72("210f43c-000017-0000-64"));
//        Debug.trace(path);

        GraphNode graphNode = uniformRuntimeAtlas.queryGraphNodeByTaskGuid(GUIDs.GUID128("21164d6-0003e5-000f-50"));
        Debug.trace(graphNode.toJSONString());

        TaskElement taskElement = uniformRuntimeAtlas.queryTaskElementByGuid(GUIDs.GUID128("233e952-000010-0000-c0"));

        Debug.trace(taskElement.toJSONObject());
    }

    public void testTape( UniformRuntimeAtlas uniformRuntimeAtlas, KOIMappingDriver driver ) {
//        MagnitudeVectorDAG magnitudeVectorDAG = new MagnitudeVectorDAG( GUIDs.GUID128("22610ea-00002d-0000-a0"),uniformRuntimeAtlas.getMasterManipulator().getVectorGraphMasterManipulator(), uniformRuntimeAtlas.getConfig()  );
//        GraphStratumTape tapeded = uniformRuntimeAtlas.tapedGraphStratumAdvancer(magnitudeVectorDAG, driver);
//        //Debug.trace(tapeded.next().toJSONString());
//        Debug.trace(tapeded.fetchNodes(2,1));
    }

    public void testAdvancer( UniformRuntimeAtlas uniformRuntimeAtlas, KOIMappingDriver driver, LayerInstrument layerInstrument ) {
        MagnitudeVectorDAG magnitudeVectorDAG = (MagnitudeVectorDAG) uniformRuntimeAtlas.queryByPath( "l1/l11" );
        MegaDeflectPriorityQueueMeta meta1 = new ConfigurableMegaDeflectPriorityQueueMeta();
        meta1.setQueueTableName( "hydra_queue_nodes" );
        MegaStratumQueueMeta meta2 = new ConfigurableMegaStratumQueueMeta();
        meta2.setQueueTableName( "hydra_temporary_queue_nodes" );
        MagnitudeDPQueue magnitudeDPQueue = new MagnitudeDPQueue(driver, 0, "segment_name", "测试队列", meta1);
        MegaDPStratumQueue megaDPStratumQueue = new MegaDPStratumQueue(driver, "segment_name", "测试临时队列", meta2);

        MegaInDegreeFirstStrategy strategyChain = new MegaInDegreeFirstStrategy(uniformRuntimeAtlas, magnitudeDPQueue, megaDPStratumQueue,layerInstrument);
        AtlasPriorityProcessStrategy atlasPriorityProcessStrategy = new AtlasPriorityProcessStrategy();
        atlasPriorityProcessStrategy.addStrategy( strategyChain );
        GenericTapedBFSGraphAdvancer advancer = new GenericTapedBFSGraphAdvancer( uniformRuntimeAtlas, magnitudeDPQueue,atlasPriorityProcessStrategy );
        advancer.traverse(magnitudeVectorDAG);
    }

    public void testOrchestrator(LayerInstrument layerInstrument, UniformRuntimeAtlas uniformRuntimeAtlas, KOIMappingDriver driver, TaskInstrument taskInstrument) {
//        VectorDAG vector = uniformRuntimeAtlas.queryByPath( "l1" );
//        RavenTaskGraphOrchestrator ravenTaskGraphOrchestrator = new RavenTaskGraphOrchestrator( vector, layerInstrument, 5,1,uniformRuntimeAtlas,driver );
//        ravenTaskGraphOrchestrator.execute();

//        Debug.trace( this.getSystemGuidAllocator().nextGUID() );

        WolfMCServer wolfKing = new WolfMCServer( "", this, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );
        CollectiveTaskRegiment regiment = new RavenCollectiveTaskRegiment( this, (CentralizedTaskInstrument) taskInstrument, wolfKing );

        RavenTaskScheduler scheduler = new RavenTaskScheduler( ( CentralizedTaskInstrument ) taskInstrument, uniformRuntimeAtlas, regiment.taskExecutionElevator() );
        scheduler.fetch();
    }


}
public class TestRuntime {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Rick rick = (Rick) Pinecone.sys().getTaskManager().add( new Rick( args, Pinecone.sys() ) );
            rick.vitalize();
            return 0;
        }, (Object[]) args );
    }
}
