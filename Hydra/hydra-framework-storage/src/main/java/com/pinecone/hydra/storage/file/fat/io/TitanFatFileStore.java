package com.pinecone.hydra.storage.file.fat.io;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.fat.FatChunkInstrument;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocationType;
import com.pinecone.hydra.storage.file.fat.service.ChunkRange;
import com.pinecone.hydra.storage.file.fat.service.ChunkSlice;
import com.pinecone.hydra.storage.file.journal.Journal;
import com.pinecone.hydra.storage.file.journal.JournalInstrument;
import com.pinecone.hydra.storage.file.journal.JournalItem;
import com.pinecone.hydra.storage.file.journal.JournalItemBuilder;
import com.pinecone.hydra.storage.file.journal.JournalItemType;
import com.pinecone.hydra.storage.file.journal.JournalPayloads;
import com.pinecone.hydra.storage.file.journal.JournalScope;
import com.pinecone.hydra.storage.file.journal.JournalType;
import com.pinecone.hydra.storage.volume.VolumeManager;
import com.pinecone.hydra.storage.volume.KernelVolumeConfig;
import com.pinecone.hydra.storage.volume.VolumeConfig;
import com.pinecone.hydra.storage.volume.core.Volume;
import com.pinecone.hydra.storage.volume.core.VolumeMappingMode;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.zip.CRC32;

public class TitanFatFileStore implements FatFileStore {
    protected final KOMFileSystem       mFileSystem;
    protected final FatChunkInstrument  mFatChunkInstrument;
    protected final FatChunkStore       mFatChunkStore;
    protected final VolumeSpaceAllocator mVolumeSpaceAllocator;
    protected final VolumeManager       mVolumeManager;
    protected final GUID                mDefaultVolumeGuid;
    protected final JournalInstrument   mJournalInstrument;
    protected final JournalType         mWriteJournalType;

    public TitanFatFileStore(
            KOMFileSystem fileSystem,
            FatChunkInstrument fatChunkInstrument,
            FatChunkStore fatChunkStore,
            VolumeSpaceAllocator volumeSpaceAllocator,
            VolumeManager volumeManager,
            GUID defaultVolumeGuid,
            JournalInstrument journalInstrument,
            JournalType writeJournalType
    ) {
        this.mFileSystem            = fileSystem;
        this.mFatChunkInstrument    = fatChunkInstrument;
        this.mFatChunkStore         = fatChunkStore;
        this.mVolumeSpaceAllocator  = volumeSpaceAllocator;
        this.mVolumeManager         = volumeManager;
        this.mDefaultVolumeGuid     = defaultVolumeGuid;
        this.mJournalInstrument     = journalInstrument;
        this.mWriteJournalType      = writeJournalType == null ? JournalType.OVERWRITE : writeJournalType;
    }

    public TitanFatFileStore(
            KOMFileSystem fileSystem,
            FatChunkInstrument fatChunkInstrument,
            FatChunkStore fatChunkStore,
            VolumeSpaceAllocator volumeSpaceAllocator,
            VolumeManager volumeManager,
            GUID defaultVolumeGuid,
            JournalInstrument journalInstrument
    ) {
        this( fileSystem, fatChunkInstrument, fatChunkStore, volumeSpaceAllocator, volumeManager, defaultVolumeGuid, journalInstrument, JournalType.OVERWRITE );
    }

    public TitanFatFileStore(
            KOMFileSystem fileSystem,
            FatChunkInstrument fatChunkInstrument,
            FatChunkStore fatChunkStore,
            VolumeSpaceAllocator volumeSpaceAllocator,
            GUID defaultVolumeGuid
    ) {
        this(
                fileSystem,
                fatChunkInstrument,
                fatChunkStore,
                volumeSpaceAllocator,
                fatChunkStore instanceof TitanFatChunkStore
                        ? ( (TitanFatChunkStore) fatChunkStore ).mVolumeManager
                        : null,
                defaultVolumeGuid,
                null,
                JournalType.OVERWRITE
        );
    }

    @Override
    public long write( FileNode fileNode, InputStream inputStream, long size ) throws IOException {
        return this.withJournal( this.mWriteJournalType, fileNode, scope -> this.write0( scope, fileNode, inputStream, size ) );
    }

