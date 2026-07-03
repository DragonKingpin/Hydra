package com.pinecone.slime.chunk.flow;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ChunkFlow extends Pinenut {
    ChunkFlowRuntime runtime();

    ChunkFlowStrategy strategy();
}
