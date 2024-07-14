package com.pinecone.slime.cache.query;

import com.pinecone.framework.system.prototype.Pinenut;

public interface SourceRetriever<V > extends Pinenut {
    V retrieve( Object key );

    long countsKey( Object key );
}
