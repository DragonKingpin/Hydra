package com.pinecone.hydra.atlas.advance;

import com.pinecone.hydra.atlas.advance.chain.GraphPriorityProcessStrategyChain;
import com.pinecone.hydra.atlas.graph.RuntimeAtlasInstrument;
import com.pinecone.hydra.unit.iqueue.MegaDeflectPriorityQueue;
import com.pinecone.hydra.unit.iqueue.MegaStratumQueue;
import com.pinecone.hydra.unit.vgraph.VectorDAG;

public class GenericTapedBFSGraphAdvancer implements TapedBFSGraphStratumAdvancer {
    private RuntimeAtlasInstrument          mRuntimeAtlasInstrument;

    private MegaDeflectPriorityQueue        mMegaDeflectPriorityQueue;

    private MegaStratumQueue                mTempMegaStratumQueue;

    private GraphPriorityProcessStrategyChain mStrategyChain;

    public GenericTapedBFSGraphAdvancer(RuntimeAtlasInstrument runtimeAtlasInstrument, MegaDeflectPriorityQueue megaDeflectPriorityQueue,
                                        MegaStratumQueue tempMegaStratumQueue, GraphPriorityProcessStrategyChain strategyChain ) {
        this.mRuntimeAtlasInstrument    = runtimeAtlasInstrument;
        this.mMegaDeflectPriorityQueue  = megaDeflectPriorityQueue;
        this.mTempMegaStratumQueue      = tempMegaStratumQueue;
        this.mStrategyChain             = strategyChain;
    }


    public void traverse( VectorDAG vectorDAG ) {
        GraphPriorityProcessStrategyChain strategyChain = this.mStrategyChain;
         while ( strategyChain != null ) {
             strategyChain.process(vectorDAG);
             strategyChain = strategyChain.next();
         }
    }

}
