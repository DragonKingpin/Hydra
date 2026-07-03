package com.pinecone.slime.chunk.flow;

public abstract class ArchChunkFlow implements ChunkFlow {
    protected ChunkFlowRuntime  mRuntime;
    protected ChunkFlowStrategy mStrategy;

    protected ArchChunkFlow( ChunkFlowRuntime runtime, ChunkFlowStrategy strategy ) {
        this.mRuntime  = runtime;
        this.mStrategy = strategy;
    }

    @Override
    public ChunkFlowRuntime runtime() {
        return this.mRuntime;
    }

    @Override
    public ChunkFlowStrategy strategy() {
        return this.mStrategy;
    }
}
