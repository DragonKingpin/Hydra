package com.pinecone.framework.unit.top;

import com.pinecone.framework.util.json.JSON;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.NavigableMap;

public class TreeToptron<K, V > implements ToptronMap<K, V > {
    protected final NavigableMap<K, V >                 mTopNCoreMap;
    protected int                                       mnTopmost;
    protected TopmostSelector<K, V >                    mSelector;

    public TreeToptron( int nTopmost, NavigableMap<K, V > map, TopmostSelector<K, V > selector ) {
        this.mnTopmost    = nTopmost;
        this.mTopNCoreMap = map;
        this.mSelector    = selector;
    }

    public TreeToptron( int nTopmost, TopmostSelector<K, V > selector ) {
        this( nTopmost, new TreeMap<>( selector ), selector );
    }

    public TreeToptron( int nTopmost ) {
        this( nTopmost, TopmostSelector.newGenericSmallestSelector( false ) );
    }

    protected Map.Entry<K, V > getMostEntry() {
        return this.mSelector.getMostEntry( this.mTopNCoreMap );
    }

    protected void trim( int nNewTopmost ) {
        int det = this.mnTopmost - nNewTopmost;
        if( det > 0 ) {
            for ( int i = 0; i < det; ++i ) {
                this.remove( this.getMostEntry().getKey() );
            }
        }
    }

    @Override
    public TreeToptron<K, V > setTopmostSize( int nTopmost ) {
        this.trim( nTopmost );
        this.mnTopmost = nTopmost;
        return this;
    }

    @Override
    public int getTopmostSize() {
        return this.mnTopmost;
    }

    @Override
    public int size() {
        return this.mTopNCoreMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.mTopNCoreMap.isEmpty();
    }

    @Override
    public void clear() {
        this.mTopNCoreMap.clear();
    }

    @Override
    public boolean containsKey( Object key ) {
        return this.mTopNCoreMap.containsKey(key);
    }

    @Override
    public boolean containsValue( Object val ) {
        return this.mTopNCoreMap.containsValue(val);
    }

    @Override
    public V get( Object key ) {
        return this.mTopNCoreMap.get( key );
    }

    @Override
    public void putAll( Map<? extends K, ? extends V> m ) {
        for( Map.Entry<? extends K, ? extends V > kv : m.entrySet() ) {
            this.put( kv.getKey(), kv.getValue() );
        }
    }

    @Override
    public K nextEvictionKey() {
        Map.Entry<K, V > preElimination = this.getMostEntry();
        if( preElimination != null ) {
            return preElimination.getKey();
        }
        return null;
    }

    @Override
    public boolean willAccept( K key ) {
        if ( this.size() >= this.mnTopmost ) {
            V v = this.mTopNCoreMap.get( key );
            if ( v == null ) {
                Map.Entry<K, V > estEntry = this.getMostEntry();
                return this.mSelector.selects( estEntry, key );
            }
        }
        return true;
    }

    @Override
    public V add( K key, V value ) {
        if ( this.size() < this.mnTopmost ) {
            return this.mTopNCoreMap.put( key, value );
        }
        else {
            V v = this.mTopNCoreMap.get( key );
            if ( v == null ) {
                Map.Entry<K, V > estEntry = this.getMostEntry();
                if( this.mSelector.selects( estEntry, key ) ) {
                    this.mTopNCoreMap.remove( estEntry.getKey() );
                    return this.put( key, value );
                }
            }
            return null;
        }
    }

    @Override
    public V putIfAbsent( K key, V value ) {
        if ( this.size() < this.mnTopmost ) {
            return this.mTopNCoreMap.putIfAbsent( key, value );
        }
        else {
            V v = this.mTopNCoreMap.get( key );
            if ( v == null ) {
                return this.put( key, value );
            }
            return null;
        }
    }

    @Override
    public V remove( Object key ) {
        return this.mTopNCoreMap.remove( key );
    }

    @Override
    public V update( K oldKey, K newKey, V value ) {
        this.remove( oldKey );
        return this.put( newKey, value );
    }

    @Override
    public V update( K oldKey, K newKey ) {
        V legacy = this.mTopNCoreMap.get( oldKey );
        this.mTopNCoreMap.remove( oldKey );
        return this.mTopNCoreMap.put( newKey, legacy );
    }


    @Override
    public Set<Entry<K, V > > entrySet() {
        return this.mTopNCoreMap.entrySet();
    }

    @Override
    public Set<K > keySet() {
        return this.mTopNCoreMap.keySet();
    }

    @Override
    public Collection<V > values() {
        return this.mTopNCoreMap.values();
    }

    @Override
    public NavigableMap<K, V > getMap() {
        return this.mTopNCoreMap;
    }

    public Set<Map.Entry<K, V > > topEntrySet(){
        return this.getMap().descendingMap().entrySet();
    }

    public Set<Map.Entry<K, V > > bottomEntrySet(){
        return this.getMap().entrySet();
    }

    @Override
    public String toJSONString() {
        return JSON.stringify( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public boolean hasOwnProperty( Object key ) {
        return this.containsKey( key );
    }
}
