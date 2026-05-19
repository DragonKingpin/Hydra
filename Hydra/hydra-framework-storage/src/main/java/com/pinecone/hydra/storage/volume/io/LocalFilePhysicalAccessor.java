package com.pinecone.hydra.storage.volume.io;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.volume.config.TitanVolumeDefaults;
import com.pinecone.hydra.storage.volume.core.VolumeAllocationMode;
import com.pinecone.hydra.storage.volume.core.VolumePhysicalType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class LocalFilePhysicalAccessor implements PhysicalAccessor {
    protected GUID        mGuid;
    protected String      mszName;
    protected Path        mPath;
    protected long        mnCapacity;
    protected VolumeAllocationMode mAllocationMode;
    protected long        mnAllocationUnit;
    protected FileChannel mChannel;

    public LocalFilePhysicalAccessor( GUID guid, String name, Path path, long capacity ) throws IOException {
        this( guid, name, path, capacity, VolumeAllocationMode.THICK, TitanVolumeDefaults.DefaultAllocationUnit );
    }

    public LocalFilePhysicalAccessor(
            GUID guid,
            String name,
            Path path,
            long capacity,
            VolumeAllocationMode allocationMode,
            long allocationUnit
    ) throws IOException {
        if ( capacity < 0 ) {
            throw new IllegalArgumentException( "Negative physical capacity: " + capacity );
        }
        this.mGuid       = guid;
        this.mszName     = name;
        this.mPath       = path;
        this.mnCapacity  = capacity;
        this.mAllocationMode = allocationMode == null ? VolumeAllocationMode.THIN : allocationMode;
        this.mnAllocationUnit = allocationUnit <= 0L ? TitanVolumeDefaults.DefaultAllocationUnit : allocationUnit;
        this.mChannel    = this.openChannel( path );
        if ( this.mAllocationMode == VolumeAllocationMode.THICK ) {
            this.ensureCapacity( this.mnCapacity );
        }
    }

    protected FileChannel openChannel( Path path ) throws IOException {
        Path parent = path.getParent();
        if ( parent != null ) {
            Files.createDirectories( parent );
        }
        return FileChannel.open(
                path,
                StandardOpenOption.CREATE,
                StandardOpenOption.READ,
                StandardOpenOption.WRITE
        );
    }

    protected synchronized void ensureCommitted( long requiredEnd ) throws IOException {
        if ( requiredEnd <= 0L ) {
            return;
        }
        if ( requiredEnd > this.mnCapacity ) {
            requiredEnd = this.mnCapacity;
        }
        if ( this.mAllocationMode == VolumeAllocationMode.THICK ) {
            this.ensureCapacity( this.mnCapacity );
            return;
        }
        long committed = this.mChannel.size();
        if ( requiredEnd <= committed ) {
            return;
        }
        this.ensureCapacity( Math.min( this.alignUp( requiredEnd, this.mnAllocationUnit ), this.mnCapacity ) );
    }

    protected void ensureCapacity( long targetSize ) throws IOException {
        if ( targetSize <= 0L ) {
            return;
        }
        if ( this.mChannel.size() >= targetSize ) {
            return;
        }
        ByteBuffer zero = ByteBuffer.allocate( 1 );
        while ( zero.hasRemaining() ) {
            this.mChannel.write( zero, targetSize - 1 );
        }
    }

    protected long alignUp( long value, long unit ) {
        long remainder = value % unit;
        return remainder == 0L ? value : value + unit - remainder;
    }

    @Override
    public GUID getGuid() {
        return this.mGuid;
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    public Path getPath() {
        return this.mPath;
    }

    @Override
    public VolumePhysicalType getPhysicalType() {
        return VolumePhysicalType.BLOCK;
    }

    @Override
    public long getCapacity() {
        return this.mnCapacity;
    }

    @Override
    public long getCommittedBytes() throws IOException {
        return this.mChannel.size();
    }

    @Override
    public int read( long position, ByteBuffer dst ) throws IOException {
        int length = this.trimLength( position, dst.remaining() );
        if ( length <= 0 ) {
            return 0;
        }
        ByteBuffer target = dst.slice();
        target.limit( length );
        int total = 0;
        while ( target.hasRemaining() ) {
            int read = this.mChannel.read( target, position + total );
            if ( read < 0 ) {
                this.fillZero( target );
                break;
            }
            if ( read == 0 ) {
                break;
            }
            total += read;
        }
        dst.position( dst.position() + target.position() );
        return target.position();
    }

    @Override
    public int write( long position, ByteBuffer src ) throws IOException {
        int length = this.trimLength( position, src.remaining() );
        if ( length <= 0 ) {
            return 0;
        }
        this.ensureCommitted( position + length );
        ByteBuffer source = src.slice();
        source.limit( length );
        int total = 0;
        while ( source.hasRemaining() ) {
            int written = this.mChannel.write( source, position + total );
            if ( written == 0 ) {
                break;
            }
            total += written;
        }
        src.position( src.position() + total );
        return total;
    }

    protected int trimLength( long position, int requestedLength ) {
        if ( position < 0 ) {
            throw new IllegalArgumentException( "Negative physical position: " + position );
        }
        if ( position >= this.mnCapacity ) {
            return 0;
        }
        long availableLength = this.mnCapacity - position;
        return (int)Math.min( requestedLength, availableLength );
    }

    protected void fillZero( ByteBuffer target ) {
        while ( target.hasRemaining() ) {
            target.put( (byte)0 );
        }
    }

    @Override
    public void flush() throws IOException {
        this.mChannel.force( true );
    }

    @Override
    public void close() throws IOException {
        this.mChannel.close();
    }
}

