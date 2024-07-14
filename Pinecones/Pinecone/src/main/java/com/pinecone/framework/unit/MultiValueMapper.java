package com.pinecone.framework.unit;

import com.pinecone.framework.system.prototype.PineUnit;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


public interface MultiValueMapper<K, V > extends PineUnit {
    V getFirst( K k );

    V add( K k, V v );

    V set( K k, V v );

    void setAll( Map<K, V > m );

    V erase( Object key, V value );

    Map<K, V > toSingleValueMap();

    V get( Object k, V v );

    @Override
    default boolean hasOwnProperty( Object key ) {
        return this.containsKey( key );
    }

    int size();

    boolean isEmpty();

    boolean containsKey( Object key );

    boolean containsValue( Object value );

    Collection<V > get(Object key );

    Collection<V > puts( K key, Collection<V > value );

    Collection<V > remove( Object key );

    void putsAll( Map<? extends K, ? extends Collection<V > > m );

    void clear();

    Set<K > keySet();

    Collection<? extends Collection<V > > values();

    Set<? extends Map.Entry<K, ? extends Collection<V > > > entrySet();

    Collection<Map.Entry<K, V > > collection() ;

    Collection<V > collectionValues();
}