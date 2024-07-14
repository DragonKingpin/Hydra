package com.pinecone.slime.chunk.scheduler;

import com.pinecone.slime.chunk.Chunk;
import com.pinecone.slime.chunk.Splitunk;

public abstract class ArchMasterSplitunkDivider64 implements ChunkDivider {
    protected Splitunk      mMasterChunk;

    protected ArchMasterSplitunkDivider64( Splitunk masterChunk ) {
        this.mMasterChunk = masterChunk;
    }

    protected abstract Chunk newChunk( long start, long end, long epoch );

    @Override
    public Splitunk getMasterChunk(){
        return this.mMasterChunk;
    }
}
