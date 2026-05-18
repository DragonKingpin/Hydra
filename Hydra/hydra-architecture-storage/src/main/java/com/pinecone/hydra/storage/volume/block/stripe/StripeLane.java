package com.pinecone.hydra.storage.volume.block.stripe;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class StripeLane implements Pinenut {
    protected final int               mnChildIndex;
    protected final List<StripeSlice> mSlices;

    public StripeLane( int childIndex ) {
        if ( childIndex < 0 ) {
            throw new IllegalArgumentException( "Negative stripe lane index: " + childIndex );
        }
        this.mnChildIndex = childIndex;
        this.mSlices = new ArrayList<>();
    }

    public int getChildIndex() {
        return this.mnChildIndex;
    }

    public List<StripeSlice> getSlices() {
        return Collections.unmodifiableList( this.mSlices );
    }

    public boolean isEmpty() {
        return this.mSlices.isEmpty();
    }

    void addSlice( StripeSlice slice ) {
        if ( slice.getChildIndex() != this.mnChildIndex ) {
            throw new IllegalArgumentException( "Stripe slice does not belong to lane " + this.mnChildIndex );
        }
        this.mSlices.add( slice );
    }
}
