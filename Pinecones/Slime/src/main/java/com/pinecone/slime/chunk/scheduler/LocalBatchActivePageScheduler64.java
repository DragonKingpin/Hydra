package com.pinecone.slime.chunk.scheduler;

import com.pinecone.slime.chunk.ContiguousPage;

public class LocalBatchActivePageScheduler64 extends BatchActivePageScheduler64 {
    public LocalBatchActivePageScheduler64( PageDivider divider, long autoIncrementId, long batchSize ) {
        super( divider, autoIncrementId, batchSize );
        this.mChunkRegister = new LocalMapChunkRegister<>();
    }

    @Override
    public ContiguousPage[] activates(long batch ) {
        long nActivated   = this.getActivatedSize();
        long nAllocations = batch - nActivated;
        long nRemains     = this.getDivider().remainAllocatable();
        if( nRemains < nAllocations ) {
            nAllocations = nRemains;
        }

        int iAlloc = (int) nAllocations;


        ContiguousPage[] pages = new ContiguousPage[ iAlloc ];
        for ( int i = 0; i < iAlloc; i++ ) {
            pages[i] = this.activate();
        }

        ++this.mnBatchEpoch;
        return pages;
    }

    @Override
    public ContiguousPage[] activates() {
        return this.activates( this.batchSize() );
    }
}
