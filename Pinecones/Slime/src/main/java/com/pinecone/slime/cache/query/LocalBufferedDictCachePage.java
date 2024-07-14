package com.pinecone.slime.cache.query;

import com.pinecone.framework.unit.Dictium;

/**
 * LocalBufferedDictCachePage
 * Only buffered, not self-loading
 * @param <V>
 */
public class LocalBufferedDictCachePage<V > extends ArchLocalDictCachePage<V > {
    public LocalBufferedDictCachePage( long id, int capacity, Dictium<V > cache ) {
        super( id, capacity, cache );
    }

    public LocalBufferedDictCachePage( int capacity, Dictium<V > cache ) {
        this( -1, capacity, cache );
    }

    public LocalBufferedDictCachePage( Dictium<V > cache ) {
        this( cache.size(), cache );
    }

    @Override
    protected V missKey( Object key ) {
        this.recordMiss();
        return null;
    }
}
