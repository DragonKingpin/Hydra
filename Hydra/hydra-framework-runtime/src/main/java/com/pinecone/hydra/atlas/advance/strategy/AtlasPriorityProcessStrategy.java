package com.pinecone.hydra.atlas.advance.strategy;

import com.pinecone.hydra.unit.vgraph.VectorDAG;

import java.util.ArrayList;
import java.util.List;

public class AtlasPriorityProcessStrategy implements PriorityProcessStrategy {
    protected List<GraphPriorityProcessStrategy > mStrategyPipeline;


    public AtlasPriorityProcessStrategy() {
        this.mStrategyPipeline                  = new ArrayList<>();
    }

    @Override
    public void process( VectorDAG vectorDAG ) {
        for( GraphPriorityProcessStrategy strategy : this.mStrategyPipeline ) {
            strategy.process( vectorDAG );
        }
    }

    @Override
    public void addStrategy(GraphPriorityProcessStrategy strategy) {
        this.mStrategyPipeline.add( strategy );
    }
}
