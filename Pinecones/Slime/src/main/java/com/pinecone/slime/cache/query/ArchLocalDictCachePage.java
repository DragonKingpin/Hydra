package com.pinecone.slime.cache.query;

import com.pinecone.framework.unit.Dictium;

import java.util.Collection;
import java.util.Set;

public abstract class ArchLocalDictCachePage<V > extends ArchCountDictCache<V > implements LocalDictCachePage<V >, IterableDictCachePage<V > {
    private long                  mnId;
    private final int             mnCapacity;
    private final Dictium<V >     mCache;

    protected ArchLocalDictCachePage( long id, int capacity, Dictium<V > cache ) {
        super();
        this.mnId       = id;
        this.mnCapacity = capacity;
        this.mCache     = cache;
    }

    @Override
    public long getId() {
        return this.mnId;
    }

    @Override
    public void setId( long id ) {
        this.mnId = id;
    }

    @Override
    public Dictium<V > getDictium() {
        return this.mCache;
    }

    @Override
    public long capacity() {
        return this.mnCapacity;
    }

    @Override
    public long size() {
        return this.mCache.size();
    }

    @Override
    public boolean isEmpty() {
        return this.mCache.isEmpty();
    }

    @Override
    public V get( Object key ) {
        V v = this.mCache.get( key );
        if( v == null ) {
            v = this.missKey( key );
        }
        this.afterKeyVisited( key );
        return v;
    }

    @Override
    public V erase( Object key ) {
        V v = this.mCache.erase( key );
        this.afterKeyVisited( key );
        return v;
    }

    @Override
    public boolean existsKey( Object key ) {
        boolean b = this.mCache.containsKey( key );
        this.afterKeyVisited( key );
        return b;
    }

    @Override
    public void clear() {
        this.mCache.clear();
    }

    @Override
    public long elementSize() {
        return this.size();
    }

    @Override
    public Set<? > entrySet() {
        return this.getDictium().entrySet();
    }

    @Override
    public Collection<V > values() {
        return this.getDictium().values();
    }
}
