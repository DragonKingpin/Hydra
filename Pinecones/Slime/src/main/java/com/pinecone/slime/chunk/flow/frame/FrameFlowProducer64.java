package com.pinecone.slime.chunk.flow.frame;

public abstract class FrameFlowProducer64 implements FrameFlowProducer {
    protected FrameFlowRuntime mRuntime;

    protected FrameFlowProducer64( FrameFlowRuntime runtime ) {
        this.mRuntime = runtime;
    }

    public FrameFlowRuntime runtime() {
        return this.mRuntime;
    }
}
