package com.pinecone.hydra.atlas.advance.chain;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.unit.vgraph.VectorDAG;

public interface PriorityProcessStrategy extends Pinenut {
    void process( VectorDAG vectorDAG );
}
