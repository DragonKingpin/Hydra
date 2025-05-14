package com.pinecone.hydra.atlas.advance.chain;

import com.pinecone.hydra.unit.vgraph.VectorDAG;

public interface StrategyChain {
    void execute(VectorDAG vectorDAG);

    StrategyChain addNext( StrategyChain strategyChain );

    StrategyChain next();
}
