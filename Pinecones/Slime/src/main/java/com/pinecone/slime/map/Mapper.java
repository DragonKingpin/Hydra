package com.pinecone.slime.map;

import com.pinecone.framework.system.prototype.PineUnit;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Mapper<V > extends PineUnit {
    long size();

    boolean isEmpty();

    @Override
    boolean containsKey( Object key );

    boolean containsValue( Object value );

    V get( Object key );

    Set<? > entrySet();

    Collection<V > values();

    Map<?, V > toMap();

    List<V > toList();
}