    protected long write0( JournalScope journalScope, FileNode fileNode, InputStream inputStream, long size ) throws IOException {
        this.delete0( journalScope, fileNode );
        long checksum = 0L;
        long crc32Xor = 0L;
        long written = 0L;
        for ( ChunkRange range : this.mFatChunkInstrument.planChunks( size ) ) {
            byte[] bytes = this.readFully( inputStream, range.getValidSize() );
            ChunkCheck check = this.check( bytes );
            FileChunk chunk = this.mFatChunkInstrument.newChunk( fileNode.getGuid(), range );
            chunk.setChecksum( check.getChecksum() );
            chunk.setCrc32( check.getCrc32() );
            FileChunkLocation location = this.newLocation( fileNode, chunk, range.getValidSize() );
            JournalItem dataItem = this.recordDataItem( journalScope, location );
            this.mFatChunkStore.write( location, ByteBuffer.wrap( bytes ) );
            journalScope.markApplied( dataItem );
            JournalItem chunkItem = this.recordChunkItem( journalScope, chunk );
            this.mFatChunkInstrument.saveChunk( chunk );
            journalScope.markApplied( chunkItem );
            JournalItem locationItem = this.recordLocationItem( journalScope, location );
            this.mFatChunkInstrument.saveLocation( location );
            journalScope.markApplied( locationItem );
            checksum += check.getChecksum();
            crc32Xor = written == 0L ? check.getCrc32() : ( crc32Xor ^ check.getCrc32() );
            written += bytes.length;
        }
        this.updateFileSize( fileNode, written, checksum, crc32Xor );
        return written;
    }

    @Override
    public int write( FileNode fileNode, ByteBuffer src ) throws IOException {
        byte[] bytes = this.toBytes( src );
        return (int) this.write( fileNode, new ByteArrayInputStream( bytes ), bytes.length );
    }

    @Override
    public long append( FileNode fileNode, InputStream inputStream, long size ) throws IOException {
        return this.withJournal( JournalType.APPEND, fileNode, scope -> this.append0( scope, fileNode, inputStream, size ) );
    }

    protected long append0( JournalScope journalScope, FileNode fileNode, InputStream inputStream, long size ) throws IOException {
        long currentSize = fileNode.getDefinitionSize();
        if ( currentSize == 0L ) {
            return this.write0( journalScope, fileNode, inputStream, size );
        }
        List<FileChunk> chunks = this.mFatChunkInstrument.fetchChunks( fileNode.getGuid() );
        long nextIndex = chunks.isEmpty() ? 0L : chunks.get( chunks.size() - 1 ).getChunkIndex() + 1L;
        long chunkSize = chunks.isEmpty()
                ? this.mFatChunkInstrument.chooseChunkSize( currentSize + size )
                : chunks.get( chunks.size() - 1 ).getChunkSize();
        long remaining = size;
        long logicalOffset = currentSize;
        long appended = 0L;
        if ( !chunks.isEmpty() ) {
            FileChunk lastChunk = chunks.get( chunks.size() - 1 );
            long freeInLastChunk = lastChunk.getChunkSize() - lastChunk.getValidSize();
            if ( freeInLastChunk > 0L && remaining > 0L ) {
                long fillSize = Math.min( freeInLastChunk, remaining );
                FileChunkLocation location = this.mFatChunkInstrument.getReadyLocation( lastChunk.getGuid() );
                if ( location == null ) {
                    throw new IllegalStateException( "Last chunk has no READY location: " + lastChunk.getGuid() );
                }
                byte[] bytes = this.readFully( inputStream, fillSize );
                long oldValidSize = lastChunk.getValidSize();
                String oldChunkPayload = JournalPayloads.chunk( lastChunk );
                String oldLocationPayload = JournalPayloads.location( location );
                JournalItem dataItem = this.recordDataItem( journalScope, location );
                this.mFatChunkStore.write( location, oldValidSize, ByteBuffer.wrap( bytes ) );
                journalScope.markApplied( dataItem );
                lastChunk.setValidSize( oldValidSize + bytes.length );
                location.setLengthBytes( location.getLengthBytes() + bytes.length );
                ChunkCheck check = this.checkChunk( location, lastChunk.getValidSize() );
                lastChunk.setChecksum( check.getChecksum() );
                lastChunk.setCrc32( check.getCrc32() );
                JournalItem chunkItem = this.recordChunkItem( journalScope, lastChunk, oldChunkPayload );
                this.mFatChunkInstrument.saveChunk( lastChunk );
                journalScope.markApplied( chunkItem );
                JournalItem locationItem = this.recordLocationItem( journalScope, location, oldLocationPayload );
                this.mFatChunkInstrument.saveLocation( location );
                journalScope.markApplied( locationItem );
                logicalOffset += bytes.length;
                appended += bytes.length;
                remaining -= bytes.length;
            }
        }
        while ( remaining > 0L ) {
            long validSize = Math.min( chunkSize, remaining );
            byte[] bytes = this.readFully( inputStream, validSize );
            ChunkCheck check = this.check( bytes );
            ChunkRange range = new ChunkRange( nextIndex, logicalOffset, chunkSize, validSize );
            FileChunk chunk = this.mFatChunkInstrument.newChunk( fileNode.getGuid(), range );
            chunk.setChecksum( check.getChecksum() );
            chunk.setCrc32( check.getCrc32() );
            FileChunkLocation location = this.newLocation( fileNode, chunk, validSize );
            JournalItem dataItem = this.recordDataItem( journalScope, location );
            this.mFatChunkStore.write( location, ByteBuffer.wrap( bytes ) );
            journalScope.markApplied( dataItem );
            JournalItem chunkItem = this.recordChunkItem( journalScope, chunk );
            this.mFatChunkInstrument.saveChunk( chunk );
            journalScope.markApplied( chunkItem );
            JournalItem locationItem = this.recordLocationItem( journalScope, location );
            this.mFatChunkInstrument.saveLocation( location );
            journalScope.markApplied( locationItem );
            logicalOffset += validSize;
            appended += validSize;
            remaining -= validSize;
            ++nextIndex;
        }
        ChunkCheck fileCheck = this.checkFileChunks( fileNode.getGuid() );
        this.updateFileSize( fileNode, currentSize + appended, fileCheck.getChecksum(), fileCheck.getCrc32() );
        return appended;
    }

