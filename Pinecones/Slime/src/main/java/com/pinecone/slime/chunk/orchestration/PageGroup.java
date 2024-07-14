package com.pinecone.slime.chunk.orchestration;

import com.pinecone.slime.cluster.ChunkGroup;
import com.pinecone.slime.chunk.PatriarchalChunk;

public interface PageGroup extends PatriarchalChunk, ChunkGroup {
    boolean hasOwnPartition( PagePartition that );
}
