package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.atlas.graph.UniformRuntimeAtlas;
import com.pinecone.hydra.atlas.graph.entity.TaskAtlasNode;
import com.pinecone.hydra.atlas.runtime.ibatis.hydranium.RuntimeMappingDriver;
import com.pinecone.hydra.unit.vgraph.source.AtlasMappingDriver;
import com.pinecone.radium.Radium;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.ulf.util.guid.GUIDs;

import java.util.List;

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
        UniformRuntimeAtlas uniformRuntimeAtlas = new UniformRuntimeAtlas(atlasMappingDriver);
        this.testInsert(uniformRuntimeAtlas);
    }

    public void testInsert(UniformRuntimeAtlas uniformRuntimeAtlas) {
        TaskAtlasNode taskAtlasNode = new TaskAtlasNode();
        taskAtlasNode.setName("这是测试图节点3");
        uniformRuntimeAtlas.put(taskAtlasNode);
        //uniformRuntimeAtlas.put(GUIDs.GUID72("20dc3d8-00007b-0000-50"), taskAtlasNode);
    }

    public void testQuery(UniformRuntimeAtlas uniformRuntimeAtlas) {
        GuidAllocator guidAllocator = uniformRuntimeAtlas.getGuidAllocator();
//        TaskGraphNode query = uniformRuntimeAtlas.query(GUIDs.GUID72("20dc3d8-00007b-0000-50"));
//        Debug.trace(query.toJSONString());
        List<String> path = uniformRuntimeAtlas.getPath(GUIDs.GUID72("210f43c-000017-0000-64"));
        Debug.trace(path);
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
