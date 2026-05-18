package com.pinecone.hydra.storage.file.fat.service;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;

public class ChunkSlice implements Pinenut {
    protected FileChunk         mChunk;
    protected FileChunkLocation mLocation;
    protected long              mnChunkOffset;
    protected long              mnLength;

    public ChunkSlice( FileChunk chunk, FileChunkLocation location, long chunkOffset, long length ) {
        this.mChunk        = chunk;
        this.mLocation     = location;
        this.mnChunkOffset = chunkOffset;
        this.mnLength      = length;
    }

    public FileChunk getChunk() {
        return this.mChunk;
    }

    public FileChunkLocation getLocation() {
        return this.mLocation;
    }

    public long getChunkOffset() {
        return this.mnChunkOffset;
    }

    public long getLength() {
        return this.mnLength;
    }
}
