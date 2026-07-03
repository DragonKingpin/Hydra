package com.pinecone.slime.chunk.flow.page;

import com.pinecone.slime.chunk.flow.ArchChunkFlow;

public abstract class ArchPageFlow extends ArchChunkFlow implements PageFlow {
    protected ArchPageFlow( PageFlowRuntime runtime, PageFlowStrategy strategy ) {
        super( runtime, strategy );
    }

    @Override
    public PageFlowRuntime runtime() {
        return (PageFlowRuntime)this.mRuntime;
    }

    @Override
    public PageFlowStrategy strategy() {
        return (PageFlowStrategy)this.mStrategy;
    }
}
