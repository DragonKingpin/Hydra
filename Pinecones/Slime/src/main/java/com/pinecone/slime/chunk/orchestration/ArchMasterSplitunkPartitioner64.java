package com.pinecone.slime.chunk.orchestration;

import com.pinecone.slime.chunk.Splitunk;

public abstract class ArchMasterSplitunkPartitioner64 implements ChunkPartitioner {
    protected Splitunk mMasterChunk;

    protected ArchMasterSplitunkPartitioner64( Splitunk masterChunk ) {
        this.mMasterChunk = masterChunk;
    }

    @Override
    public Splitunk getMasterChunk(){
        return this.mMasterChunk;
    }
}
