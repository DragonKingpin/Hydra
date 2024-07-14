package com.pinecone.slime.cluster;

import com.pinecone.slime.chunk.DiscreteChunk;
import com.pinecone.slime.chunk.PatriarchalChunk;

public interface ChunkGroup extends Cluster, PatriarchalChunk, DiscreteChunk {
}
