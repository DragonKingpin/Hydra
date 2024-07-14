package com.pinecone.framework.unit;

import com.pinecone.framework.util.json.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.LinkedHashMap;

public class MapDictium<V > implements Dictium<V >, Map<Object, V > {
    private Map<Object, V > mTargetMap;

    public MapDictium( Map<Object, V > map ) {
        this.mTargetMap = map;
    }

    @SuppressWarnings( "unchecked" )
    public MapDictium( Map map, boolean bUnchecked ) {
        this( map );
    }

    public MapDictium() {
        this( new LinkedHashMap<>() );
    }

    @Override
    public int size() {
        return this.mTargetMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.mTargetMap.isEmpty();
    }

    @Override
    public void clear() {
        this.mTargetMap.clear();
    }

    @Override
    public boolean containsKey( Object key ) {
        return this.mTargetMap.containsKey(key);
    }

    @Override
    public boolean containsValue( Object value ) {
        return this.mTargetMap.containsValue( value );
    }

    @Override
    public V get( Object key ) {
        return this.mTargetMap.get( key );
    }

    @Override
    public V put( Object key, V value ) {
        return this.insert( key, value );
    }

    @Override
    public void putAll( Map<?, ? extends V> m ) {
        this.mTargetMap.putAll( m );
    }

    @Override
    public V insert( Object key, V value ) {
        return this.mTargetMap.put( key, value );
    }

    @Override
    public V insertIfAbsent( Object key, V value ) {
        return this.mTargetMap.putIfAbsent( key, value );
    }

    @Override
    public V erase( Object key ) {
        return this.mTargetMap.remove( key );
    }

    @Override
    public V remove( Object key ) {
        return this.erase( key );
    }

    @Override
    public boolean remove( Object key, Object value ) {
        return this.mTargetMap.remove( key, value );
    }

    @Override
    public Set<Map.Entry<Object, V > > entrySet() {
        return this.mTargetMap.entrySet();
    }

    @Override
    public Set<Object > keySet() {
        return this.mTargetMap.keySet();
    }

    @Override
    public Collection<V > values() {
        return this.mTargetMap.values();
    }

    @Override
    public Map<Object, V > toMap() {
        return this.mTargetMap;
    }

    @Override
    public List<V > toList() {
        return new ArrayList<>( this.mTargetMap.values() );
    }

    @Override
    public boolean hasOwnProperty( Object index ) {
        return this.containsKey( index );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return JSON.stringify( this.mTargetMap );
    }

}
