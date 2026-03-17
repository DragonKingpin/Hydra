package com.walnut.odin.atlas.advance;

import com.walnut.odin.atlas.advance.strategy.PriorityProcessStrategy;
import com.walnut.odin.atlas.graph.RuntimeAtlasInstrument;
import com.pinecone.hydra.unit.iqueue.DeflectPriorityQueue;
import com.pinecone.hydra.unit.iqueue.QueueExistManipulator;
import com.pinecone.hydra.unit.iqueue.entity.QueueElement;
import com.pinecone.hydra.unit.vgraph.VectorDAG;

import java.util.List;

public class GenericTapedBFSGraphAdvancer implements TapedBFSGraphStratumAdvancer {
    private RuntimeAtlasInstrument          mRuntimeAtlasInstrument;

    private QueueExistManipulator           mQueueExistManipulator;

    private DeflectPriorityQueue            mDeflectPriorityQueue;

    private PriorityProcessStrategy         mStrategy;

    public GenericTapedBFSGraphAdvancer( RuntimeAtlasInstrument runtimeAtlasInstrument, DeflectPriorityQueue deflectPriorityQueue, PriorityProcessStrategy strategy ) {
        this.mRuntimeAtlasInstrument    = runtimeAtlasInstrument;
        this.mDeflectPriorityQueue      = deflectPriorityQueue;
        this.mQueueExistManipulator     = deflectPriorityQueue.getMasterManipulator().getQueueExistManipulator();
        this.mStrategy = strategy;
    }

    public void traverse( VectorDAG vectorDAG ) {
        if( !this.mQueueExistManipulator.isExist( vectorDAG.getAffiliateLayerGuid() ) ) {
            this.mQueueExistManipulator.setQueueExist( vectorDAG.getAffiliateLayerGuid() );
            this.mStrategy.process( vectorDAG );
        }

    }

    @Override
    public List<QueueElement> fetchExecuteNode( long offset, long limit ) {
        return this.mDeflectPriorityQueue.fetchElements( offset, limit );
    }
}
