package com.pinecone.slime.chunk.scheduler;

import com.pinecone.slime.chunk.ContiguousPage;

public interface ActivePageScheduler extends RangedPageScheduler {
    ContiguousPage activate();

    void activate( ContiguousPage that );

    void deactivate( ContiguousPage that );

    void deactivate( ContiguousPage[] those );

    long getActivatedSize();

    ContiguousPage getPageById(long id );

}
