package com.pinecone.slime.cache.query.pool;

import com.pinecone.slime.cache.query.RangedDictCachePage;

/**
 * For the index-key is same with the dict-cache-key
 * e.g. RDB::id( Auto-Increment ) as the Range-Key and the Cache-Key
 *    [000-100] => [object { 0 => key0, 1 => key1, ..., 100 => key100 }]
 *    [100-200] => [object { 100 => key100, 101 => key101, ..., 200 => key200 }]
 * In this example: Find( key ) => O(log(pages))(TreeMap) + O(1)(HashMap)
 */
public class LocalLRUPrimaryPooledDictCache<IK extends Comparable<IK >, V > extends LocalLRUPooledDictCache<IK, V > {
    public LocalLRUPrimaryPooledDictCache( int nPageEachCapacity, int nPagesCapacity, BatchPageSourceRetriever<V > retriever ) {
        super( nPageEachCapacity, nPagesCapacity, retriever );
    }

    @Override
    protected V getFromCache( Object key ) {
        RangedDictCachePage<V > page = this.mPageQueuePool.get( key );
        if( page != null ) {
            return page.get( key );
        }
        return null;
    }
}
