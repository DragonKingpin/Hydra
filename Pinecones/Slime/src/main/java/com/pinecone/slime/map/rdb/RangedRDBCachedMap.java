package com.pinecone.slime.map.rdb;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.unit.Mapnut;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.slime.cache.query.IterableDictCachePage;
import com.pinecone.slime.cache.query.RangedDictCachePage;
import com.pinecone.slime.cache.query.pool.BatchPageSourceRetriever;
import com.pinecone.slime.cache.query.pool.CountSelfPooledPageDictCache;
import com.pinecone.slime.map.MonoKeyQueryRange;
import com.pinecone.slime.source.rdb.RDBTargetTableMeta;
import com.pinecone.slime.source.rdb.RangedRDBQuerierDataManipulator;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.AbstractCollection;
import java.util.NoSuchElementException;


public class RangedRDBCachedMap<K, V > implements Mapnut<K, V > {
    private final RDBMapQuerier<K, V >                      mQuerier;
    private final RangedRDBQuerierDataManipulator<K, V >    mDataMapper;
    protected CountSelfPooledPageDictCache<V >              mCache;
    protected RDBTargetTableMeta                            mTableMeta;

    public RangedRDBCachedMap( RDBTargetTableMeta tableMeta, CountSelfPooledPageDictCache<V > cache, RDBMapQuerier<K, V > querier ) {
        this.mDataMapper = (RangedRDBQuerierDataManipulator<K, V >) tableMeta.<K, V >getDataManipulator();
        this.mTableMeta  = tableMeta;
        this.mCache      = cache;
        this.mQuerier    = querier;
    }

    public RangedRDBCachedMap( RDBTargetTableMeta tableMeta, CountSelfPooledPageDictCache<V > cache ) {
        this( tableMeta, cache, new RDBMapQuerier<>( tableMeta, cache ) );
    }

    @Override
    public long megaSize() {
        return this.mQuerier.size();
    }

