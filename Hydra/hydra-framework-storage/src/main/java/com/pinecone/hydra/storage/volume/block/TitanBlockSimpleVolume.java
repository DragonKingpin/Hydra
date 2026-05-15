package com.pinecone.hydra.storage.volume.block;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.core.ArchVolume;
import com.pinecone.hydra.storage.volume.core.VolumeExtent;
import com.pinecone.hydra.storage.volume.core.VolumeStatus;
import com.pinecone.hydra.storage.volume.core.VolumeType;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TitanBlockSimpleVolume extends ArchVolume implements BlockSimpleVolume {
    protected VolumeExtent mBackingExtent;

    public TitanBlockSimpleVolume() {
        super();
        this.mVolumeType = VolumeType.SIMPLE;
    }

    public TitanBlockSimpleVolume( GUID guid, String name, VolumeExtent backingExtent ) {
        super( guid, name, VolumeType.SIMPLE );
        this.setBackingExtent( backingExtent );
        this.mStatus = VolumeStatus.READY;
    }

    @Override
    public VolumeExtent getBackingExtent() {
        return this.mBackingExtent;
    }

    @Override
    public void setBackingExtent( VolumeExtent backingExtent ) {
        if ( backingExtent == null ) {
            throw new IllegalArgumentException( "Block simple volume backing extent is null" );
        }
        if ( backingExtent.getPhysicalAccessor() == null ) {
            throw new IllegalArgumentException( "Block simple volume requires physical backing" );
        }
        this.mBackingExtent = backingExtent;
        this.mnLogicalSize = backingExtent.getLength();
    }

    @Override
    public int read( long position, ByteBuffer dst ) throws IOException {
        int length = this.trimLength( position, dst.remaining() );
        if ( length <= 0 ) {
            return 0;
        }
        ByteBuffer target = dst.slice();
        target.limit( length );
        long physicalPosition = this.mBackingExtent.getPhysicalOffset() + position;
        int read = this.mBackingExtent.getPhysicalAccessor().read( physicalPosition, target );
        dst.position( dst.position() + read );
        return read;
    }

    @Override
    public int write( long position, ByteBuffer src ) throws IOException {
        int length = this.trimLength( position, src.remaining() );
        if ( length <= 0 ) {
            return 0;
        }
        ByteBuffer source = src.slice();
        source.limit( length );
        long physicalPosition = this.mBackingExtent.getPhysicalOffset() + position;
        int written = this.mBackingExtent.getPhysicalAccessor().write( physicalPosition, source );
        src.position( src.position() + written );
        return written;
    }

    @Override
    public void flush() throws IOException {
        this.mBackingExtent.getPhysicalAccessor().flush();
    }
}
