package com.pinecone.slime.cache.query.pool;

import com.pinecone.framework.unit.Dictium;
import com.pinecone.slime.cache.query.LocalBufferedDictCachePage;
import com.pinecone.slime.cache.query.RangedDictCachePage;
import com.pinecone.slime.unitization.PartialRange;

public class LocalRangedDictCachePage<V > extends LocalBufferedDictCachePage<V > implements RangedDictCachePage<V > {
    protected PartialRange   mRange;

    public <T extends Comparable<T > > LocalRangedDictCachePage( long id, int capacity, Dictium<V > cache, PartialRange<T > range ) {
        super( id, capacity, cache );

        this.mRange        = range;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends Comparable<T > > PartialRange<T > getRange() {
        return (PartialRange<T >) this.mRange;
    }
}
