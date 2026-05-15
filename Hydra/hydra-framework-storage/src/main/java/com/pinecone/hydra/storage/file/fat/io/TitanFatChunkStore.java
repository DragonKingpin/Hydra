package com.pinecone.hydra.storage.file.fat.io;

import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocationType;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.storage.volume.core.Volume;
import com.pinecone.hydra.storage.volume.core.VolumeMappingMode;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TitanFatChunkStore implements FatChunkStore {
    protected static final int DEFAULT_BUFFER_SIZE = 64 * 1024;

    protected final VolumeManager mVolumeManager;

    public TitanFatChunkStore( VolumeManager volumeManager ) {
        this.mVolumeManager = volumeManager;
    }

    @Override
    public int write( FileChunkLocation location, ByteBuffer source ) throws IOException {
        return this.write( location, 0L, source );
    }

    @Override
    public int write( FileChunkLocation location, long offsetInChunk, ByteBuffer source ) throws IOException {
        if ( this.resolveLocationType( location ) == FileChunkLocationType.VOLUME_DIRECT_OBJECT ) {
            int written = this.writeObject( location, offsetInChunk, source );
            this.mVolumeManager.refreshVolumeUsage( location.getVolumeGuid() );
            return written;
        }
        int written = 0;
        long position = location.getVolumeOffset() + offsetInChunk;
        while ( source.hasRemaining() ) {
            int n = this.mVolumeManager.write( location.getVolumeGuid(), position + written, source );
            if ( n <= 0 ) {
                throw new IOException( "Volume write made no progress: " + location.getVolumeGuid() );
            }
            written += n;
        }
        return written;
    }

    @Override
    public long read( FileChunkLocation location, long offsetInChunk, long length, OutputStream outputStream ) throws IOException {
        if ( this.resolveLocationType( location ) == FileChunkLocationType.VOLUME_DIRECT_OBJECT ) {
            return this.readObject( location, offsetInChunk, length, outputStream );
        }
        long remaining = length;
        long position = location.getVolumeOffset() + offsetInChunk;
        long copied = 0L;
        while ( remaining > 0L ) {
            int step = (int) Math.min( DEFAULT_BUFFER_SIZE, remaining );
            ByteBuffer buffer = ByteBuffer.allocate( step );
            int n = this.mVolumeManager.read( location.getVolumeGuid(), position + copied, buffer );
            if ( n <= 0 ) {
                break;
            }
            outputStream.write( buffer.array(), 0, n );
            copied += n;
            remaining -= n;
        }
        return copied;
    }

    @Override
    public void delete( FileChunkLocation location ) throws IOException {
        if ( this.resolveLocationType( location ) == FileChunkLocationType.VOLUME_DIRECT_OBJECT ) {
            Path path = this.resolveObjectPath( location );
            Files.deleteIfExists( path );
        }
    }

    protected FileChunkLocationType resolveLocationType( FileChunkLocation location ) throws IOException {
        if ( location.getLocationType() != null ) {
            return location.getLocationType();
        }
        Volume volume = this.mVolumeManager.loadVolume( location.getVolumeGuid() );
        if ( volume.getMappingMode() == VolumeMappingMode.VOLUME_DIRECT_OBJECT ) {
            return FileChunkLocationType.VOLUME_DIRECT_OBJECT;
        }
        return FileChunkLocationType.VOLUME_BLOCK_EXTENT;
    }

    protected int writeObject( FileChunkLocation location, long offsetInChunk, ByteBuffer source ) throws IOException {
        Path path = this.resolveObjectPath( location );
        Path parent = path.getParent();
        if ( parent != null ) {
            Files.createDirectories( parent );
        }
        int written = 0;
        long position = location.getObjectOffset() + offsetInChunk;
        try ( FileChannel channel = FileChannel.open(
                path,
                StandardOpenOption.CREATE,
                StandardOpenOption.READ,
                StandardOpenOption.WRITE
        ) ) {
            while ( source.hasRemaining() ) {
                int n = channel.write( source, position + written );
                if ( n <= 0 ) {
                    throw new IOException( "Object chunk write made no progress: " + path );
                }
                written += n;
            }
        }
        return written;
    }

    protected long readObject(
            FileChunkLocation location,
            long offsetInChunk,
            long length,
            OutputStream outputStream
    ) throws IOException {
        Path path = this.resolveObjectPath( location );
        long remaining = length;
        long copied = 0L;
        try ( FileChannel channel = FileChannel.open( path, StandardOpenOption.READ ) ) {
            long position = location.getObjectOffset() + offsetInChunk;
            while ( remaining > 0L ) {
                int step = (int) Math.min( DEFAULT_BUFFER_SIZE, remaining );
                ByteBuffer buffer = ByteBuffer.allocate( step );
                int n = channel.read( buffer, position + copied );
                if ( n <= 0 ) {
                    break;
                }
                outputStream.write( buffer.array(), 0, n );
                copied += n;
                remaining -= n;
            }
        }
        return copied;
    }

    protected Path resolveObjectPath( FileChunkLocation location ) throws IOException {
        if ( location.getObjectKey() == null || location.getObjectKey().isBlank() ) {
            throw new IOException( "Object chunk location has no object key: " + location.getGuid() );
        }
        Volume volume = this.mVolumeManager.loadVolume( location.getVolumeGuid() );
        if ( volume.getObjectRoot() == null || volume.getObjectRoot().isBlank() ) {
            throw new IOException( "Direct object volume has no object root: " + volume.getGuid() );
        }
        return Paths.get( volume.getObjectRoot() ).resolve( location.getObjectKey() );
    }
}

