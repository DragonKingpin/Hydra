package com.pinecone.slime.chunk.flow.window;

import com.pinecone.slime.chunk.flow.ChunkFlowStatus;
import com.pinecone.slime.chunk.flow.page.ArchPageFlowRuntime;

public abstract class ArchWindowedPageFlowRuntime extends ArchPageFlowRuntime implements WindowedPageFlowRuntime {
    protected ArchWindowedPageFlowRuntime() {
        super();
    }

    protected ArchWindowedPageFlowRuntime( ChunkFlowStatus status ) {
        super( status );
    }
}
