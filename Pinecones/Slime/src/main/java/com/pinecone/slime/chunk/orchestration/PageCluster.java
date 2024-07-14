package com.pinecone.slime.chunk.orchestration;

import com.pinecone.slime.chunk.ContiguousPage;
import com.pinecone.slime.cluster.RangedCluster;

public interface PageCluster extends RangedCluster {
    boolean hasOwnPage( ContiguousPage that );
}
