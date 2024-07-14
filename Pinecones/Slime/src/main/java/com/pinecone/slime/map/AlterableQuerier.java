package com.pinecone.slime.map;

public interface AlterableQuerier<V > extends Querier<V > {
    void clear();

    V insert( Object key, V value );

    V insertIfAbsent( Object key, V value );

    V erase( Object key );

    // No need to retrieve value.
    void expunge( Object key );
}
