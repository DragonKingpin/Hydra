package com.pinecone.slime.chunk.flow.frame;

public abstract class FrameFlowConsumer64 implements FrameFlowConsumer {
    protected FrameFlowRuntime mRuntime;

    protected FrameFlowConsumer64( FrameFlowRuntime runtime ) {
        this.mRuntime = runtime;
    }

    public FrameFlowRuntime runtime() {
        return this.mRuntime;
    }
}