    @Override
    public int append( FileNode fileNode, ByteBuffer src ) throws IOException {
        byte[] bytes = this.toBytes( src );
        return (int) this.append( fileNode, new ByteArrayInputStream( bytes ), bytes.length );
    }

    @Override
    public long read( FileNode fileNode, OutputStream outputStream ) throws IOException {
        return this.read( fileNode, 0L, fileNode.getDefinitionSize(), outputStream );
    }

    @Override
    public long read( FileNode fileNode, long position, long length, OutputStream outputStream ) throws IOException {
        if ( position < 0L || length < 0L ) {
            throw new IllegalArgumentException( "Illegal read range: " + position + ", " + length );
        }
        long readable = Math.min( length, Math.max( 0L, fileNode.getDefinitionSize() - position ) );
        List<ChunkSlice> slices = this.mFatChunkInstrument.fetchSlices( fileNode.getGuid(), position, readable );
        long copied = 0L;
        for ( ChunkSlice slice : slices ) {
            copied += this.mFatChunkStore.read(
                    slice.getLocation(),
                    slice.getChunkOffset(),
                    slice.getLength(),
                    outputStream
            );
        }
        return copied;
    }

    @Override
    public int read( FileNode fileNode, long position, ByteBuffer dst ) throws IOException {
        int length = dst.remaining();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( length );
        long read = this.read( fileNode, position, length, outputStream );
        byte[] bytes = outputStream.toByteArray();
        dst.put( bytes, 0, (int) read );
        return (int) read;
    }

    @Override
    public void flush( FileNode fileNode ) throws IOException {
        if ( this.mVolumeManager != null ) {
            this.mVolumeManager.flush( this.mDefaultVolumeGuid );
        }
    }

    @Override
    public void commit( FileNode fileNode ) throws IOException {
        this.flush( fileNode );
    }

    @Override
    public void abort( FileNode fileNode ) throws IOException {
        this.delete( fileNode );
    }

    @Override
    public void delete( FileNode fileNode ) throws IOException {
        this.withJournal( JournalType.DELETE, fileNode, scope -> {
            this.delete0( scope, fileNode );
            return 0L;
        } );
    }

    protected void delete0( JournalScope journalScope, FileNode fileNode ) throws IOException {
        for ( FileChunk chunk : this.mFatChunkInstrument.fetchChunks( fileNode.getGuid() ) ) {
            for ( FileChunkLocation location : this.mFatChunkInstrument.fetchLocations( chunk.getGuid() ) ) {
                JournalItem cleanupItem = this.recordCleanupItem( journalScope, chunk, location );
                this.mFatChunkStore.delete( location );
                journalScope.markApplied( cleanupItem );
            }
        }
        this.mFatChunkInstrument.deleteFileChunks( fileNode.getGuid() );
    }

