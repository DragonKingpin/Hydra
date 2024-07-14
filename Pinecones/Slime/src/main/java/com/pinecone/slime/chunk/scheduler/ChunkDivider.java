package com.pinecone.slime.chunk.scheduler;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.slime.chunk.Chunk;
import com.pinecone.slime.chunk.DivisibleChunk;

public interface ChunkDivider extends Pinenut {
    Chunk allocate() throws BadAllocateException;

    DivisibleChunk getMasterChunk();

    long getMaxAllocations();

    long remainAllocatable();
}
