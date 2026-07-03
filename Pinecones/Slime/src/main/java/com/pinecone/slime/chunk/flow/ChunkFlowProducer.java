package com.pinecone.slime.chunk.flow;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ChunkFlowProducer<T> extends Pinenut {
    T produce();
}