    protected long withJournal( JournalType type, FileNode fileNode, JournaledOperation operation ) throws IOException {
        if ( this.mJournalInstrument == null ) {
            return operation.run( JournalScope.open( null, type, fileNode ) );
        }
        JournalScope scope = JournalScope.open( this.mJournalInstrument, type, fileNode );
        String oldFilePayload = JournalPayloads.fileNode( fileNode );
        try {
            scope.markWriting();
            long result = operation.run( scope );
            scope.recordApplied( JournalItemBuilder.of( JournalItemType.FILE_NODE, fileNode.getGuid(), scope.nextOrdinal() )
                    .fileGuid( fileNode.getGuid() )
                    .oldPayload( oldFilePayload )
                    .newPayload( JournalPayloads.fileNode( fileNode ) ) );
            scope.commit();
            return result;
        }
        catch ( IOException | RuntimeException e ) {
            scope.fail( e );
            throw e;
        }
    }

    protected byte[] readFully( InputStream inputStream, long size ) throws IOException {
        if ( size > Integer.MAX_VALUE ) {
            throw new IOException( "Chunk is too large for first-round in-memory transfer: " + size );
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( (int) size );
        byte[] buffer = new byte[64 * 1024];
        long remaining = size;
        while ( remaining > 0L ) {
            int step = (int) Math.min( buffer.length, remaining );
            int n = inputStream.read( buffer, 0, step );
            if ( n < 0 ) {
                throw new EOFException( "Unexpected EOF while reading UOFS chunk" );
            }
            outputStream.write( buffer, 0, n );
            remaining -= n;
        }
        return outputStream.toByteArray();
    }

    protected ChunkCheck check( byte[] bytes ) {
        CRC32 crc32 = new CRC32();
        long checksum = 0L;
        for ( byte value : bytes ) {
            checksum += value & 0xFF;
            crc32.update( value );
        }
        return new ChunkCheck( crc32.getValue(), checksum );
    }

    protected byte[] toBytes( ByteBuffer source ) {
        ByteBuffer slice = source.slice();
        byte[] bytes = new byte[ slice.remaining() ];
        slice.get( bytes );
        source.position( source.position() + bytes.length );
        return bytes;
    }

    protected FileChunkLocation newLocation( FileNode fileNode, FileChunk chunk, long validSize ) throws IOException {
        if ( this.mVolumeManager == null ) {
            FileChunkLocation location = this.mFatChunkInstrument.newLocation(
                    chunk.getGuid(),
                    this.mDefaultVolumeGuid,
                    this.mVolumeSpaceAllocator.allocate( this.mDefaultVolumeGuid, validSize ),
                    validSize
            );
            location.setLocationType( FileChunkLocationType.VOLUME_BLOCK_EXTENT );
            return location;
        }
        Volume volume = this.mVolumeManager.loadVolume( this.mDefaultVolumeGuid );
        if ( volume == null ) {
            FileChunkLocation location = this.mFatChunkInstrument.newLocation(
                    chunk.getGuid(),
                    this.mDefaultVolumeGuid,
                    this.mVolumeSpaceAllocator.allocate( this.mDefaultVolumeGuid, validSize ),
                    validSize
            );
            location.setLocationType( FileChunkLocationType.VOLUME_BLOCK_EXTENT );
            return location;
        }
        if ( volume.getMappingMode() == VolumeMappingMode.VOLUME_DIRECT_OBJECT ) {
            FileChunkLocation location = this.mFatChunkInstrument.newLocation(
                    chunk.getGuid(),
                    this.mDefaultVolumeGuid,
                    0L,
                    validSize
            );
            location.setLocationType( FileChunkLocationType.VOLUME_DIRECT_OBJECT );
            location.setObjectKey( this.toObjectKey( fileNode, chunk ) );
            location.setObjectOffset( 0L );
            return location;
        }
        long volumeOffset = this.mVolumeSpaceAllocator.allocate( this.mDefaultVolumeGuid, validSize );
        FileChunkLocation location = this.mFatChunkInstrument.newLocation(
                chunk.getGuid(),
                this.mDefaultVolumeGuid,
                volumeOffset,
                validSize
        );
        location.setLocationType( FileChunkLocationType.VOLUME_BLOCK_EXTENT );
        return location;
    }

    protected String toObjectKey( FileNode fileNode, FileChunk chunk ) {
        VolumeConfig config = this.mVolumeManager == null
                ? new KernelVolumeConfig()
                : this.mVolumeManager.getConfig();
        return config.getTitanHomeDirectory()
                + "/"
                + config.getObjectDataDirectory()
                + "/"
                + fileNode.getGuid()
                + "/"
                + config.getChunkFilePrefix()
                + String.format( "%012d", chunk.getChunkIndex() )
                + config.getChunkFileExtension();
    }

    protected ChunkCheck checkChunk( FileChunkLocation location, long validSize ) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( (int) validSize );
        long readBytes = this.mFatChunkStore.read( location, 0L, validSize, outputStream );
        if ( readBytes != validSize ) {
            throw new EOFException( "Unexpected EOF while checking UOFS chunk" );
        }
        return this.check( outputStream.toByteArray() );
    }

