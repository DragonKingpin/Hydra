package com.pinecone.hydra.atlas.advance.chain;

public abstract class AbstractStrategyChain implements StrategyChain {
    protected StrategyChain mNextStrategyChain;

    public AbstractStrategyChain( StrategyChain strategyChain ) {
        this.mNextStrategyChain = strategyChain;
    }

    public AbstractStrategyChain() {

    }

    @Override
    public StrategyChain addNext(StrategyChain strategyChain) {
        this.mNextStrategyChain = strategyChain;
        return this.mNextStrategyChain;
    }

    @Override
    public StrategyChain next() {
        return this.mNextStrategyChain;
    }
}
