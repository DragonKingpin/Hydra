package com.pinecone.slime.chunk.flow.window;

import com.pinecone.slime.chunk.flow.page.PageFlowProducer64;

public abstract class WindowedPageFlowProducer64 extends PageFlowProducer64 implements WindowedPageFlowProducer {
    protected WindowedPageFlowProducer64( WindowedPageFlowRuntime runtime ) {
        super( runtime );
    }

    @Override
    public WindowedPageFlowRuntime runtime() {
        return (WindowedPageFlowRuntime)super.runtime();
    }
}
