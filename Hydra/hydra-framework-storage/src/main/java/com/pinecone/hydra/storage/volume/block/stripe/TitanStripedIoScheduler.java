package com.pinecone.hydra.storage.volume.block.stripe;

import com.pinecone.hydra.storage.volume.block.BlockVolume;
import com.pinecone.hydra.storage.volume.block.StripedVolume;
import com.pinecone.hydra.storage.volume.core.VolumeExtent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class TitanStripedIoScheduler implements StripedIoScheduler {
    protected final VolumeIoExecutor mExecutor;

    public TitanStripedIoScheduler() {
        this( new TitanVolumeIoExecutor() );
    }

    public TitanStripedIoScheduler( VolumeIoExecutor executor ) {
        if ( executor == null ) {
            throw new IllegalArgumentException( "Volume IO executor is null" );
        }
        this.mExecutor = executor;
    }

    @Override
    public int read( StripedVolume volume, long position, ByteBuffer dst ) throws IOException {
        return this.transfer( volume, position, dst, false );
    }

    @Override
    public int write( StripedVolume volume, long position, ByteBuffer src ) throws IOException {
        return this.transfer( volume, position, src, true );
    }

    protected int transfer( StripedVolume volume, long position, ByteBuffer buffer, boolean write ) throws IOException {
        StripedIoPlan plan = this.plan( volume, position, buffer );
        if ( plan.getTotalLength() <= 0 ) {
            return 0;
        }
        return this.mExecutor.execute( plan, buffer, write ).getTransferredBytes();
    }

    protected StripedIoPlan plan( StripedVolume volume, long position, ByteBuffer buffer ) {
        if ( position < 0L ) {
            throw new IllegalArgumentException( "Negative striped volume position: " + position );
        }
        if ( volume.getMembers().isEmpty() ) {
            return new StripedIoPlan( position, buffer.position(), 0, 1, new ArrayList<>() );
        }
        int requestedLength = this.trimLength( volume, position, buffer.remaining() );
        List<StripeSlice> slices = new ArrayList<>();
        int planned = 0;
        int sequenceNo = 0;
        while ( planned < requestedLength ) {
            long cursor = position + planned;
            StripeAddress address = this.map( volume, cursor );
            int sliceLength = (int)Math.min(
                    requestedLength - planned,
                    volume.getStripeUnit() - address.mnOffsetInStripe
            );
            sliceLength = (int)Math.min(
                    sliceLength,
                    address.mExtent.getLength() - address.mnMemberOffset
            );
            if ( sliceLength <= 0 ) {
                break;
            }
            BlockVolume child = (BlockVolume)address.mExtent.getChildVolume();
            long childPosition = address.mExtent.getChildOffset() + address.mnMemberOffset;
            slices.add( new StripeSlice(
                    child,
                    address.mnMemberIndex,
                    childPosition,
                    buffer.position() + planned,
                    sliceLength,
                    sequenceNo++
            ) );
            planned += sliceLength;
        }
        return new StripedIoPlan( position, buffer.position(), planned, volume.getMembers().size(), slices );
    }

    protected int trimLength( StripedVolume volume, long position, int requestedLength ) {
        if ( position >= volume.getLogicalSize() ) {
            return 0;
        }
        long availableLength = volume.getLogicalSize() - position;
        return (int)Math.min( requestedLength, availableLength );
    }

    protected StripeAddress map( StripedVolume volume, long position ) {
        long stripeNo = position / volume.getStripeUnit();
        int memberIndex = (int)( stripeNo % volume.getMembers().size() );
        long memberStripeNo = stripeNo / volume.getMembers().size();
        long offsetInStripe = position % volume.getStripeUnit();
        long memberOffset = memberStripeNo * volume.getStripeUnit() + offsetInStripe;
        return new StripeAddress(
                volume.getMembers().get( memberIndex ),
                memberIndex,
                memberOffset,
                offsetInStripe
        );
    }

    protected static class StripeAddress {
        protected final VolumeExtent mExtent;
        protected final int          mnMemberIndex;
        protected final long         mnMemberOffset;
        protected final long         mnOffsetInStripe;

        protected StripeAddress( VolumeExtent extent, int memberIndex, long memberOffset, long offsetInStripe ) {
            this.mExtent = extent;
            this.mnMemberIndex = memberIndex;
            this.mnMemberOffset = memberOffset;
            this.mnOffsetInStripe = offsetInStripe;
        }
    }
}
