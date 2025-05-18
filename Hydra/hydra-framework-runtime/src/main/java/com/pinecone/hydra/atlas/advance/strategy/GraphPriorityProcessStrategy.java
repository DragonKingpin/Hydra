package com.pinecone.hydra.atlas.advance.strategy;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.unit.vgraph.VectorDAG;

public interface GraphPriorityProcessStrategy extends Pinenut {
    void process( VectorDAG vectorDAG );
}
