package com.pinecone.slime.chunk.flow.window;

import com.pinecone.slime.chunk.flow.page.ArchPageFlow;

public abstract class ArchWindowedPageFlow extends ArchPageFlow implements WindowedPageFlow {
    protected ArchWindowedPageFlow( WindowedPageFlowRuntime runtime, WindowedPageFlowStrategy strategy ) {
        super( runtime, strategy );
    }

    @Override
    public WindowedPageFlowRuntime runtime() {
        return (WindowedPageFlowRuntime)super.runtime();
    }

    @Override
    public WindowedPageFlowStrategy strategy() {
        return (WindowedPageFlowStrategy)super.strategy();
    }
}
