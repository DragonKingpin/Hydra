package com.pinecone.slime.chunk;

public interface PatriarchalChunk extends Chunk {
    PatriarchalChunk parent();

    default PatriarchalChunk root() {
        PatriarchalChunk p = this.parent();
        if( p == null ) {
            return this;
        }

        return p.root();
    }

    void setParent( PatriarchalChunk parent );


}
