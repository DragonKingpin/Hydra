package com.pinecone.framework.unit;

import com.pinecone.framework.system.prototype.PineUnit;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Dictium<V > extends PineUnit {
    int size();

    boolean isEmpty();

    void clear();

    @Override
    boolean containsKey( Object key );

    boolean containsValue( Object value );

    V get( Object key );

    V insertIfAbsent( Object key, V value );

    V insert( Object key, V value );

    V erase( Object key );

    Set<? > entrySet();

    Collection<V > values();

    Map<?, V > toMap();

    List<V > toList();
}