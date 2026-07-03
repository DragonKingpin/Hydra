package com.pinecone.slime.chunk.flow.window;

import com.pinecone.slime.chunk.flow.page.PageFlow;

public interface WindowedPageFlow extends PageFlow {
    @Override
    WindowedPageFlowRuntime runtime();

    @Override
    WindowedPageFlowStrategy strategy();
}
