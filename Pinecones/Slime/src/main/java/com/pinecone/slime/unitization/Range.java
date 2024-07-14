package com.pinecone.slime.unitization;

import com.pinecone.framework.system.prototype.Pinenut;

public interface Range extends Pinenut {
    boolean contains( Range that );

    boolean contains( Object elm );
}
