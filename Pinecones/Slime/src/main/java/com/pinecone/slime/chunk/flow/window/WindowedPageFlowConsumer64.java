package com.pinecone.slime.chunk.flow.window;

import com.pinecone.slime.chunk.flow.page.PageFlowConsumer64;

public abstract class WindowedPageFlowConsumer64 extends PageFlowConsumer64 implements WindowedPageFlowConsumer {
    protected WindowedPageFlowConsumer64( WindowedPageFlowRuntime runtime ) {
        super( runtime );
    }

    @Override
    public WindowedPageFlowRuntime runtime() {
        return (WindowedPageFlowRuntime)super.runtime();
    }
}
