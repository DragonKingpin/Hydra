package com.pinecone.slime.chunk.scheduler;

import com.pinecone.slime.chunk.Splitunk;
import com.pinecone.slime.unitization.MinMaxRange;
import com.pinecone.slime.chunk.Chunk;

public abstract class FixedChunkDivider64 extends ArchMasterSplitunkDivider64 implements ChunkDivider {
    protected long          mnEach;
    protected long          mnStartOffset;
    protected long          mnChunkMin;
    protected long          mnChunkMax;
    protected long          mnChunkElements;

    protected long          mnCurrentEpoch;
    protected long          mnMaxAllocations;


    public FixedChunkDivider64(Splitunk masterChunk, long each ) {
        super( masterChunk );

        this.mnEach            = each;

        this.mnChunkMin        = ( (MinMaxRange)this.mMasterChunk.getRange() ).getMin().longValue();
        this.mnChunkMax        = ( (MinMaxRange)this.mMasterChunk.getRange() ).getMax().longValue();
        this.mnStartOffset     = this.mnChunkMin;
        this.mnChunkElements   = this.mnChunkMax - this.mnChunkMin;

        this.mnCurrentEpoch    = 0;
        this.mnMaxAllocations  = (this.mnChunkElements + this.mnEach - 1) / this.mnEach;
    }

    protected long nextRange( long to ) {
        if( to + this.mnEach > this.mnChunkElements ) {
            return this.mnChunkElements;
        }
        return to + this.mnEach;
    }

    @Override
    public long getMaxAllocations() {
        return this.mnMaxAllocations;
    }

    @Override
    public long remainAllocatable(){
        return this.mnMaxAllocations - this.mnCurrentEpoch;
    }

    public long getEach() {
        return this.mnEach;
    }

    @Override
    public Chunk allocate() throws BadAllocateException {
        if( this.mnCurrentEpoch < this.getMaxAllocations() ) {
            long start  = this.nextRange( (this.mnCurrentEpoch - 1) * this.mnEach ) + this.mnStartOffset;
            long end    = this.nextRange( this.mnCurrentEpoch       * this.mnEach ) + this.mnStartOffset;

            Chunk chunk = this.newChunk( start, end, this.mnCurrentEpoch );

            ++this.mnCurrentEpoch;

            return chunk;
        }

        throw new BadAllocateException();
    }
}
