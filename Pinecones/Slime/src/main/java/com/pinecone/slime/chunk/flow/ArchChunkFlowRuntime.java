package com.pinecone.slime.chunk.flow;

public abstract class ArchChunkFlowRuntime implements ChunkFlowRuntime {
    protected ChunkFlowStatus mStatus;

    protected ArchChunkFlowRuntime() {
        this( ChunkFlowStatus.Prepared );
    }

    protected ArchChunkFlowRuntime( ChunkFlowStatus status ) {
        this.mStatus = status;
    }

    @Override
    public ChunkFlowStatus status() {
        return this.mStatus;
    }

    public void setStatus( ChunkFlowStatus status ) {
        this.mStatus = status;
    }
}
