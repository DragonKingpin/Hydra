package com.pinecone.slime.cache.query;

import com.pinecone.slime.unitization.PartialRange;

public interface RangedDictCachePage<V > extends CountDictCachePage<V > {
    <T extends Comparable<T > > PartialRange<T > getRange();
}
