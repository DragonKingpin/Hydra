package com.pinecone.slime.chunk.scheduler;

import com.pinecone.framework.system.prototype.Pinenut;

public interface PageScheduler extends Pinenut {
    PageScheduler setPageRecycleStrategy( PageRecycleStrategy strategy );

    PageRecycleStrategy getPageRecycleStrategy();
}
