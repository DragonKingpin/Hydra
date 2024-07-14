package com.pinecone.slime.cache.query.pool;

import com.pinecone.framework.unit.LinkedTreeMap;

import com.pinecone.slime.cache.query.ArchCountDictCache;
import com.pinecone.slime.cache.query.RangedDictCachePage;
import com.pinecone.slime.unitization.PartialRange;

import java.util.Map;

public class LocalLRUPooledDictCache<IK extends Comparable<IK >, V > extends ArchCountDictCache<V > implements CountSelfPooledPageDictCache<V >  {
    private   final int                                                 mnPagesCapacity;
    private   final int                                                 mnPageCapacity;
    protected final Map<PartialRange<IK >, RangedDictCachePage<V >>     mPageQueuePool;
    protected final BatchPageSourceRetriever<V >                        mSourceRetriever;

    public LocalLRUPooledDictCache( int nPageEachCapacity, int nPagesCapacity, BatchPageSourceRetriever<V > retriever ) {
        super();
        this.mnPageCapacity   = nPageEachCapacity;
        this.mnPagesCapacity  = nPagesCapacity;
        this.mSourceRetriever = retriever;
        this.mPageQueuePool   = new LinkedTreeMap<>( PartialRange.DefaultIntervalRangeComparator ) {
            @Override
            protected boolean removeEldestEntry( Map.Entry<PartialRange<IK >, RangedDictCachePage<V > > eldest ) {
                return this.size() > LocalLRUPooledDictCache.this.mnPagesCapacity;
            }
        };
    }

    @Override
    protected void afterKeyVisited( Object key ) {
        super.afterKeyVisited( key );
        // Since we used the `accessOrder`, the newest accessed key will auto moving to the top.
    }

    @Override
    protected V missKey( Object key ) {
        this.recordMiss();
        RangedDictCachePage<V > page = this.mSourceRetriever.retrieves( key );
        if( page != null ) {
            this.mPageQueuePool.put( page.getRange(), page );
            return page.get( key );
        }
        return null;
    }

    @Override
    public long capacity() {
        return this.mnPageCapacity * this.mnPagesCapacity;
    }

    @Override
    public long getPooledPagesCapacity() {
        return this.mnPagesCapacity;
    }

    @Override
    public long size() {
        return PoolCaches.countPoolSize( this.mPageQueuePool );
    }

    @Override
    public boolean isEmpty() {
        return this.mPageQueuePool.isEmpty();
    }

    protected V getFromCache( Object key ) {
        for( Map.Entry<PartialRange<IK >, RangedDictCachePage<V > > kv : this.mPageQueuePool.entrySet() ) {
            V v = kv.getValue().get( key );
            if( v != null ) {
                return v;
            }
        }
        return null;
    }

    @Override
    public V get( Object key ) {
        V v = this.getFromCache( key );
        if( v == null ) {
            v = this.missKey( key );
        }
        this.afterKeyVisited( key );
        return v;
    }

    @Override
    public V erase( Object key ) {
        for( Map.Entry<PartialRange<IK >, RangedDictCachePage<V > > kv : this.mPageQueuePool.entrySet() ) {
            V v = kv.getValue().erase( key );
            if( v != null ) {
                return v;
            }
        }
        return null;
    }

    @Override
    public boolean existsKey( Object key ) {
        boolean b = this.getFromCache( key ) != null;
        this.afterKeyVisited( key );
        return b;
    }

    @Override
    public boolean implicatesKey( Object key ) {
        return this.get( key ) != null;
    }

    @Override
    public void clear() {
        this.mPageQueuePool.clear();
    }

    @Override
    public BatchPageSourceRetriever<V > getSourceRetriever() {
        return this.mSourceRetriever;
    }
}
