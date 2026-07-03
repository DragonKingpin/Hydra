package com.pinecone.slime.chunk.flow;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ChunkFlowConsumer<T> extends Pinenut {
    void consume( T unit );
}
