package com.pinecone.slime.cache.query.pool;

import com.pinecone.framework.unit.LinkedTreeMap;
import com.pinecone.framework.unit.Mapnut;
import com.pinecone.framework.unit.top.LinkedMultiTreeToptron;
import com.pinecone.slime.cache.query.ArchCountDictCache;
import com.pinecone.slime.cache.query.RangedDictCachePage;
import com.pinecone.slime.unitization.PartialRange;

import java.util.Map;

public class LocalHotspotPooledDictCache<IK extends Comparable<IK >, V > extends ArchCountDictCache<V > implements CountSelfPooledPageDictCache<V > {
    private   final int                                                            mnPagesCapacity;
    private   final int                                                            mnPageCapacity;
    private   final int                                                            mnTemperaturesCapacity;
    protected Mapnut<PartialRange<IK >, RangedDictCachePage<V > >                  mPageQueuePool;        // Interval range search with O(log( SUM( Pages ) / Each ))
    protected Mapnut<PartialRange<IK >, Long >                                     mTemperaturesRecord;   // Interval range search with O(log( SUM( Pages ) / Each ))
    //protected Topper<Map.Entry<Long, RangedDictCachePage<V > > >                   mTopNTemperatures;     // Heap method to find top-N with O(log(N))
    protected LinkedMultiTreeToptron<Long, PartialRange<IK >  >                    mTopNTemperatures;     // Tree method to find top-N with O(log(N))
    protected final BatchPageSourceRetriever<V >                                   mSourceRetriever;

    public LocalHotspotPooledDictCache( int nPageEachCapacity, int nPagesCapacity, int nTemperaturesCapacity, BatchPageSourceRetriever<V > retriever ) {
        super();
        this.mnPageCapacity   = nPageEachCapacity;
        this.mnPagesCapacity  = nPagesCapacity;
        this.mSourceRetriever = retriever;
        if( nTemperaturesCapacity < this.mnPagesCapacity ) {
            throw new IllegalArgumentException( "TemperaturesRecordCapacity can`t below the PagesCapacity." );
        }

        this.mnTemperaturesCapacity   = nTemperaturesCapacity;
        this.mPageQueuePool           = new LinkedTreeMap<>( PartialRange.DefaultIntervalRangeComparator, true ); // With deque sequence access order.
        this.mTemperaturesRecord      = new LinkedTreeMap<>( PartialRange.DefaultIntervalRangeComparator, true ); // With deque sequence access order.
//        this.mTopNTemperatures        = new HeapTopper<>( this.mnPagesCapacity, new Comparator<>() {
//            @Override
//            public int compare( Map.Entry<Long, RangedDictCachePage<V > > o1, Map.Entry<Long, RangedDictCachePage<V > > o2 ) {
//                return o1.getKey().compareTo( o2.getKey() );
//            }
//        });

        this.mTopNTemperatures        = new LinkedMultiTreeToptron< >( this.mnPagesCapacity, true );
        // Select Top-Pages::Temperature as activated caches.
    }

    public LocalHotspotPooledDictCache( int nPageEachCapacity, int nPagesCapacity, BatchPageSourceRetriever<V > retriever ) {
        this( nPageEachCapacity, nPagesCapacity, nPagesCapacity * 4, retriever );
    }

    protected void updateTopNTemperatures( TemperatureInfo info ) {
        this.mTopNTemperatures.clear();
        info.nLowestTemp  = Long.MAX_VALUE;
        info.nHighestTemp = Long.MIN_VALUE;
        for( Map.Entry<PartialRange<IK >, Long > kv : this.mTemperaturesRecord.entrySet() ) {
            Long tp = kv.getValue();
            if ( tp < info.nLowestTemp ) {
                info.nLowestTemp = tp;
                info.lowestEntry = kv;
            }
            if ( tp > info.nHighestTemp ) {
                info.nHighestTemp = tp;
                info.highestEntry = kv;
            }
            this.mTopNTemperatures.add( tp, kv.getKey() );
            //this.mTopNTemperatures.add( new KeyValue<>( tp, this.mPageQueuePool.get( kv.getKey() ) ));
        }

        Mapnut<PartialRange<IK >, RangedDictCachePage<V > >   neoPool = new LinkedTreeMap<>( PartialRange.DefaultIntervalRangeComparator, true );
        //Collection<Map.Entry<Long, RangedDictCachePage<V > > > chosen = this.mTopNTemperatures.topmost();
        info.nPooledLowestTemp  = Long.MAX_VALUE;
        info.nPooledHighestTemp = Long.MIN_VALUE;
        for( Map.Entry<Long, PartialRange<IK > > kv : this.mTopNTemperatures.collection()/*chosen*/ ) {
            Long tp = kv.getKey();
            PartialRange<IK > range = kv.getValue();
            RangedDictCachePage<V > legacy = this.mPageQueuePool.get( range );
            if( legacy == null ) {
                // Restoring from history range.
                RangedDictCachePage<V > recover = this.mSourceRetriever.retrieves( range );
                neoPool.put( range, recover );
            }
            else {
                neoPool.put( range, legacy  );
            }

            if ( tp < info.nPooledLowestTemp ) {
                info.nPooledLowestTemp     = tp;
                info.lowestPooledTopEntry  = kv;
            }
            if ( tp > info.nPooledHighestTemp ) {
                info.nPooledHighestTemp    = tp;
                info.highestPooledTopEntry = kv;
            }
        }

        this.mPageQueuePool = neoPool;
    }

