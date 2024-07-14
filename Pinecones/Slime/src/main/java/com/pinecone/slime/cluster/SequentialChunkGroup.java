package com.pinecone.slime.cluster;

import com.pinecone.slime.chunk.Chunk;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface SequentialChunkGroup extends ChunkGroup {
    void add( Chunk that );

    List<Chunk > getChunksById( long id );

    Chunk getFirstChunkById( long id );

    void remove( Chunk that );

    void remove( long id );

    Collection<Chunk > getSequentialChunks();

    Iterator<Chunk > begin();
}
