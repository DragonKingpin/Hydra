package com.pinecone.hydra.atlas.advance;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.atlas.advance.chain.StrategyChain;
import com.pinecone.hydra.atlas.graph.RuntimeAtlasInstrument;
import com.pinecone.hydra.task.kom.TaskInstrument;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.unit.iqueue.MegaDeflectPriorityQueue;
import com.pinecone.hydra.unit.iqueue.MegaStratumQueue;
import com.pinecone.hydra.unit.iqueue.entity.GenericQueueElement;
import com.pinecone.hydra.unit.vgraph.VectorDAG;

import java.util.ArrayDeque;
import java.util.List;

public class GenericTapedBFSGraphAdvancer implements TapedBFSGraphStratumAdvancer {
    private RuntimeAtlasInstrument          mRuntimeAtlasInstrument;

    private MegaDeflectPriorityQueue        mMegaDeflectPriorityQueue;

    private MegaStratumQueue                mTempMegaStratumQueue;

    private StrategyChain                   mStrategyChain;

    public GenericTapedBFSGraphAdvancer( RuntimeAtlasInstrument runtimeAtlasInstrument, MegaDeflectPriorityQueue megaDeflectPriorityQueue,
                                         MegaStratumQueue tempMegaStratumQueue,StrategyChain strategyChain ) {
        this.mRuntimeAtlasInstrument    = runtimeAtlasInstrument;
        this.mMegaDeflectPriorityQueue  = megaDeflectPriorityQueue;
        this.mTempMegaStratumQueue      = tempMegaStratumQueue;
        this.mStrategyChain             = strategyChain;
    }


    public void traverse( VectorDAG vectorDAG ) {
        this.mStrategyChain.execute( vectorDAG );
    }

}
