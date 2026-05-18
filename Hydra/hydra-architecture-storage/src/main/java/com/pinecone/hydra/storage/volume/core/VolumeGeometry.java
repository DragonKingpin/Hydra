package com.pinecone.hydra.storage.volume.core;

public class VolumeGeometry {
    protected long mnLogicalSize;
    protected long mnStripeUnit;
    protected long mnMemberLength;
    protected int  mnMemberCount;

    public long getLogicalSize() {
        return this.mnLogicalSize;
    }

    public void setLogicalSize( long logicalSize ) {
        this.mnLogicalSize = logicalSize;
    }

    public long getStripeUnit() {
        return this.mnStripeUnit;
    }

    public void setStripeUnit( long stripeUnit ) {
        this.mnStripeUnit = stripeUnit;
    }

    public long getMemberLength() {
        return this.mnMemberLength;
    }

    public void setMemberLength( long memberLength ) {
        this.mnMemberLength = memberLength;
    }

    public int getMemberCount() {
        return this.mnMemberCount;
    }

    public void setMemberCount( int memberCount ) {
        this.mnMemberCount = memberCount;
    }
}