    @Override
    public int size() {
        return (int)this.megaSize();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public void clear() {
        this.mQuerier.clear();
    }

    @Override
    public boolean containsKey( Object key ) {
        return this.mQuerier.containsKey( key );
    }

    @Override
    public boolean hasOwnProperty( Object key ) {
        return this.containsKey( key );
    }

    @Override
    public boolean containsValue( Object value ) {
        return this.mQuerier.containsValue( value );
    }

    @Override
    public V get( Object key ) {
        return this.mQuerier.get( key );
    }

    @Override
    public Entry<K, V> getEntryByKey( Object compatibleKey ) {
        return this.getEntryCopyByKey( compatibleKey );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Entry<K, V> getEntryCopyByKey( Object compatibleKey ) {
        return new KeyValue<>( (K)compatibleKey, this.get( compatibleKey ) );
    }

    @Override
    public V put( K key, V value ) {
        return this.mQuerier.insert( key, value );
    }

    @Override
    public V putIfAbsent( K key, V value ) {
        return this.mQuerier.insertIfAbsent( key, value );
    }

    @Override
    public void putAll( Map<? extends K, ? extends V> m ) {
        for( Map.Entry<? extends K, ? extends V> kv : m.entrySet() ){
            this.put( kv.getKey(), kv.getValue() );
        }
    }

    @Override
    public V remove( Object key ) {
        return this.mQuerier.erase( key );
    }

    @Override
    public Set<K > keySet() {
        return new BufferedRDBKeySet();
    }

    @Override
    public Set<Entry<K, V > > entrySet() {
        return new BufferedRDBEntrySet();
    }

    @Override
    public Collection<V > values() {
        return new BufferedRDBValCollection();
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return JSON.stringify( this );
    }

    protected abstract class BufferedRDBIterator {
        protected Comparable                                mMax;
        protected Comparable                                mMin;
        protected RangedRDBQuerierDataManipulator<K, V >    mDataMapper;
        protected RDBTargetTableMeta                        mTableMeta;
        protected BatchPageSourceRetriever<V >              mRetriever;
        protected long                                      mnPageCapacity;
        protected String                                    mszRangeKey;
        protected RangedDictCachePage<V >                   mCurrentPage;
        protected Iterator<? >                              mCurrentIter;

        @SuppressWarnings( "unchecked" )
        public BufferedRDBIterator() {
            this.mDataMapper     = RangedRDBCachedMap.this.mDataMapper;
            this.mTableMeta      = RangedRDBCachedMap.this.mTableMeta;
            this.mRetriever      = (BatchPageSourceRetriever<V > ) RangedRDBCachedMap.this.mCache.getSourceRetriever(); // Specifically required `BatchPageSourceRetriever`.
            this.mszRangeKey     = this.mRetriever.getRangeKey();
            this.mMax            = (Comparable) this.mDataMapper.getMaximumRangeVal( this.mTableMeta, this.mszRangeKey );
            this.mMin            = (Comparable) this.mDataMapper.getMinimumRangeVal( this.mTableMeta, this.mszRangeKey );
            this.mnPageCapacity  = this.mRetriever.getBatchSize();

            this.mCurrentPage    = this.mRetriever.retrieves( this.mMin );
            if( !( this.mCurrentPage instanceof IterableDictCachePage ) ) {
                throw new IllegalArgumentException( "DictCachePage is not iterable." );
            }

            IterableDictCachePage<V > page = ( IterableDictCachePage<V > ) this.mCurrentPage;
            this.mCurrentIter    = page.iterator();
        }

        @SuppressWarnings( "unchecked" )
        public boolean hasNext() {
            if( this.mCurrentIter.hasNext() ) {
                return true;
            }
            this.mCurrentPage    = this.mRetriever.retrieves(
                    this.mMin, new MonoKeyQueryRange( this.mCurrentPage.getRange().getMax(), this.mRetriever.nextRangeMax( this.mCurrentPage.getRange().getMax() ), this.mszRangeKey )
            );
            IterableDictCachePage<V > page = ( IterableDictCachePage<V > ) this.mCurrentPage;
            this.mCurrentIter    = page.iterator();
            return this.mCurrentIter.hasNext();
        }

        @SuppressWarnings( "unchecked" )
        protected Map.Entry<K, V > nextNode() {
            if ( !this.hasNext() ) {
                throw new NoSuchElementException();
            }

            Object next = this.mCurrentIter.next();
            if( next instanceof Map.Entry ) {
                return (Map.Entry<K, V >) next;
            }
            throw new IllegalArgumentException( "Iterable object is not `Map.Entry`." );
        }

        public void remove() {
            this.mCurrentIter.remove();
        }
    }

    protected final class BufferedRDBEntryIterator extends BufferedRDBIterator implements Iterator<Map.Entry<K, V > > {
        public final Map.Entry<K, V > next() { return this.nextNode(); }
    }

    protected class BufferedRDBEntrySet extends AbstractSet<Map.Entry<K, V > > {
        public final int size()                 { return RangedRDBCachedMap.this.size(); }

        public final void clear()               { RangedRDBCachedMap.this.clear(); }

        public final Iterator<Map.Entry<K, V > > iterator() {
            return new BufferedRDBEntryIterator();
        }

        public final boolean contains( Object o ) {
            if ( !(o instanceof Map.Entry) ) {
                return false;
            }
            Map.Entry<?,?> e = (Map.Entry<?,?>) o;
            Object key = e.getKey();

            Object v = RangedRDBCachedMap.this.get(key);
            return v != null && v.equals(e.getValue());
        }

        public final boolean remove( Object o ) {
            if ( this.contains(o) ) {
                Map.Entry<?,?> e = (Map.Entry<?,?>) o;
                Object key = e.getKey();

                return RangedRDBCachedMap.this.remove(key) != null ;
            }
            return false;
        }
    }

    protected final class BufferedRDBKeyIterator extends BufferedRDBIterator implements Iterator<K > {
        public final K next() { return this.nextNode().getKey(); }
    }

    protected class BufferedRDBKeySet extends AbstractSet<K > {
        public final int size()                 { return RangedRDBCachedMap.this.size(); }

        public final void clear()               { RangedRDBCachedMap.this.clear(); }

        public final Iterator<K > iterator() {
            return new BufferedRDBKeyIterator();
        }

        public final boolean contains( Object o ) {
            return RangedRDBCachedMap.this.containsKey( o );
        }

        public final boolean remove( Object o ) {
            return RangedRDBCachedMap.this.remove( o ) != null;
        }
    }

    protected final class BufferedRDBValueIterator extends BufferedRDBIterator implements Iterator<V > {
        public final V next() { return this.nextNode().getValue(); }
    }

    protected class BufferedRDBValCollection extends AbstractCollection<V > {
        public final int size()                 { return RangedRDBCachedMap.this.size(); }

        public final void clear()               { RangedRDBCachedMap.this.clear(); }

        public final Iterator<V > iterator() {
            return new BufferedRDBValueIterator();
        }
    }
}
