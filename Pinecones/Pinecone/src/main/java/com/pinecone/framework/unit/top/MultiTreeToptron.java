package com.pinecone.framework.unit.top;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.unit.MultiValueMapper;
import com.pinecone.framework.unit.TreeMap;
import com.pinecone.framework.unit.Units;
import com.pinecone.framework.unit.multi.MultiCollectionMaptron;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.NavigableMap;

/**
 *  Pinecone Ursus For Java, MultiTreeToptron: For dynamic top-N scenarios.
 *  Author: Harold.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  This tron has not wrapped `Collection` into Readonly mode.
 *  WARNING, All methods which returns `Collection` should be operated-Readonly outside, otherwise the elementSize will got malfunction.
 *  *****************************************************************************************
 *  @param <K> The key, which should implements the comparable in principle.
 *  @param <V> The value
 */
public class MultiTreeToptron<K, V > implements ToptronMultiMap<K, V > {
    protected static <MV >MultiToptronValueAdapter<MV > newLinkdedHashValueAdapter() {
        return new MultiToptronValueAdapter<MV >() {
            @Override
            public Collection<MV > newCollection() {
                return new LinkedHashSet<>();
            }
        };
    }

    protected final NavigableMap<K, Collection<V > >      mTopNCoreMap;
    protected final MultiValueMapper<K, V >               mTopNMap;
    protected int                                         mnTopmost;
    protected int                                         mnElementSize;
    protected TopmostSelector<K, Collection<V > >         mSelector;

    protected MultiTreeToptron( int nTopmost, NavigableMap<K, Collection<V > > coreMap, MultiToptronValueAdapter<V > valueAdapter, TopmostSelector<K, Collection<V > > selector ) {
        this.mTopNCoreMap  = coreMap;
        this.mTopNMap      = new MultiCollectionMaptron<>( this.mTopNCoreMap, true ){
            @Override
            protected Collection<V> newCollection() {
                return valueAdapter.newCollection();
            }
        };
        this.mnTopmost     = nTopmost;
        this.mnElementSize = 0;
        this.mSelector     = selector;
    }

    public MultiTreeToptron( int nTopmost, MultiToptronValueAdapter<V > valueAdapter, TopmostSelector<K, Collection<V > > selector ) {
        this( nTopmost, new TreeMap<>(), valueAdapter, selector );
    }

    public MultiTreeToptron( int nTopmost, TopmostSelector<K, Collection<V > > selector ) {
        this( nTopmost, MultiTreeToptron.newLinkdedHashValueAdapter(), selector );
    }

    public MultiTreeToptron( int nTopmost ) {
        this( nTopmost, TopmostSelector.newGenericGreatestSelector( false ) );
    }

    protected Map.Entry<K, Collection<V > > getMostEntry() {
        return this.mSelector.getMostEntry( this.mTopNCoreMap );
    }

    @Override
    public int getTopmostSize() {
        return this.mnTopmost;
    }

    protected void trim( int nNewTopmost ) {
        int det = this.mnTopmost - nNewTopmost;
        if( det > 0 ) {
            for ( int i = 0; i < det; ++i ) {
                Map.Entry<K, Collection<V > > kv = this.getMostEntry();
                this.erase( kv.getKey(), kv.getValue().iterator().next() );
            }
        }
    }

    @Override
    public MultiTreeToptron<K, V > setTopmostSize( int nTopmost ) {
        this.trim( nTopmost );
        this.mnTopmost = nTopmost;
        return this;
    }

    @Override
    public K nextEvictionKey() {
        Map.Entry<K, Collection<V > > preElimination = this.getMostEntry();
        if( preElimination != null ) {
            return preElimination.getKey();
        }
        return null;
    }

    // In this context, which means there are ONE-Single value will be inserted.
    @Override
    public boolean willAccept( K key ) {
        if ( this.size() >= this.mnTopmost ) {
            Collection<V > c = this.mTopNCoreMap.get( key );
            if ( c == null ) {
                Map.Entry<K, Collection<V > > estEntry = this.getMostEntry();
                return this.mSelector.selects( estEntry, key );
            }
        }
        return true;
    }

    @Override
    public Map.Entry<K, V > nextEviction() {
        Map.Entry<K, Collection<V > > preElimination = this.getMostEntry();
        if( preElimination != null ) {
            return new KeyValue<>( preElimination.getKey(), preElimination.getValue().iterator().next() );
        }
        return null;
    }

    @Override
    public int size() {
        return this.mTopNMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.mTopNMap.isEmpty();
    }

    @Override
    public void clear() {
        this.mTopNMap.clear();
        this.mnElementSize = 0;
    }

    @Override
    public V get( Object k, V v ) {
        return this.mTopNMap.get( k, v );
    }

    @Override
    public Collection<V > get( Object key ) {
        return this.mTopNMap.get( key );
    }

    @Override
    public V getFirst( K key ) {
        return this.mTopNMap.getFirst( key );
    }

