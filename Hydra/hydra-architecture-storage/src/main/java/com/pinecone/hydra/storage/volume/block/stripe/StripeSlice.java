package com.pinecone.hydra.storage.volume.block.stripe;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.volume.block.BlockVolume;

public final class StripeSlice implements Pinenut {
    protected final BlockVolume mChild;
    protected final int         mnChildIndex;
    protected final long        mnChildPosition;
    protected final int         mnBufferOffset;
    protected final int         mnLength;
    protected final int         mnSequenceNo;

    public StripeSlice(
            BlockVolume child,
            int childIndex,
            long childPosition,
            int bufferOffset,
            int length,
            int sequenceNo
    ) {
        if ( child == null ) {
            throw new IllegalArgumentException( "Stripe child volume is null" );
        }
        if ( childIndex < 0 ) {
            throw new IllegalArgumentException( "Negative stripe child index: " + childIndex );
        }
        if ( childPosition < 0L ) {
            throw new IllegalArgumentException( "Negative stripe child position: " + childPosition );
        }
        if ( bufferOffset < 0 ) {
            throw new IllegalArgumentException( "Negative stripe buffer offset: " + bufferOffset );
        }
        if ( length <= 0 ) {
            throw new IllegalArgumentException( "Stripe slice length must be positive: " + length );
        }
        this.mChild = child;
        this.mnChildIndex = childIndex;
        this.mnChildPosition = childPosition;
        this.mnBufferOffset = bufferOffset;
        this.mnLength = length;
        this.mnSequenceNo = sequenceNo;
    }

    public BlockVolume getChild() {
        return this.mChild;
    }

    public int getChildIndex() {
        return this.mnChildIndex;
    }

    public long getChildPosition() {
        return this.mnChildPosition;
    }

    public int getBufferOffset() {
        return this.mnBufferOffset;
    }

    public int getLength() {
        return this.mnLength;
    }

    public int getSequenceNo() {
        return this.mnSequenceNo;
    }
}
