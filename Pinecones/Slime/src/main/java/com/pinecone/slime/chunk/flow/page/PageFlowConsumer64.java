package com.pinecone.slime.chunk.flow.page;

public abstract class PageFlowConsumer64 implements PageFlowConsumer {
    protected PageFlowRuntime mRuntime;

    protected PageFlowConsumer64( PageFlowRuntime runtime ) {
        this.mRuntime = runtime;
    }

    public PageFlowRuntime runtime() {
        return this.mRuntime;
    }
}
