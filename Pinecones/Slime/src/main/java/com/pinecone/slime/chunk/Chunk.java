package com.pinecone.slime.chunk;

import com.pinecone.framework.system.prototype.Pinenut;

public interface Chunk extends Pinenut {
    long getId();

    void setId( long id );
}
