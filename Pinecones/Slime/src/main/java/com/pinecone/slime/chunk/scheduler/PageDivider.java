package com.pinecone.slime.chunk.scheduler;

public interface PageDivider extends ChunkDivider {
    PagePool getPagePool();

    long getPageIdOffset();

    void setPageIdOffset( long offset );
}
