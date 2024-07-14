package com.pinecone.framework.unit;

import com.pinecone.framework.unit.multi.MultiCollectionProxyMap;
import com.pinecone.framework.util.json.JSON;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class MultiValueMaptron<K, V, U extends Collection<V > > extends AbstractMultiValueMap<K, V > implements MultiCollectionProxyMap<K, V, U >, Serializable {
    private static final long serialVersionUID = 3801124242820219131L;
    private final Map<K, U >    mTargetMap;

    public MultiValueMaptron( Map<K, U > otherMap, boolean bAssimilate ) {
        if( bAssimilate ) {
            this.mTargetMap = otherMap;
        }
        else {
            this.mTargetMap = new LinkedHashMap<>( otherMap );
        }
    }

    public MultiValueMaptron() {
        this( new LinkedHashMap<>(), true );
    }

    protected Map<K, U > getTargetMap(){
        return this.mTargetMap;
    }

    @SuppressWarnings( "unchecked" )
    protected U newCollection() {
        return (U) new ArrayList<V >();
    }

    @Override
    public V add( K key, V value ) {
        U values = (U)this.mTargetMap.get( key );
        if ( values == null ) {
            values = this.newCollection();
            this.mTargetMap.put( key, values );
        }

        if( ((U)values).add( value ) ){
            return value;
        }
        return null;
    }

    @Override
    public V getFirst( K key ) {
        U values = (U)this.mTargetMap.get( key );
        return values != null ? values.iterator().next() : null;
    }

    @Override
    public V set( K key, V value ) {
        U values = this.newCollection();
        boolean b = values.add( value );
        this.mTargetMap.put( key, values );
        if( b ) {
            return value;
        }
        return null;
    }

    @Override
    public void setAll( Map<K, V > values ) {
        Iterator<Entry<K, V > > iter = values.entrySet().iterator();

        while( iter.hasNext() ) {
            Entry<K, V > entry = (Entry<K, V>)iter.next();
            this.set( entry.getKey(), entry.getValue() );
        }
    }

    @Override
    public Map<K, V > toSingleValueMap() {
        LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<>(this.mTargetMap.size());
        Iterator<Entry<K, U > > iter = this.mTargetMap.entrySet().iterator();

        while( iter.hasNext() ) {
            Entry<K, U > entry = (Entry<K, U >)iter.next();
            singleValueMap.put( entry.getKey(), ((U)entry.getValue()).iterator().next() );
        }

        return singleValueMap;
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
    public boolean containsKey( Object key ) {
        return this.mTargetMap.containsKey(key);
    }

    @Override
    public boolean containsValue( Object value ) {
        return this.mTargetMap.containsValue(value);
    }

    @Override
    public U get( Object key ) {
        return (U)this.mTargetMap.get(key);
    }

    @Override
    public U put( K key, U value ) {
        return (U)this.mTargetMap.put( key, value );
    }

    @Override
    public U remove( Object key ) {
        return (U)this.mTargetMap.remove(key);
    }

    @Override
    public void putAll( Map<? extends K, ? extends U > m ) {
        this.mTargetMap.putAll(m);
    }

    @Override
    public void clear() {
        this.mTargetMap.clear();
    }

    @Override
    public Set<K > keySet() {
        return this.mTargetMap.keySet();
    }

    @Override
    public Collection<U > values() {
        return this.mTargetMap.values();
    }

    @Override
    public Set<Entry<K, U > > entrySet() {
        return this.mTargetMap.entrySet();
    }

    @Override
    public boolean equals( Object obj ) {
        return this.mTargetMap.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.mTargetMap.hashCode();
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