package com.pinecone.hydra.storage.file.fat;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;
import com.pinecone.hydra.storage.file.fat.service.ChunkRange;
import com.pinecone.hydra.storage.file.fat.service.ChunkSlice;

import java.util.List;

public interface FatChunkInstrument extends Pinenut {
    long chooseChunkSize( long fileSize );

    List<ChunkRange> planChunks( long fileSize );

    FileChunk newChunk( GUID fileGuid, ChunkRange range );

    FileChunkLocation newLocation( GUID chunkGuid, GUID volumeGuid, long volumeOffset, long lengthBytes );

    void saveChunk( FileChunk chunk );

    void saveLocation( FileChunkLocation location );

    List<FileChunk> fetchChunks( GUID fileGuid );

    List<ChunkSlice> fetchSlices( GUID fileGuid, long offset, long length );

    FileChunkLocation getReadyLocation( GUID chunkGuid );

    List<FileChunkLocation> fetchLocations( GUID chunkGuid );

    boolean existsChunk( GUID chunkGuid );

    boolean existsLocation( GUID locationGuid );

    void deleteChunk( GUID chunkGuid );

    void deleteLocation( GUID locationGuid );

    void deleteFileChunks( GUID fileGuid );
}
