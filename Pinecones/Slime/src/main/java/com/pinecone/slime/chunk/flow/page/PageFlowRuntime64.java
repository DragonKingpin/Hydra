package com.pinecone.slime.chunk.flow.page;

import com.pinecone.slime.unitization.NumPrecision;
import com.pinecone.slime.unitization.Precision64;

public class PageFlowRuntime64 extends ArchPageFlowRuntime {
    protected long mnPageSize;

    public PageFlowRuntime64( long pageSize ) {
        this.mnPageSize = pageSize;
    }

    public long pageSize64() {
        return this.mnPageSize;
    }

    @Override
    public NumPrecision pageSize() {
        return new Precision64( this.pageSize64() );
    }
}
