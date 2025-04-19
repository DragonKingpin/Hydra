package com.pinecone.hydra.atlas;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.entity.TaskGraphNode;
import com.pinecone.hydra.unit.vgraph.KArchAtlasInstrument;
import com.pinecone.hydra.unit.vgraph.VectorGraphConfig;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;
import com.pinecone.hydra.unit.vgraph.source.AtlasMappingDriver;

import java.util.List;

public class UniformRuntimeAtlas extends KArchAtlasInstrument implements RuntimeAtlasInstrument {

    public UniformRuntimeAtlas(List<GraphNode> parent, AtlasMappingDriver driver, VectorGraphConfig config) {
        super(parent,driver,config);
    }

    public UniformRuntimeAtlas(AtlasMappingDriver driver) {
        super(driver);
    }

    public GUID put(GraphNode graphNode) {
        return super.put(graphNode);
    }

    public void remove(GUID guid) {
        super.remove(guid);
    }

    public TaskGraphNode query(GUID guid) {
        return (TaskGraphNode) super.get(guid);
    }

}
