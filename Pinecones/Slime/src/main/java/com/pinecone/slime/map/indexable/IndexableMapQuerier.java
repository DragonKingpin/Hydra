package com.pinecone.slime.map.indexable;

import com.pinecone.framework.system.NotImplementedException;
import com.pinecone.slime.cache.query.LocalFixedLRUDictCachePage;
import com.pinecone.slime.cache.query.UniformCountSelfLoadingDictCache;
import com.pinecone.slime.map.AlterableQuerier;
import com.pinecone.slime.source.indexable.GenericIndexKeySourceRetriever;
import com.pinecone.slime.source.indexable.IndexableDataManipulator;
import com.pinecone.slime.source.indexable.IndexableIterableManipulator;
import com.pinecone.slime.source.indexable.IndexableTargetScopeMeta;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

public class IndexableMapQuerier<K, V > implements AlterableQuerier<V > {
    private final IndexableDataManipulator<K, V >  mManipulator;
    protected UniformCountSelfLoadingDictCache<V > mCache;
    protected IndexableTargetScopeMeta             mIndexMeta;

    public IndexableMapQuerier( IndexableTargetScopeMeta meta, UniformCountSelfLoadingDictCache<V > cache ) {
        this.mManipulator  = (IndexableDataManipulator<K, V >) meta.<K, V >getDataManipulator();
        this.mIndexMeta    = meta;
        this.mCache        = cache;
    }

    public IndexableMapQuerier( IndexableTargetScopeMeta meta ) {
        this( meta, new LocalFixedLRUDictCachePage<>( 1000,
                new GenericIndexKeySourceRetriever<>( meta )
        ) );
    }

    @Override
    public long size() {
        return this.mManipulator.counts( this.mIndexMeta, null );
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public void clear() {
        this.mCache.clear();
        this.mManipulator.purge( this.mIndexMeta );
    }

    @Override
    public boolean containsKey( Object key ) {
        return this.mCache.implicatesKey( key );
    }

    @Override
    public boolean containsValue( Object value ) {
        Object values = this.mManipulator.selectAllByNS( this.mIndexMeta, null, null );
        if( values instanceof Collection) {
            return ((Collection) values).contains( value );
        }
        else if( values instanceof Map) {
            return ((Map) values).values().contains( value );
        }
        return false;
    }

    @Override
    public V get(Object key) {
        return this.mCache.get( key );
    }

    @Override
    public V insert( Object key, V value) {
        this.mManipulator.insert( this.mIndexMeta, (K)key, value );
        return value;
    }

    @Override
    public V insertIfAbsent(Object key, V value) {
        if ( !this.containsKey( key ) ) {
            return this.insert( key, value );
        }
        return null;
    }

    @Override
    public V erase(Object key) {
        V value = this.get(key);
        this.expunge(key);
        return value;
    }

    @Override
    public void expunge( Object key ) {
        this.mCache.erase( key );
        this.mManipulator.deleteByKey( this.mIndexMeta, key );
    }

    @Override
    public Set<? extends Map.Entry<?, V>> entrySet() {
        Map<K, V> map = this.toMap();
        return map.entrySet();
    }

    @Override
    public Collection<V> values() {
        return this.toMap().values();
    }

    @Override
    public Map<K, V > toMap() {
        if( this.mManipulator instanceof IndexableIterableManipulator ) {
            IndexableIterableManipulator<K, V > manipulator = (IndexableIterableManipulator<K, V >)this.mManipulator;

            return new IndexableCachedMap<>( this.mIndexMeta, this.mCache, this );
        }
        throw new NotImplementedException( "Manipulator should be IterableManipulator." );
    }

    @Override
    public List<V> toList() {
        return new ArrayList<>( this.values() );
    }

}
