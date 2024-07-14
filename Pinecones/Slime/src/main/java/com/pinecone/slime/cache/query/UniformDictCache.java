package com.pinecone.slime.cache.query;

import com.pinecone.framework.system.prototype.Pinenut;

public interface UniformDictCache<V > extends Pinenut {
    long capacity();

    long size();

    boolean isEmpty();

    V get( Object key );

    boolean existsKey( Object key );

    V erase( Object key );

    void clear();
}