    @Override
    public V add( K key, V value ) {
        if ( this.mnElementSize < this.mnTopmost ) {
            V v = this.mTopNMap.add( key, value );
            if( v != null ) {
                ++this.mnElementSize;
            }
            return v;
        }
        else {
            Collection<V > more = this.mTopNMap.get( key );
            if ( more == null || !more.contains( value ) ) {
                Map.Entry<K, Collection<V > > estEntry = this.getMostEntry();
                if( this.mSelector.selects( estEntry, key ) ) {
                    V oldestValue = estEntry.getValue().iterator().next();

                    K           legacyKey = estEntry.getKey();
                    Collection<V > legacy = this.mTopNMap.get( legacyKey );
                    legacy.remove( oldestValue );
                    if( legacy.isEmpty() ) {
                        this.mTopNMap.remove( legacyKey );
                    }
                    --this.mnElementSize;
                    return this.add( key, value );
                }
            }
        }
        return null;
    }

    @Override
    public V set( K k, V v ) {
        Collection<V > legacy = this.mTopNMap.get( k );
        if( legacy != null && !legacy.isEmpty() ) {
            if( this.mTopNMap.set( k, v ) != null ){
                this.mnElementSize -= legacy.size();
                ++this.mnElementSize;
                return v;
            }
        }
        return null;
    }

    @Override
    public void setAll( Map<K, V > m ) {
        for( Map.Entry<K, V > kv : m.entrySet() ) {
            this.add( kv.getKey(), kv.getValue() );
        }
    }

    @Override
    public Collection<V > put( K key, Collection<V > values ) {
        Collection<V > ret = Units.spawnExtendParent( values );
        for( V v : values ) {
            if( this.add( key, v ) != null ) {
                ret.add( v );
            }
        }

        if( ret.isEmpty() ) {
            return null;
        }
        return ret;
    }

    @Override
    public Collection<V > putIfAbsent( K key, Collection<V > value ) {
        if( !this.containsKey( key ) ) {
            return this.put( key, value );
        }
        return null;
    }

    @Override
    public void putAll( Map<? extends K, ? extends Collection<V > > m ) {
        for( Map.Entry<? extends K, ? extends Collection<V > > kv : m.entrySet() ) {
            Collection<V > c = kv.getValue();
            for( V v : c ) {
                this.add( kv.getKey(), v );
            }
        }
    }

    @Override
    public V erase( Object key, V value ) {
        Collection<V > legacy = this.mTopNMap.get( key );
        if ( legacy != null && legacy.contains( value ) ) {
            legacy.remove( value );
            if ( legacy.isEmpty() ) {
                this.mTopNMap.remove( key );
            }
            --this.mnElementSize;
            return value;
        }
        return null;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public boolean remove( Object key, Object values ) {
        if( values instanceof Collection ) {
            Collection c = (Collection)values;
            boolean b = true;
            for( Object v : c ) {
                b = b & this.erase( key, (V)v ) != null;
            }
            return b;
        }
        return false;
    }

    @Override
    public Collection<V > remove( Object key ) {
        Collection<V > legacy = this.mTopNMap.remove( key );
        this.mnElementSize -= legacy.size();
        return legacy;
    }

    @Override
    public boolean containsKey( Object key ) {
        return this.mTopNMap.containsKey( key );
    }

    @Override
    public boolean containsValue( Object value ) {
        return false;
    }

    public V update( K oldKey, K newKey, V value ) {
        this.remove( oldKey, value );
        return this.add( newKey, value );
    }

    public Collection<V > update( K oldKey, K newKey ) {
        Collection<V > legacy = this.mTopNMap.get( oldKey );
        this.mTopNMap.remove( oldKey );
        return this.mTopNMap.puts( newKey, legacy );
    }

    public int elementSize() {
        return this.mnElementSize;
    }

    @Override
    public Set<K > keySet() {
        return this.mTopNMap.keySet();
    }

    @Override
    public Map<K, V > toSingleValueMap() {
        return this.mTopNMap.toSingleValueMap();
    }

    @Override
    public Collection<Entry<K, V > > collection() {
        return this.mTopNMap.collection();
    }

    @Override
    public Collection<V > collectionValues() {
        return this.mTopNMap.collectionValues();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Set<Map.Entry<K, Collection<V > > > entrySet() {
        return (Set<Map.Entry<K, Collection<V > > >)this.mTopNMap.entrySet();
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Collection<Collection<V > > values() {
        return (Collection<Collection<V > >)this.mTopNMap.values();
    }

    public NavigableMap<K, Collection<V > > getMap() {
        return this.mTopNCoreMap;
    }

    public Set<Map.Entry<K, Collection<V > > > topEntrySet(){
        return this.getMap().descendingMap().entrySet();
    }

    public Set<Map.Entry<K, Collection<V > > > bottomEntrySet(){
        return this.getMap().entrySet();
    }
}
