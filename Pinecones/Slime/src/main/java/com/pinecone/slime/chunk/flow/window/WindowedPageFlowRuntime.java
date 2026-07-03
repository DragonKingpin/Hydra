package com.pinecone.slime.chunk.flow.window;

import com.pinecone.slime.chunk.flow.page.PageFlowRuntime;
import com.pinecone.slime.unitization.NumPrecision;

public interface WindowedPageFlowRuntime extends PageFlowRuntime {
    NumPrecision windowSize();

    NumPrecision frameSize();
}
