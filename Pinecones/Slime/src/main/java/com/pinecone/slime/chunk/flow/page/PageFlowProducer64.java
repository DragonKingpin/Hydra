package com.pinecone.slime.chunk.flow.page;

public abstract class PageFlowProducer64 implements PageFlowProducer {
    protected PageFlowRuntime mRuntime;

    protected PageFlowProducer64( PageFlowRuntime runtime ) {
        this.mRuntime = runtime;
    }

    public PageFlowRuntime runtime() {
        return this.mRuntime;
    }
}
