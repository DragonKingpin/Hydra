package com.pinecone.hydra.storage.volume.block;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.ArchVolume;
import com.pinecone.hydra.storage.volume.core.VolumeExtent;
import com.pinecone.hydra.storage.volume.core.VolumeStatus;
import com.pinecone.hydra.storage.volume.core.VolumeType;
import com.pinecone.hydra.storage.volume.block.stripe.StripedIoScheduler;
import com.pinecone.hydra.storage.volume.block.stripe.TitanStripedIoScheduler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TitanStripedVolume extends ArchVolume implements StripedVolume {
    protected final List<VolumeExtent> mMembers;
    protected long                     mnStripeUnit;
    protected long                     mnMemberLength;
    protected StripedIoScheduler       mScheduler;

    public TitanStripedVolume() {
        super();
        this.mVolumeType = VolumeType.STRIPED;
        this.mMembers = new ArrayList<>();
        this.mScheduler = new TitanStripedIoScheduler();
    }

    public TitanStripedVolume( GUID guid, String name, long stripeUnit ) {
        this();
        if ( stripeUnit <= 0 ) {
            throw new IllegalArgumentException( "Stripe unit must be positive: " + stripeUnit );
        }
        this.mGuid = guid;
        this.mszName = name;
        this.mnStripeUnit = stripeUnit;
        this.mStatus = VolumeStatus.READY;
    }

    @Override
    public long getStripeUnit() {
        return this.mnStripeUnit;
    }

    @Override
    public void setStripeUnit( long stripeUnit ) {
        if ( stripeUnit <= 0 ) {
            throw new IllegalArgumentException( "Stripe unit must be positive: " + stripeUnit );
        }
        this.mnStripeUnit = stripeUnit;
    }

    @Override
    public long getMemberLength() {
        return this.mnMemberLength;
    }

    @Override
    public List<VolumeExtent> getMembers() {
        return this.mMembers;
    }

    @Override
    public void addMember( VolumeExtent extent ) {
        if ( extent == null ) {
            throw new IllegalArgumentException( "Striped member is null" );
        }
        if ( !( extent.getChildVolume() instanceof BlockVolume ) ) {
            throw new IllegalArgumentException( "Striped member requires block child volume" );
        }
        if ( this.mMembers.isEmpty() ) {
            this.mnMemberLength = extent.getLength();
        }
        else if ( this.mnMemberLength != extent.getLength() ) {
            throw new IllegalArgumentException(
                    "Striped member length mismatch, expected " + this.mnMemberLength + " but got " + extent.getLength()
            );
        }
        extent.setParentOffset( 0 );
        this.mMembers.add( extent );
        this.mnLogicalSize = this.mnMemberLength * this.mMembers.size();
    }

    @Override
    public int read( long position, ByteBuffer dst ) throws IOException {
        return this.mScheduler.read( this, position, dst );
    }

    @Override
    public int write( long position, ByteBuffer src ) throws IOException {
        return this.mScheduler.write( this, position, src );
    }

    @Override
    public void flush() throws IOException {
        Set<GUID> flushed = new HashSet<>();
        for ( VolumeExtent extent : this.mMembers ) {
            GUID childGuid = extent.getChildVolume().getGuid();
            if ( flushed.add( childGuid ) ) {
                extent.getChildVolume().flush();
            }
        }
    }

    public void setScheduler( StripedIoScheduler scheduler ) {
        if ( scheduler == null ) {
            throw new IllegalArgumentException( "Striped IO scheduler is null" );
        }
        this.mScheduler = scheduler;
    }
}
