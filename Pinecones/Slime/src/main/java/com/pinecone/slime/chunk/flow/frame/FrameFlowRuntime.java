package com.pinecone.slime.chunk.flow.frame;

import com.pinecone.slime.chunk.flow.ChunkFlowRuntime;
import com.pinecone.slime.unitization.NumPrecision;

public interface FrameFlowRuntime extends ChunkFlowRuntime {
    NumPrecision frameLimit();
}
