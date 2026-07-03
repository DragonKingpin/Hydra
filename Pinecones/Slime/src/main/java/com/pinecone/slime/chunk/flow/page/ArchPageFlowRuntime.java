package com.pinecone.slime.chunk.flow.page;

import com.pinecone.slime.chunk.flow.ArchChunkFlowRuntime;
import com.pinecone.slime.chunk.flow.ChunkFlowStatus;

public abstract class ArchPageFlowRuntime extends ArchChunkFlowRuntime implements PageFlowRuntime {
    protected ArchPageFlowRuntime() {
        super();
    }

    protected ArchPageFlowRuntime( ChunkFlowStatus status ) {
        super( status );
    }
}
