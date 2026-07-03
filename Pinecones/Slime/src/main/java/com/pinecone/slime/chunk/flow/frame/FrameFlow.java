package com.pinecone.slime.chunk.flow.frame;

import com.pinecone.slime.chunk.flow.ChunkFlow;

public interface FrameFlow extends ChunkFlow {
    @Override
    FrameFlowRuntime runtime();

    @Override
    FrameFlowStrategy strategy();
}
