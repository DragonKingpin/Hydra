package com.pinecone.hydra.atlas.advance.chain;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.unit.vgraph.VectorDAG;

public interface GraphPriorityProcessStrategyChain extends Pinenut {
    void process( VectorDAG vectorDAG );

    GraphPriorityProcessStrategyChain addNext(GraphPriorityProcessStrategyChain strategyChain );

    GraphPriorityProcessStrategyChain next();
}
