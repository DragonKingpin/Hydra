package com.pinecone.framework.unit.multi;

import com.pinecone.framework.unit.MultiValueMaptron;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class MultiCollectionMaptron<K, V > extends MultiValueMaptron<K, V, Collection<V > > implements MultiCollectionMap<K, V > {
    private static final long serialVersionUID = 1897280134591921341L;

    public MultiCollectionMaptron( int initialCapacity ) {
        this( new LinkedHashMap<>( initialCapacity ) );
    }

    public MultiCollectionMaptron( Map<K, Collection<V > > otherMap, boolean bAssimilate ) {
        super( otherMap, bAssimilate );
    }

    public MultiCollectionMaptron( Map<K, Collection<V > > otherMap ) {
        this( otherMap, false );
    }

    public MultiCollectionMaptron() {
        this( new LinkedHashMap<>(), true );
    }

    @Override
    protected Collection<V> newCollection() {
        return new ArrayList<>();
    }
}