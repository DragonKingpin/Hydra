package com.pinecone.slime.chunk.flow;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ChunkFlowStrategy extends Pinenut {
    void prepare( ChunkFlowRuntime runtime );

    boolean hasNext( ChunkFlowRuntime runtime );
}
