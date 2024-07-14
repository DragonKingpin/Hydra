package com.sauron.radium.heistron.scheduler;

import com.pinecone.slime.chunk.RangedChunk64;
import com.pinecone.slime.chunk.RangedPage64;

public class RangedTaskPage extends RangedPage64 implements TaskPage {
    public RangedTaskPage(){
        super();
    }

    public RangedTaskPage( long nStart, long nEnd, long id, RangedChunk64 parent ) {
        super( nStart, nEnd, id, parent );
    }

    public RangedTaskPage( long nStart, long nEnd, long id ) {
        super( nStart, nEnd, id );
    }

}
