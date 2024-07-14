package com.pinecone.slime.chunk.scheduler;

import com.pinecone.slime.chunk.ContiguousPage;

public class DefaultPageRecycleStrategy implements PageRecycleStrategy {
    protected PageScheduler mPageScheduler;

    public DefaultPageRecycleStrategy( PageScheduler parent ){
        this.mPageScheduler = parent;
    }

    @Override
    public PageScheduler parentScheduler(){
        return this.mPageScheduler;
    }

    @Override
    public boolean qualified( ContiguousPage that ){
        return false;
    }
}
