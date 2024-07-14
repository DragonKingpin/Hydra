package com.pinecone.framework.unit.top;

import com.pinecone.framework.system.prototype.PineUnit;

import java.util.NavigableMap;

public interface Toptron<K, V > extends PineUnit {
    int size();

    boolean isEmpty();

    void clear();

    boolean containsKey( Object key );

    boolean containsValue( Object val );

    Object get( Object key );

    V add( K key, V value );

    Toptron<K, V > setTopmostSize(int nTopmost );

    int getTopmostSize();

    NavigableMap<K, ? > getMap();

    V update( K oldKey, K newKey, V value ) ;

    Object update( K oldKey, K newKey ) ;

    K nextEvictionKey();

    boolean willAccept( K key );
}
