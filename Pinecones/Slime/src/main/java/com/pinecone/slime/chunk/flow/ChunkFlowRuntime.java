package com.pinecone.slime.chunk.flow;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ChunkFlowRuntime extends Pinenut {
    ChunkFlowStatus status();
}
