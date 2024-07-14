package com.pinecone.slime.chunk;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.json.JSONEncoder;
import com.pinecone.slime.unitization.MinMaxRange64;
import com.pinecone.slime.unitization.Precision64;

public abstract class RangedChunk64 extends ArchPatriarchalChunk implements Splitunk {
    protected long             mnId;
    protected MinMaxRange64    mRange;
    protected Precision64      mChunkSize;

    protected RangedChunk64(){
        super();
    }

    public RangedChunk64( long nStart, long nEnd, long id, RangedChunk64 parent ) {
        super( parent );
        this.applyMembers( nStart, nEnd, id, parent );
    }

    public RangedChunk64( long nStart, long nEnd, long id ) {
        this( nStart, nEnd, id, null );
    }

    protected void applyMembers( long nStart, long nEnd, long id, RangedChunk64 parent ) {
        this.mParent    = parent;
        this.mnId   = id;
        this.mRange     = new MinMaxRange64( nStart, nEnd );
        this.mChunkSize = new Precision64( (long)this.mRange.span() );
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
    public MinMaxRange64 getRange() {
        return this.mRange;
    }

    @Override
    public Precision64 size(){
        return this.mChunkSize;
    }

    @Override
    public String toJSONString() {
        return JSONEncoder.stringifyMapFormat( new KeyValue[]{
                new KeyValue<>( "class", this.className() ),
                new KeyValue<>( "min", this.getRange().getMin() ),
                new KeyValue<>( "max", this.getRange().getMax() )
        } );
    }
}
