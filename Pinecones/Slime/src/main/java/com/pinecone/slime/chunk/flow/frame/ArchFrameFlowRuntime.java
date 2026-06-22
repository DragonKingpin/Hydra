package com.pinecone.slime.chunk.flow.frame;

import com.pinecone.slime.chunk.flow.ArchChunkFlowRuntime;
import com.pinecone.slime.chunk.flow.ChunkFlowStatus;

public abstract class ArchFrameFlowRuntime extends ArchChunkFlowRuntime implements FrameFlowRuntime {
    protected ArchFrameFlowRuntime() {
        super();
    }

    protected ArchFrameFlowRuntime( ChunkFlowStatus status ) {
        super( status );
    }
}