    protected ChunkCheck checkFileChunks( GUID fileGuid ) {
        long checksum = 0L;
        long crc32Xor = 0L;
        boolean first = true;
        for ( FileChunk chunk : this.mFatChunkInstrument.fetchChunks( fileGuid ) ) {
            checksum += chunk.getChecksum() == null ? 0L : chunk.getChecksum();
            long crc32 = chunk.getCrc32() == null ? 0L : chunk.getCrc32();
            crc32Xor = first ? crc32 : ( crc32Xor ^ crc32 );
            first = false;
        }
        return new ChunkCheck( crc32Xor, checksum );
    }

    protected void updateFileSize( FileNode fileNode, long size, long checksum, long crc32Xor ) {
        fileNode.setDefinitionSize( size );
        fileNode.setLogicSize( size );
        fileNode.setPhysicalSize( size );
        fileNode.setChecksum( checksum );
        fileNode.setCrc32Xor( crc32Xor );
        this.mFileSystem.update( fileNode );
    }

    protected JournalItem recordChunkItem( JournalScope journalScope, FileChunk chunk ) {
        return this.recordChunkItem( journalScope, chunk, null );
    }

    protected JournalItem recordChunkItem( JournalScope journalScope, FileChunk chunk, String oldPayload ) {
        return journalScope.record( JournalItemBuilder.of( JournalItemType.CHUNK, chunk.getGuid(), journalScope.nextOrdinal() )
                .fileGuid( chunk.getFileGuid() )
                .chunkGuid( chunk.getGuid() )
                .oldPayload( oldPayload )
                .newPayload( JournalPayloads.chunk( chunk ) ) );
    }

    protected JournalItem recordLocationItem( JournalScope journalScope, FileChunkLocation location ) {
        return this.recordLocationItem( journalScope, location, null );
    }

    protected JournalItem recordLocationItem( JournalScope journalScope, FileChunkLocation location, String oldPayload ) {
        return journalScope.record( JournalItemBuilder.of( JournalItemType.LOCATION, location.getGuid(), journalScope.nextOrdinal() )
                .location( location )
                .oldPayload( oldPayload )
                .newPayload( JournalPayloads.location( location ) ) );
    }

    protected JournalItem recordDataItem( JournalScope journalScope, FileChunkLocation location ) {
        JournalItemType itemType = location.getLocationType() == FileChunkLocationType.VOLUME_DIRECT_OBJECT
                ? JournalItemType.OBJECT
                : JournalItemType.VOLUME_EXTENT;
        return journalScope.record( JournalItemBuilder.of( itemType, location.getGuid(), journalScope.nextOrdinal() )
                .location( location )
                .newPayload( JournalPayloads.location( location ) ) );
    }

    protected JournalItem recordCleanupItem( JournalScope journalScope, FileChunk chunk, FileChunkLocation location ) {
        return journalScope.record( JournalItemBuilder.of( JournalItemType.CLEANUP, location.getGuid(), journalScope.nextOrdinal() )
                .fileGuid( chunk.getFileGuid() )
                .chunkGuid( chunk.getGuid() )
                .location( location )
                .oldPayload( JournalPayloads.cleanup( chunk, location ) ) );
    }

    protected static class ChunkCheck {
        protected final long mCrc32;
        protected final long mChecksum;

        public ChunkCheck( long crc32, long checksum ) {
            this.mCrc32    = crc32;
            this.mChecksum = checksum;
        }

        public long getCrc32() {
            return this.mCrc32;
        }

        public long getChecksum() {
            return this.mChecksum;
        }
    }

    @FunctionalInterface
    protected interface JournaledOperation {
        long run( JournalScope journalScope ) throws IOException;
    }
}

