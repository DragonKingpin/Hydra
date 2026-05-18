package com.pinecone.hydra.storage.file.fat.service;

import com.pinecone.framework.system.prototype.Pinenut;

public class ChunkRange implements Pinenut {
    protected long mnChunkIndex;
    protected long mnLogicalOffset;
    protected long mnChunkSize;
    protected long mnValidSize;

    public ChunkRange( long chunkIndex, long logicalOffset, long chunkSize, long validSize ) {
        this.mnChunkIndex    = chunkIndex;
        this.mnLogicalOffset = logicalOffset;
        this.mnChunkSize     = chunkSize;
        this.mnValidSize     = validSize;
    }

    public long getChunkIndex() {
        return this.mnChunkIndex;
    }

    public long getLogicalOffset() {
        return this.mnLogicalOffset;
    }

    public long getChunkSize() {
        return this.mnChunkSize;
    }

    public long getValidSize() {
        return this.mnValidSize;
    }
}
