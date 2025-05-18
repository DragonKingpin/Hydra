package com.pinecone.hydra.atlas.advance;

import com.pinecone.hydra.atlas.advance.strategy.PriorityProcessStrategy;
import com.pinecone.hydra.atlas.graph.RuntimeAtlasInstrument;
import com.pinecone.hydra.unit.iqueue.MegaDeflectPriorityQueue;
import com.pinecone.hydra.unit.iqueue.MegaStratumQueue;
import com.pinecone.hydra.unit.vgraph.VectorDAG;

public class GenericTapedBFSGraphAdvancer implements TapedBFSGraphStratumAdvancer {
    private RuntimeAtlasInstrument          mRuntimeAtlasInstrument;

    private MegaDeflectPriorityQueue        mMegaDeflectPriorityQueue;

    private MegaStratumQueue                mTempMegaStratumQueue;

    private PriorityProcessStrategy         mStrategy;

    public GenericTapedBFSGraphAdvancer(RuntimeAtlasInstrument runtimeAtlasInstrument, MegaDeflectPriorityQueue megaDeflectPriorityQueue,
                                        MegaStratumQueue tempMegaStratumQueue, PriorityProcessStrategy strategy ) {
        this.mRuntimeAtlasInstrument    = runtimeAtlasInstrument;
        this.mMegaDeflectPriorityQueue  = megaDeflectPriorityQueue;
        this.mTempMegaStratumQueue      = tempMegaStratumQueue;
        this.mStrategy = strategy;
    }


    public void traverse( VectorDAG vectorDAG ) {
       this.mStrategy.process( vectorDAG );
    }

}
