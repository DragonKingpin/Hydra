package com.pinecone.hydra.storage.volume.block.stripe;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class StripedIoPlan implements Pinenut {
    protected final long              mnPosition;
    protected final int               mnBufferPosition;
    protected final int               mnTotalLength;
    protected final List<StripeSlice> mSlices;
    protected final List<StripeLane>  mLanes;

    public StripedIoPlan( long position, int bufferPosition, int totalLength, int laneCount, List<StripeSlice> slices ) {
        if ( position < 0L ) {
            throw new IllegalArgumentException( "Negative striped IO position: " + position );
        }
        if ( bufferPosition < 0 ) {
            throw new IllegalArgumentException( "Negative striped IO buffer position: " + bufferPosition );
        }
        if ( totalLength < 0 ) {
            throw new IllegalArgumentException( "Negative striped IO length: " + totalLength );
        }
        if ( laneCount <= 0 ) {
            throw new IllegalArgumentException( "Striped IO lane count must be positive: " + laneCount );
        }
        this.mnPosition = position;
        this.mnBufferPosition = bufferPosition;
        this.mnTotalLength = totalLength;
        this.mSlices = Collections.unmodifiableList( new ArrayList<>( slices ) );
        this.mLanes = this.buildLanes( laneCount, slices );
    }

    public long getPosition() {
        return this.mnPosition;
    }

    public int getBufferPosition() {
        return this.mnBufferPosition;
    }

    public int getTotalLength() {
        return this.mnTotalLength;
    }

    public List<StripeSlice> getSlices() {
        return this.mSlices;
    }

    public List<StripeLane> getLanes() {
        return this.mLanes;
    }

    public int getActiveLaneCount() {
        int count = 0;
        for ( StripeLane lane : this.mLanes ) {
            if ( !lane.isEmpty() ) {
                count++;
            }
        }
        return count;
    }

    protected List<StripeLane> buildLanes( int laneCount, List<StripeSlice> slices ) {
        ArrayList<StripeLane> lanes = new ArrayList<>( laneCount );
        for ( int index = 0; index < laneCount; index++ ) {
            lanes.add( new StripeLane( index ) );
        }
        for ( StripeSlice slice : slices ) {
            if ( slice.getChildIndex() >= laneCount ) {
                throw new IllegalArgumentException( "Stripe slice child index out of range: " + slice.getChildIndex() );
            }
            lanes.get( slice.getChildIndex() ).addSlice( slice );
        }
        return Collections.unmodifiableList( lanes );
    }
}
