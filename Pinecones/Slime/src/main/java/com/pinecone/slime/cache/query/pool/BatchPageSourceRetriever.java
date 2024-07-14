package com.pinecone.slime.cache.query.pool;

import com.pinecone.slime.cache.query.RangedDictCachePage;
import com.pinecone.slime.cache.query.SourceRetriever;
import com.pinecone.slime.unitization.PartialRange;

public interface BatchPageSourceRetriever<V > extends SourceRetriever<V > {
    String getRangeKey();

    RangedDictCachePage<V > retrieves( Object key );

    <T extends Comparable<T > > RangedDictCachePage<V > retrieves( Object key, PartialRange<T > range );

    <T extends Comparable<T > > PartialRange<T > queryRangeOnly( Object key );

    <T extends Comparable<T > > long counts( PartialRange<T > range );

    long getBatchSize();

    <T extends Comparable<T > > T nextRangeMax( T key );
}
