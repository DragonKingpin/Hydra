package com.pinecone.hydra.atlas;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.entity.TaskGraphNode;
import com.pinecone.hydra.atlas.source.RuntimeMasterManipulator;
import com.pinecone.hydra.unit.vgraph.AtlasInstrument;
import com.pinecone.hydra.unit.vgraph.KArchAtlasInstrument;
import com.pinecone.hydra.unit.vgraph.MegaVectorDAG;
import com.pinecone.hydra.unit.vgraph.VectorGraphConfig;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.source.AtlasMappingDriver;
import com.pinecone.hydra.unit.vgraph.source.AtlasMasterManipulator;

import java.util.List;

public class UniformRuntimeAtlas extends KArchAtlasInstrument implements RuntimeAtlas {

    public UniformRuntimeAtlas(List<GraphNode> parent, AtlasMappingDriver driver, VectorGraphConfig config) {
        super(parent,driver,config);
    }

    public UniformRuntimeAtlas(AtlasMappingDriver driver) {
        super(driver);
    }

    public GUID put(GraphNode graphNode) {
        return this.mMegaVectorDAG.put(graphNode);
    }

    public void remove(GUID guid) {
        this.mMegaVectorDAG.remove(guid);
    }

    public TaskGraphNode query(GUID guid) {
        return (TaskGraphNode) this.mMegaVectorDAG.get(guid);
    }


}
