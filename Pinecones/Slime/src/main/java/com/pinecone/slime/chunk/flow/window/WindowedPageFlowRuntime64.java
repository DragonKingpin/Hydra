package com.pinecone.slime.chunk.flow.window;

import com.pinecone.slime.unitization.NumPrecision;
import com.pinecone.slime.unitization.Precision64;

public class WindowedPageFlowRuntime64 extends ArchWindowedPageFlowRuntime {
    protected long mnPageSize;
    protected long mnWindowSize;
    protected long mnFrameSize;

    public WindowedPageFlowRuntime64( long pageSize, long windowSize, long frameSize ) {
        this.mnPageSize   = pageSize;
        this.mnWindowSize = windowSize;
        this.mnFrameSize  = frameSize;
    }

    public long pageSize64() {
        return this.mnPageSize;
    }

    public long windowSize64() {
        return this.mnWindowSize;
    }

    public long frameSize64() {
        return this.mnFrameSize;
    }

    @Override
    public NumPrecision pageSize() {
        return new Precision64( this.pageSize64() );
    }

    @Override
    public NumPrecision windowSize() {
        return new Precision64( this.windowSize64() );
    }

    @Override
    public NumPrecision frameSize() {
        return new Precision64( this.frameSize64() );
    }
}
