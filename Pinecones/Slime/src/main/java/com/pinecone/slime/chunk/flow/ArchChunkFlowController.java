package com.pinecone.slime.chunk.flow;

public abstract class ArchChunkFlowController implements ChunkFlowController {
    protected ChunkFlow mFlow;

    protected ArchChunkFlowController( ChunkFlow flow ) {
        this.mFlow = flow;
    }

    public ChunkFlow flow() {
        return this.mFlow;
    }
}
