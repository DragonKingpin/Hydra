package com.pinecone.slime.chunk.scheduler;

import com.pinecone.slime.chunk.ContiguousPage;

public interface RangedPageScheduler extends PageScheduler {
    long getAutoIncrementId();

    PageDivider getDivider();

    ContiguousPage getMasterPage();
}
