package com.pinecone.slime.chunk.scheduler;

import com.pinecone.slime.chunk.RangedPage;

public abstract class BatchActivePageScheduler64 extends ActivePageScheduler64 implements BatchActivePageScheduler {
    protected long                 mnBatchSize;
    protected long                 mnBatchEpoch;

    protected BatchActivePageScheduler64(RangedPage masterPage, PagePool pagePool, PageDivider divider, long autoIncrementId, long batchSize ) {
        super( masterPage, pagePool, divider, autoIncrementId );
        this.mnBatchSize           = batchSize;
        this.mnBatchEpoch          = 0;
    }

    protected BatchActivePageScheduler64( PageDivider divider, long autoIncrementId, long batchSize ) {
        super( divider, autoIncrementId );
        this.mnBatchSize           = batchSize;
        this.mnBatchEpoch          = 0;
    }

    @Override
    public long batchSize(){
        return this.mnBatchSize;
    }

    @Override
    public long getBatchEpoch(){
        return this.mnBatchEpoch;
    }
}
