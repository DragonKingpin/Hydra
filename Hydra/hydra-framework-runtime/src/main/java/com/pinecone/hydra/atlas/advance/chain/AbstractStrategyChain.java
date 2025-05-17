package com.pinecone.hydra.atlas.advance.chain;

public abstract class AbstractStrategyChain implements GraphPriorityProcessStrategyChain {
    protected GraphPriorityProcessStrategyChain mNextStrategyChain;

    public AbstractStrategyChain( GraphPriorityProcessStrategyChain strategyChain ) {
        this.mNextStrategyChain = strategyChain;
    }

    public AbstractStrategyChain() {

    }

    @Override
    public GraphPriorityProcessStrategyChain addNext(GraphPriorityProcessStrategyChain strategyChain) {
        this.mNextStrategyChain = strategyChain;
        return this.mNextStrategyChain;
    }

    @Override
    public GraphPriorityProcessStrategyChain next() {
        return this.mNextStrategyChain;
    }
}
