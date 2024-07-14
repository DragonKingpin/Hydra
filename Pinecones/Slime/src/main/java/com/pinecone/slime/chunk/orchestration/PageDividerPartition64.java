package com.pinecone.slime.chunk.orchestration;

import com.pinecone.slime.chunk.ContiguousPage;

public interface PageDividerPartition64 extends PagePartition {
    long pagesSize();

    long eachPerPage();

    void inheritRange( ContiguousPage that );

    void setEachPerPage( long eachPerPage );
}
