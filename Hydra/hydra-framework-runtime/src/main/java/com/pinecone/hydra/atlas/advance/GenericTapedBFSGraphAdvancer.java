package com.pinecone.hydra.atlas.advance;

import com.pinecone.hydra.atlas.advance.strategy.PriorityProcessStrategy;
import com.pinecone.hydra.atlas.graph.RuntimeAtlasInstrument;
import com.pinecone.hydra.orchestration.GraphNode;
import com.pinecone.hydra.unit.iqueue.MegaDeflectPriorityQueue;
import com.pinecone.hydra.unit.iqueue.MegaStratumQueue;
import com.pinecone.hydra.unit.iqueue.entity.QueueElement;
import com.pinecone.hydra.unit.vgraph.VectorDAG;

import java.util.List;

public class GenericTapedBFSGraphAdvancer implements TapedBFSGraphStratumAdvancer {
    private RuntimeAtlasInstrument          mRuntimeAtlasInstrument;

    private MegaDeflectPriorityQueue        mMegaDeflectPriorityQueue;

    private PriorityProcessStrategy         mStrategy;

    public GenericTapedBFSGraphAdvancer(RuntimeAtlasInstrument runtimeAtlasInstrument, MegaDeflectPriorityQueue megaDeflectPriorityQueue, PriorityProcessStrategy strategy ) {
        this.mRuntimeAtlasInstrument    = runtimeAtlasInstrument;
        this.mMegaDeflectPriorityQueue  = megaDeflectPriorityQueue;
        this.mStrategy = strategy;
    }


    public void traverse( VectorDAG vectorDAG ) {
       this.mStrategy.process( vectorDAG );
    }

    @Override
    public List<QueueElement> fetchExecuteNode(long offset, long limit) {
        return this.mMegaDeflectPriorityQueue.fetchElements( offset, limit );
    }
}
