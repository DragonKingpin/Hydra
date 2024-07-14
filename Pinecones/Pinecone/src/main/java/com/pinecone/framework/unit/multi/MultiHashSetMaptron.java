package com.pinecone.framework.unit.multi;

import com.pinecone.framework.unit.MultiValueMaptron;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map;

public class MultiHashSetMaptron<K, V > extends MultiValueMaptron<K, V, Set<V > > implements MultiSetMap<K, V > {
    public MultiHashSetMaptron() {
        this( new LinkedHashMap<>() );
    }

    public MultiHashSetMaptron( int initialCapacity ) {
        this( new LinkedHashMap<>( initialCapacity ) );
    }

    public MultiHashSetMaptron( Map<K, Set<V > > otherMap, boolean bAssimilate ) {
        super( otherMap, bAssimilate );
    }

    public MultiHashSetMaptron( Map<K, Set<V > > otherMap ) {
        this( otherMap, false );
    }

    @Override
    protected Set<V> newCollection() {
        return new LinkedHashSet<>();
    }
}
