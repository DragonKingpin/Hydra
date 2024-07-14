package com.pinecone.slime.cluster;

import com.pinecone.framework.unit.LinkedMultiValueMap;
import com.pinecone.framework.unit.MultiValueMap;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.slime.unitization.Precision;
import com.pinecone.slime.unitization.Precision64;
import com.pinecone.slime.chunk.Chunk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ArchSequentialChunkGroup implements SequentialChunkGroup {
    protected long                           mnId;
    protected List<Chunk >                   mChunkList;
    protected MultiValueMap<Long, Chunk >    mChunkRegister;

    protected ArchSequentialChunkGroup() {
        this.mChunkList     = new ArrayList<>();
        this.mChunkRegister = new LinkedMultiValueMap<> ();
    }

    @Override
    public long getId() {
        return this.mnId;
    }

    @Override
    public void setId( long id ) {
        this.mnId = id;
    }

    @Override
    public Precision size() {
        return new Precision64( this.getSequentialChunks().size() );
    }

    @Override
    public void add( Chunk that ) {
        this.mChunkList.add( that );
        this.mChunkRegister.add( that.getId(), that );
    }

    @Override
    public List<Chunk > getChunksById( long id ){
        return this.mChunkRegister.get( id );
    }

    @Override
    public Chunk getFirstChunkById( long id ){
        return this.mChunkRegister.getFirst( id );
    }

    @Override
    public void remove( Chunk that ) {
        List<Chunk > chunks = this.getChunksById( that.getId() );
        if( chunks.size() > 1 ) {
            chunks.remove( that );
        }
        else {
            this.mChunkRegister.remove( that.getId() );
        }

        this.mChunkList.remove( that );
    }

    @Override
    public void remove( long id ) {
        List<Chunk > chunks = this.getChunksById( id );
        this.mChunkRegister.remove( id );

        for ( Chunk c : chunks  ) {
            this.mChunkList.remove( c );
        }
    }

    @Override
    public List<Chunk > getSequentialChunks() {
        return this.mChunkList;
    }

    @Override
    public Iterator<Chunk > begin() {
        return this.getSequentialChunks().iterator();
    }

    @Override
    public String toJSONString() {
        return JSON.stringify( this.mChunkList );
    }
}
