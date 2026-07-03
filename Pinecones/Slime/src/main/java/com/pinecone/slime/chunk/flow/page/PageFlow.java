package com.pinecone.slime.chunk.flow.page;

import com.pinecone.slime.chunk.flow.ChunkFlow;

public interface PageFlow extends ChunkFlow {
    @Override
    PageFlowRuntime runtime();

    @Override
    PageFlowStrategy strategy();
}
