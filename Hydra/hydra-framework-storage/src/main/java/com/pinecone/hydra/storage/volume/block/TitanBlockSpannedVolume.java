package com.pinecone.hydra.storage.volume.block;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.ArchVolume;
import com.pinecone.hydra.storage.volume.core.VolumeExtent;
import com.pinecone.hydra.storage.volume.core.VolumeStatus;
import com.pinecone.hydra.storage.volume.core.VolumeType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TitanBlockSpannedVolume extends ArchVolume implements BlockSpannedVolume {
    protected final List<VolumeExtent> mExtents;

    public TitanBlockSpannedVolume() {
        super();
        this.mVolumeType = VolumeType.SPANNED;
        this.mExtents = new ArrayList<>();
    }

    public TitanBlockSpannedVolume( GUID guid, String name ) {
        this();
        this.mGuid = guid;
        this.mszName = name;
        this.mStatus = VolumeStatus.READY;
    }

    @Override
    public List<VolumeExtent> getExtents() {
        return this.mExtents;
    }

    @Override
    public void addExtent( VolumeExtent extent ) {
        if ( extent == null ) {
            throw new IllegalArgumentException( "Block spanned extent is null" );
        }
        if ( !( extent.getChildVolume() instanceof BlockVolume ) ) {
            throw new IllegalArgumentException( "Block spanned extent requires block child volume" );
        }
        extent.setParentOffset( this.mnLogicalSize );
        this.mExtents.add( extent );
        this.mnLogicalSize += extent.getLength();
    }

    @Override
    public int read( long position, ByteBuffer dst ) throws IOException {
        return this.transfer( position, dst, false );
    }

    @Override
    public int write( long position, ByteBuffer src ) throws IOException {
        return this.transfer( position, src, true );
    }

    protected int transfer( long position, ByteBuffer buffer, boolean write ) throws IOException {
        int requestedLength = this.trimLength( position, buffer.remaining() );
        if ( requestedLength <= 0 ) {
            return 0;
        }
        int total = 0;
        while ( total < requestedLength ) {
            long cursor = position + total;
            VolumeExtent extent = this.findExtent( cursor );
            long offsetInExtent = cursor - extent.getParentOffset();
            int sliceLength = (int)Math.min(
                    requestedLength - total,
                    extent.getLength() - offsetInExtent
            );
            ByteBuffer slice = buffer.slice();
            slice.limit( sliceLength );
            BlockVolume child = (BlockVolume) extent.getChildVolume();
            long childPosition = extent.getChildOffset() + offsetInExtent;
            int transferred = write
                    ? child.write( childPosition, slice )
                    : child.read( childPosition, slice );
            buffer.position( buffer.position() + transferred );
            total += transferred;
            if ( transferred < sliceLength ) {
                break;
            }
        }
        return total;
    }

    protected VolumeExtent findExtent( long position ) {
        for ( VolumeExtent extent : this.mExtents ) {
            long start = extent.getParentOffset();
            long end = start + extent.getLength();
            if ( position >= start && position < end ) {
                return extent;
            }
        }
        throw new IllegalArgumentException( "Block spanned position out of range: " + position );
    }

    @Override
    public void flush() throws IOException {
        Set<GUID> flushed = new HashSet<>();
        for ( VolumeExtent extent : this.mExtents ) {
            GUID childGuid = extent.getChildVolume().getGuid();
            if ( flushed.add( childGuid ) ) {
                extent.getChildVolume().flush();
            }
        }
    }
}
