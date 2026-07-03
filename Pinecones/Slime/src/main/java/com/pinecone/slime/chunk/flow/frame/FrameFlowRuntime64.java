package com.pinecone.slime.chunk.flow.frame;

import com.pinecone.slime.unitization.NumPrecision;
import com.pinecone.slime.unitization.Precision64;

public class FrameFlowRuntime64 extends ArchFrameFlowRuntime {
    protected long mnFrameLimit;

    public FrameFlowRuntime64( long frameLimit ) {
        this.mnFrameLimit = frameLimit;
    }

    public long frameLimit64() {
        return this.mnFrameLimit;
    }

    @Override
    public NumPrecision frameLimit() {
        return new Precision64( this.frameLimit64() );
    }
}
