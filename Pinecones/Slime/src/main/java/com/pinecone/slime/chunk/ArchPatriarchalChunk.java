package com.pinecone.slime.chunk;

public abstract class ArchPatriarchalChunk implements PatriarchalChunk {
    protected ArchPatriarchalChunk mParent;

    protected ArchPatriarchalChunk(){

    }

    protected ArchPatriarchalChunk( ArchPatriarchalChunk parent ) {
        this.mParent = parent;
    }

    @Override
    public PatriarchalChunk parent() {
        return this.mParent;
    }


    @Override
    public void setParent( PatriarchalChunk parent ){
        this.mParent = (ArchPatriarchalChunk) parent;
    }

}
