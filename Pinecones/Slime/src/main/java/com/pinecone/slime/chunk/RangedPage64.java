package com.pinecone.slime.chunk;

public class RangedPage64 extends RangedChunk64 implements RangedPage {
    public RangedPage64(){
        super();
    }

    public RangedPage64( long nStart, long nEnd, long id, RangedChunk64 parent ) {
        super( nStart, nEnd, id, parent );
    }

    public RangedPage64( long nStart, long nEnd, long id ) {
        super( nStart, nEnd, id );
    }

    public void apply( long nStart, long nEnd, long id, RangedChunk64 parent ) {
        this.applyMembers( nStart, nEnd, id, parent );
    }

    @Override
    public void apply( Object... args ) {
        if( args.length == 0 ) {
            return;
        }
        else if( args.length >= 3 ) {
            long nStart          = ((Number) args[0]).longValue();
            long nEnd            = ((Number) args[1]).longValue();
            long id              = ((Number) args[2]).longValue();
            RangedChunk64 parent  = null;
            if( args.length >= 4 ){
                parent = (RangedChunk64) args[3];
            }

            this.applyMembers( nStart, nEnd, id, parent );
            return;
        }

        throw new IllegalArgumentException( "RangedPage64 only be applied with 0, 3 and 4 arguments." );
    }
}
