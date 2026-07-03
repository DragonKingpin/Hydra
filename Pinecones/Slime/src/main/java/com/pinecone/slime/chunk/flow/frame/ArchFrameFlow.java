package com.pinecone.slime.chunk.flow.frame;

import com.pinecone.slime.chunk.flow.ArchChunkFlow;

public abstract class ArchFrameFlow extends ArchChunkFlow implements FrameFlow {
    protected ArchFrameFlow( FrameFlowRuntime runtime, FrameFlowStrategy strategy ) {
        super( runtime, strategy );
    }

    @Override
    public FrameFlowRuntime runtime() {
        return (FrameFlowRuntime)this.mRuntime;
    }

    @Override
    public FrameFlowStrategy strategy() {
        return (FrameFlowStrategy)this.mStrategy;
    }
}
