package com.pinecone.hydra.storage.file.fat.io;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface FatChunkStore extends Pinenut {
    int write( FileChunkLocation location, ByteBuffer source ) throws IOException;

    int write( FileChunkLocation location, long offsetInChunk, ByteBuffer source ) throws IOException;

    long read( FileChunkLocation location, long offsetInChunk, long length, OutputStream outputStream ) throws IOException;

    void delete( FileChunkLocation location ) throws IOException;
}
