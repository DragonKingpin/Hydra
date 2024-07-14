package com.pinecone.slime.chunk.scheduler;

import com.pinecone.slime.chunk.ContiguousPage;

public interface BatchActivePageScheduler extends ActivePageScheduler {
    long batchSize();

    long getBatchEpoch();

    ContiguousPage[] activates(long batch );

    default ContiguousPage[] activates() {
        return this.activates( this.batchSize() );
    }
}
