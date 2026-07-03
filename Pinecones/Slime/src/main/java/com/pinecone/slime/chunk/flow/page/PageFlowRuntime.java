package com.pinecone.slime.chunk.flow.page;

import com.pinecone.slime.chunk.flow.ChunkFlowRuntime;
import com.pinecone.slime.unitization.NumPrecision;

public interface PageFlowRuntime extends ChunkFlowRuntime {
    NumPrecision pageSize();
}
