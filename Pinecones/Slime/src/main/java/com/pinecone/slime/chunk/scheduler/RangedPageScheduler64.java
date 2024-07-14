package com.pinecone.slime.chunk.scheduler;

import com.pinecone.slime.chunk.ContiguousPage;
import com.pinecone.slime.chunk.RangedPage;

public abstract class RangedPageScheduler64 implements RangedPageScheduler {
    protected long                 mnAutoIncrementId;
    protected RangedPage           mMasterPage;
    protected PagePool             mPagePool;
    protected PageDivider          mDivider;

    protected PageRecycleStrategy  mRecycleStrategy;

    protected RangedPageScheduler64( RangedPage masterPage, PagePool pagePool, PageDivider divider, long autoIncrementId ) {
        this.mMasterPage           = masterPage;
        this.mPagePool             = pagePool;
        this.mDivider              = divider;
        this.mnAutoIncrementId     = autoIncrementId;

        this.mRecycleStrategy      = null;
    }

    protected RangedPageScheduler64( PageDivider divider, long autoIncrementId ) {
        this( (RangedPage) divider.getMasterChunk(), divider.getPagePool(), divider, autoIncrementId );
        this.mDivider.setPageIdOffset( autoIncrementId );
    }

    protected abstract void beforeActivatePage() ;

    @Override
    public PageScheduler setPageRecycleStrategy( PageRecycleStrategy strategy ) {
        this.mRecycleStrategy = strategy;
        return this;
    }

    @Override
    public PageRecycleStrategy getPageRecycleStrategy() {
        return this.mRecycleStrategy;
    }

    @Override
    public long getAutoIncrementId() {
        return this.mnAutoIncrementId;
    }

    @Override
    public PageDivider getDivider() {
        return this.mDivider;
    }

    @Override
    public ContiguousPage getMasterPage() {
        return this.mMasterPage;
    }
}
