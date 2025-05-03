package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.atlas.advance.GraphStratumTape;
import com.pinecone.hydra.atlas.graph.UniformRuntimeAtlas;
import com.pinecone.hydra.atlas.graph.entity.TaskAtlasNode;
import com.pinecone.hydra.atlas.runtime.ibatis.hydranium.RuntimeMappingDriver;
import com.pinecone.hydra.queue.ibatis.hydranium.QueueMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.task.ibatis.hydranium.TaskMappingDriver;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.unit.vgraph.GenericVectorDAG;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.source.AtlasMappingDriver;
import com.pinecone.radium.Radium;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.GUIDs;

class Rick extends Radium {
    public Rick( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Rick( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        AtlasMappingDriver atlasMappingDriver = new RuntimeMappingDriver(
                this,(IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ),this.getDispenserCenter()
        );

        KOIMappingDriver koiMappingDriver = new QueueMappingDriver(
                this, (IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ), this.getDispenserCenter()
        );

        KOIMappingDriver driver = new TaskMappingDriver(
                this,(IbatisClient)this.getMiddlewareDirector().getRDBManager().getRDBClientByName( "MySQLKingHydranium" ),this.getDispenserCenter()
        );
        UniformTaskInstrument uniformTaskInstrument = new UniformTaskInstrument( driver );

        UniformRuntimeAtlas uniformRuntimeAtlas = new UniformRuntimeAtlas(atlasMappingDriver, uniformTaskInstrument);
        //this.testInsert(uniformRuntimeAtlas);
        //this.testQuery( uniformRuntimeAtlas );
        this.testAdvancer( uniformRuntimeAtlas, koiMappingDriver );
    }

    public void testInsert(UniformRuntimeAtlas uniformRuntimeAtlas) {
        TaskAtlasNode taskAtlasNode = new TaskAtlasNode();
        taskAtlasNode.setName("这是测试图节点5");
        uniformRuntimeAtlas.put(taskAtlasNode,GUIDs.GUID72( "21164d6-0003e5-000f-50" ));
        //uniformRuntimeAtlas.put(GUIDs.GUID72("20dc3d8-00007b-0000-50"), taskAtlasNode);
    }

    public void testQuery(UniformRuntimeAtlas uniformRuntimeAtlas) {
//        GuidAllocator guidAllocator = uniformRuntimeAtlas.getGuidAllocator();
//        TaskGraphNode query = uniformRuntimeAtlas.query(GUIDs.GUID72("20dc3d8-00007b-0000-50"));
//        Debug.trace(query.toJSONString());
//        List<String> path = uniformRuntimeAtlas.getPath(GUIDs.GUID72("210f43c-000017-0000-64"));
//        Debug.trace(path);

        GraphNode graphNode = uniformRuntimeAtlas.queryGraphNodeByTaskGuid(GUIDs.GUID72("21164d6-0003e5-000f-50"));
        Debug.trace(graphNode.toJSONString());

        TaskElement taskElement = uniformRuntimeAtlas.queryTaskElementByGuid(GUIDs.GUID72("233e952-000010-0000-c0"));

        Debug.trace(taskElement.toJSONObject());
    }

    public void testAdvancer(UniformRuntimeAtlas uniformRuntimeAtlas,KOIMappingDriver driver ) {
        GenericVectorDAG genericVectorDAG = new GenericVectorDAG( GUIDs.GUID72("22610ea-00002d-0000-a0"), null,uniformRuntimeAtlas.getMasterManipulator().getVectorGraphMasterManipulator(), uniformRuntimeAtlas.getConfig()  );
        GraphStratumTape tapeded = uniformRuntimeAtlas.tapedGraphStratumAdvancer(genericVectorDAG, driver);
        //Debug.trace(tapeded.next().toJSONString());
        Debug.trace(tapeded.fetchNodes(2,1));

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
