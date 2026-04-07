package com.walnut.odin.atlas.advance.strategy;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.unit.vgraph.VectorDAG;

public interface PriorityProcessStrategy extends Pinenut {
    void process( VectorDAG vectorDAG );

    void addStrategy( GraphPriorityProcessStrategy strategy );
}
