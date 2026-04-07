package com.pinecone.hydra.unit.vgraph.traversal;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.VectorDAG;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;

public class AtlasGraphIterator implements GraphIterator {

    @Override
    public boolean containNode(VectorDAG vectorDAG, GUID targetNodeGuid) {
        return false;
    }

}
