package com.pinecone.hydra.storage.file.fat;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;
import com.pinecone.hydra.storage.file.fat.entity.GenericFileChunk;
import com.pinecone.hydra.storage.file.fat.entity.GenericFileChunkLocation;
import com.pinecone.hydra.storage.file.fat.policy.ChunkSizingPolicy;
import com.pinecone.hydra.storage.file.fat.policy.TieredChunkSizingPolicy;
import com.pinecone.hydra.storage.file.fat.service.ChunkRange;
import com.pinecone.hydra.storage.file.fat.service.ChunkSlice;
import com.pinecone.hydra.storage.file.fat.source.FileChunkLocationManipulator;
import com.pinecone.hydra.storage.file.fat.source.FileChunkManipulator;

import java.util.ArrayList;
import java.util.List;

public class TitanFatChunkInstrument implements FatChunkInstrument {
    protected FileChunkManipulator         mChunkManipulator;
    protected FileChunkLocationManipulator mLocationManipulator;
    protected ChunkSizingPolicy            mSizingPolicy;
    protected GuidAllocator                mGuidAllocator;

    public TitanFatChunkInstrument(
            FileChunkManipulator chunkManipulator,
            FileChunkLocationManipulator locationManipulator,
            GuidAllocator guidAllocator
    ) {
        this( chunkManipulator, locationManipulator, guidAllocator, new TieredChunkSizingPolicy() );
    }

    public TitanFatChunkInstrument(
            FileChunkManipulator chunkManipulator,
            FileChunkLocationManipulator locationManipulator,
            GuidAllocator guidAllocator,
            ChunkSizingPolicy sizingPolicy
    ) {
        this.mChunkManipulator    = chunkManipulator;
        this.mLocationManipulator = locationManipulator;
        this.mGuidAllocator       = guidAllocator;
        this.mSizingPolicy        = sizingPolicy;
    }

    @Override
    public long chooseChunkSize( long fileSize ) {
        return this.mSizingPolicy.chooseChunkSize( fileSize );
    }

    @Override
    public List<ChunkRange> planChunks( long fileSize ) {
        List<ChunkRange> ranges = new ArrayList<>();
        long chunkSize = this.chooseChunkSize( fileSize );
        long position = 0L;
        long chunkIndex = 0L;
        if ( fileSize == 0L ) {
            ranges.add( new ChunkRange( 0L, 0L, chunkSize, 0L ) );
            return ranges;
        }
        while ( position < fileSize ) {
            long validSize = Math.min( chunkSize, fileSize - position );
            ranges.add( new ChunkRange( chunkIndex, position, chunkSize, validSize ) );
            position += validSize;
            ++chunkIndex;
        }
        return ranges;
    }

    @Override
    public FileChunk newChunk( GUID fileGuid, ChunkRange range ) {
        return new GenericFileChunk(
                this.mGuidAllocator.nextGUID(),
                fileGuid,
                range.getChunkIndex(),
                range.getLogicalOffset(),
                range.getChunkSize(),
                range.getValidSize()
        );
    }

    @Override
    public FileChunkLocation newLocation( GUID chunkGuid, GUID volumeGuid, long volumeOffset, long lengthBytes ) {
        return new GenericFileChunkLocation(
                this.mGuidAllocator.nextGUID(),
                chunkGuid,
                volumeGuid,
                volumeOffset,
                lengthBytes
        );
    }

    @Override
    public void saveChunk( FileChunk chunk ) {
        FileChunk oldChunk = this.mChunkManipulator.get( chunk.getGuid() );
        if ( oldChunk == null ) {
            this.mChunkManipulator.insert( chunk );
        }
        else {
            this.mChunkManipulator.update( chunk );
        }
    }

    @Override
    public void saveLocation( FileChunkLocation location ) {
        FileChunkLocation oldLocation = this.mLocationManipulator.get( location.getGuid() );
        if ( oldLocation == null ) {
            this.mLocationManipulator.insert( location );
        }
        else {
            this.mLocationManipulator.update( location );
        }
    }

    @Override
    public List<FileChunk> fetchChunks( GUID fileGuid ) {
        return new ArrayList<>( this.mChunkManipulator.listByFileGuid( fileGuid ) );
    }

    @Override
    public long countChunks() {
        return this.mChunkManipulator.countAll();
    }

    @Override
    public List<FileChunk> fetchChunkPage( int offset, int limit ) {
        return new ArrayList<>( this.mChunkManipulator.listPage( offset, limit ) );
    }

    @Override
    public List<ChunkSlice> fetchSlices( GUID fileGuid, long offset, long length ) {
        long endOffset = offset + length;
        List<? extends FileChunk> chunks = this.mChunkManipulator.listByFileRange( fileGuid, offset, endOffset );
        List<ChunkSlice> slices = new ArrayList<>();
        for ( FileChunk chunk : chunks ) {
            FileChunkLocation location = this.getReadyLocation( chunk.getGuid() );
            if ( location == null ) {
                throw new IllegalStateException( "Chunk has no READY location: " + chunk.getGuid() );
            }
            long chunkStart = chunk.getLogicalOffset();
            long chunkEnd = chunk.getLogicalOffset() + chunk.getValidSize();
            long sliceStart = Math.max( offset, chunkStart );
            long sliceEnd = Math.min( endOffset, chunkEnd );
            if ( sliceStart < sliceEnd ) {
                slices.add( new ChunkSlice( chunk, location, sliceStart - chunkStart, sliceEnd - sliceStart ) );
            }
        }
        return slices;
    }

    @Override
    public FileChunkLocation getReadyLocation( GUID chunkGuid ) {
        return this.mLocationManipulator.getReadyLocation( chunkGuid );
    }

    @Override
    public List<FileChunkLocation> fetchLocations( GUID chunkGuid ) {
        return new ArrayList<>( this.mLocationManipulator.listByChunkGuid( chunkGuid ) );
    }

    @Override
    public long countLocations() {
        return this.mLocationManipulator.countAll();
    }

    @Override
    public List<FileChunkLocation> fetchLocationPage( int offset, int limit ) {
        return new ArrayList<>( this.mLocationManipulator.listPage( offset, limit ) );
    }

    @Override
    public boolean existsChunk( GUID chunkGuid ) {
        return this.mChunkManipulator.get( chunkGuid ) != null;
    }

    @Override
    public boolean existsLocation( GUID locationGuid ) {
        return this.mLocationManipulator.get( locationGuid ) != null;
    }

    @Override
    public void deleteChunk( GUID chunkGuid ) {
        this.mLocationManipulator.removeByChunkGuid( chunkGuid );
        this.mChunkManipulator.remove( chunkGuid );
    }

    @Override
    public void deleteLocation( GUID locationGuid ) {
        this.mLocationManipulator.remove( locationGuid );
    }

    @Override
    public void deleteFileChunks( GUID fileGuid ) {
        this.mLocationManipulator.removeByFileGuid( fileGuid );
        this.mChunkManipulator.removeByFileGuid( fileGuid );
    }
}