    protected void updateCacheTemperature( Object key ) {
        Map.Entry<PartialRange<IK >, Long > tempInfo = this.mTemperaturesRecord.getEntryByKey( key );
        if( tempInfo != null ) {
            Long temperature = tempInfo.getValue();
            ++temperature;
            tempInfo.setValue( temperature );
        }
    }

    @Override
    protected void afterKeyVisited( Object key ) {
        super.afterKeyVisited( key );
    }

    @Override
    protected V missKey( Object key ) {
        this.recordMiss();
        TemperatureInfo info = new TemperatureInfo();
        this.updateTopNTemperatures( info );

        PartialRange<IK > range = this.mSourceRetriever.queryRangeOnly( key );
        if( range != null ) {
            Long temperature = this.mTemperaturesRecord.get( range );
            if( temperature != null ) {
                ++temperature;
                this.mTemperaturesRecord.put( range, temperature );
                if( temperature >= info.nPooledLowestTemp ) {
                    RangedDictCachePage<V > page = this.mSourceRetriever.retrieves( key );
                    if( this.mPageQueuePool.size() >= this.mnPagesCapacity ) {
                        Map.Entry<Long, PartialRange<IK > > elimination = this.mTopNTemperatures.nextEviction();
                        this.mPageQueuePool.remove( elimination.getValue() );
                    }
                    this.mTopNTemperatures.add( temperature, page.getRange() );  // Updating TopNTemperatures, and substitutes lowest LRU page.
                    this.mPageQueuePool.put( range, page );
                    return page.get( key );
                }

                return this.mSourceRetriever.retrieve( key ); // Don`t using cache.
            }
            else {
                temperature = 1L;
                if( this.mPageQueuePool.size() < this.mnPagesCapacity ) {
                    RangedDictCachePage<V > page = this.mSourceRetriever.retrieves( key );
                    this.mTopNTemperatures.add( temperature, page.getRange() );
                    this.mPageQueuePool.put( range, page );
                    this.mTemperaturesRecord.put( range, temperature ); // TemperaturesRecord.size should beyond PageQueuePool.size
                    return page.get( key );
                }
                else {
                    if( this.mTemperaturesRecord.size() >= this.mnTemperaturesCapacity ) {
                        this.mTemperaturesRecord.remove( info.lowestEntry.getKey() );// Substituting lowest one in record.
                        temperature = info.lowestEntry.getValue();
                    }
                    this.mTemperaturesRecord.put( range, temperature );
                    return this.mSourceRetriever.retrieve( key ); // Don`t using cache.
                }
            }
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
    public V get( Object key ) {
        V v = this.getFromCache( key );
        if( v == null ) {
            //Debug.trace( key );
            v = this.missKey( key ); // Update miss temperature
        }
        else {
            this.updateCacheTemperature( key ); // Update cache temperature
        }
        this.afterKeyVisited( key );
        return v;
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

    class TemperatureInfo {
        long nLowestTemp        = Long.MAX_VALUE;
        long nHighestTemp       = Long.MIN_VALUE;
        long nPooledLowestTemp  = Long.MAX_VALUE;
        long nPooledHighestTemp = Long.MIN_VALUE;

        Map.Entry<PartialRange<IK >, Long > lowestEntry;
        Map.Entry<PartialRange<IK >, Long > highestEntry;
        Map.Entry<Long, PartialRange<IK > > lowestPooledTopEntry; // In top-N unit.
        Map.Entry<Long, PartialRange<IK > > highestPooledTopEntry; // In top-N unit.
    }
}