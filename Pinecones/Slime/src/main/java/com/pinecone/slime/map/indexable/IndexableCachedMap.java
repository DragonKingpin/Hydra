package com.pinecone.slime.map.indexable;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.unit.Mapnut;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.slime.cache.query.UniformCountSelfLoadingDictCache;
import com.pinecone.slime.source.indexable.IndexableDataManipulator;
import com.pinecone.slime.source.indexable.IndexableIterableManipulator;
import com.pinecone.slime.source.indexable.IndexableTargetScopeMeta;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.AbstractSet;
import java.util.AbstractCollection;
import java.util.Iterator;

public class IndexableCachedMap<K, V > implements Mapnut<K, V > {
    private final IndexableMapQuerier<K, V >                mQuerier;
    private final IndexableIterableManipulator<K, V >       mManipulator;
    protected UniformCountSelfLoadingDictCache<V >          mCache;
    protected IndexableTargetScopeMeta                      mIndexMeta;

    public IndexableCachedMap( IndexableTargetScopeMeta indexMeta, UniformCountSelfLoadingDictCache<V > cache, IndexableMapQuerier<K, V > querier ) {
        IndexableDataManipulator<K, V > manipulator = (IndexableDataManipulator<K, V >) indexMeta.<K, V >getDataManipulator();
        if( ! ( manipulator instanceof IndexableIterableManipulator ) ) {
            throw new IllegalArgumentException( "Manipulator should be IterableManipulator." );
        }

        this.mManipulator = (IndexableIterableManipulator<K, V >) manipulator;
        this.mIndexMeta   = indexMeta;
        this.mCache       = cache;
        this.mQuerier     = querier;
    }

    public IndexableCachedMap( IndexableTargetScopeMeta indexMeta, UniformCountSelfLoadingDictCache<V > cache ) {
        this( indexMeta, cache, new IndexableMapQuerier<>( indexMeta, cache ) );
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
        return new IndexableKeySet();
    }

    @Override
    public Set<Entry<K, V > > entrySet() {
        return new IndexableEntrySet();
    }

    @Override
    public Collection<V > values() {
        return new IndexableValCollection();
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return JSON.stringify( this );
    }



    protected final class IndexableValueIterator implements Iterator<V > {
        Iterator<Map.Entry<K, V > > entryIterator;

        IndexableValueIterator() {
            this.entryIterator = IndexableCachedMap.this.mManipulator.iterator( IndexableCachedMap.this.mIndexMeta );
        }

        @Override
        public final boolean hasNext() {
            return this.entryIterator.hasNext();
        }

        public final V next() { return this.entryIterator.next().getValue(); }
    }

    protected class IndexableEntrySet extends AbstractSet<Map.Entry<K, V > > {
        public final int size()                 { return IndexableCachedMap.this.size(); }

        public final void clear()               { IndexableCachedMap.this.clear(); }

        public final Iterator<Map.Entry<K, V > > iterator() {
            return IndexableCachedMap.this.mManipulator.iterator( IndexableCachedMap.this.mIndexMeta );
        }

        public final boolean contains( Object o ) {
            if ( !(o instanceof Map.Entry) ) {
                return false;
            }
            Map.Entry<?,?> e = (Map.Entry<?,?>) o;
            Object key = e.getKey();

            Object v = IndexableCachedMap.this.get(key);
            return v != null && v.equals(e.getValue());
        }

        public final boolean remove( Object o ) {
            if ( this.contains(o) ) {
                Map.Entry<?,?> e = (Map.Entry<?,?>) o;
                Object key = e.getKey();

                return IndexableCachedMap.this.remove(key) != null ;
            }
            return false;
        }
    }

    protected class IndexableKeySet extends AbstractSet<K > {
        public final int size()                 { return IndexableCachedMap.this.size(); }

        public final void clear()               { IndexableCachedMap.this.clear(); }

        public final Iterator<K > iterator() {
            return IndexableCachedMap.this.mManipulator.keysIterator( IndexableCachedMap.this.mIndexMeta );
        }

        public final boolean contains( Object o ) {
            return IndexableCachedMap.this.containsKey( o );
        }

        public final boolean remove( Object o ) {
            return IndexableCachedMap.this.remove( o ) != null;
        }
    }

    protected class IndexableValCollection extends AbstractCollection<V > {
        public final int size()                 { return IndexableCachedMap.this.size(); }

        public final void clear()               { IndexableCachedMap.this.clear(); }

        public final Iterator<V > iterator() {
            return new IndexableValueIterator();
        }
    }

}