package com.pinecone.slime.chunk.scheduler;

import com.pinecone.slime.chunk.ContiguousPage;
import com.pinecone.framework.system.prototype.Pinenut;

public interface PagePool extends Pinenut {
    int size();

    ContiguousPage allocate(Object... args );

    void deallocate( ContiguousPage that );
}
