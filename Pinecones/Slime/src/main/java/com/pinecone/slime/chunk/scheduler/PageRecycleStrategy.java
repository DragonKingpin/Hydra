package com.pinecone.slime.chunk.scheduler;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.slime.chunk.ContiguousPage;

public interface PageRecycleStrategy extends Pinenut {
    PageScheduler parentScheduler();

    boolean qualified( ContiguousPage that );
}
