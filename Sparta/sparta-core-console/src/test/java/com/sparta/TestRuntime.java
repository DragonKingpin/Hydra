package com.sparta;

import java.util.Map;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.system.regime.arch.Lord;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.event.ProcessEvent;
import com.pinecone.hydra.proc.event.ProcessEventHandler;
import com.pinecone.hydra.proc.event.ProcessLifecycleHandler;
import com.pinecone.hydra.proc.image.ArchEntryPointRunnable;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.LocalHostedClassImage;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.server.WolfMCServer;
import com.walnut.odin.atlas.advance.GenericTapedBFSGraphAdvancer;
import com.walnut.odin.atlas.advance.strategy.AtlasPriorityProcessStrategy;
import com.walnut.odin.atlas.advance.strategy.MegaInDegreeFirstStrategy;
import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.walnut.odin.atlas.graph.UniformRuntimeAtlas;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.unit.iqueue.ConfigurableMegaDeflectPriorityQueueMeta;
import com.pinecone.hydra.unit.iqueue.ConfigurableMegaStratumQueueMeta;
import com.pinecone.hydra.unit.iqueue.MagnitudeDPQueue;
import com.pinecone.hydra.unit.iqueue.MegaDPStratumQueue;
import com.pinecone.hydra.unit.iqueue.MegaDeflectPriorityQueueMeta;
import com.pinecone.hydra.unit.iqueue.MegaStratumQueueMeta;
import com.pinecone.hydra.unit.vgraph.MagnitudeVectorDAG;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.layer.LayerInstrument;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.archcraft.ender.EnderHydra;
import com.walnut.odin.conduct.CollectiveTaskLegionary;
import com.walnut.odin.conduct.RavenCollectiveTaskLegionary;
import com.walnut.odin.conduct.schedule.RavenTaskScheduler;
import com.walnut.odin.system.Odin;
import com.walnut.odin.task.CentralizedTaskInstrument;

class Rick extends EnderHydra {
    public Rick( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Rick( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {

        WolfMCServer wolfKing = new WolfMCServer( "", this, new JSONMaptron("{host: \"0.0.0.0\",\n" +
                "port: 5777, SocketTimeout: 800, KeepAliveTimeout: 3600, MaximumConnections: 1e6}") );
        this.getDispenserCenter().getInstanceDispenser().registerInstance( "TaskWolfKing", wolfKing );

        Lord lord = this.getLordFederation().instantiate( "KernelOdinLord", "./system/setup/lords/odin.json5" );

        Odin odin = (Odin) lord;
        odin.vitalize();


        LayerInstrument layerInstrument = odin.layerInstrument();
        CentralizedTaskInstrument uniformTaskInstrument = odin.taskRegiment().taskInstrument();
        RuntimeAtlasInstrument uniformRuntimeAtlas = odin.atlasInstrument();




        //this.testInsert(uniformRuntimeAtlas);
        //this.testQuery( uniformRuntimeAtlas );
        //this.testTape( uniformRuntimeAtlas, koiMappingDriver );
        //this.testAdvancer( uniformRuntimeAtlas, koiMappingDriver,layerInstrument );
        this.testOrchestrator( odin );
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

    public void testOrchestrator( Odin odin ) throws Exception {
        odin.taskRegiment().startRemoteProcessServer();

        UlfClient ulfClient = new WolfMCClient(
                this.getSystemGuidAllocator72().nextGUIDi64(), "", this, this.getMiddlewareDirector().getMiddlewareConfig().queryJSONObject( "Messagers.Messagers.WolfMCKingpin" )
        );
        CollectiveTaskLegionary regimentClient = new RavenCollectiveTaskLegionary( "jesus", this, ulfClient );
        regimentClient.startService();
        regimentClient.joinRegiment();

        regimentClient.remoteProcessManagerClient().addProcessLifecycleHandler(new ProcessLifecycleHandler() {
            @Override
            public void fired(String imageAddress, EntryPointRunnable runnable, ProcessEvent event ) {
                Debug.greenfs( imageAddress, event );
            }
        });


        ProcessManager manager = regimentClient.processManager();
        ProcessEventHandler eventHandler = new ProcessEventHandler() {
            @Override
            public void fired(EntryPointRunnable runnable, ProcessEvent event ) {
                Debug.bluef( runnable, event );
            }
        };

        ExecutionImage image = new LocalHostedClassImage( "image_c", new ArchEntryPointRunnable( eventHandler ) {
            @Override
            public int main( Map<String, String[]> args ) {
                Debug.greenfs( "Hello, hi, I am `" + this.ownedProcess().getName() + "`!" );
                Debug.sleep( 1000 );
                Debug.greenfs( "Miao~" );

                //throw new IrrationalProvokedException();
                return 1984;
            }
        }, manager );
        manager.getImageLoader().registerLocalScopeExecutionImage( "hola/senorita", image );



        ElementNode n = odin.taskRegiment().taskInstrument().queryElement( "root" );
        Debug.fmp( 2, n.toJSONObject() );


        RavenTaskScheduler scheduler = (RavenTaskScheduler) odin.taskScheduler();
        scheduler.pulseSchedule();
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
