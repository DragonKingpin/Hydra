package com.pinecone.framework.unit.multi;

import com.pinecone.framework.unit.LinkedTreeSet;
import com.pinecone.framework.unit.MultiValueMaptron;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MultiSetMaptron<K, V > extends MultiValueMaptron<K, V, Set<V > > implements MultiSetMap<K, V > {
    private static final long serialVersionUID = 1367280134591921341L;

    public MultiSetMaptron( Map<K, Set<V > > otherMap, boolean bAssimilate ) {
        super( otherMap, bAssimilate );
    }

    public MultiSetMaptron( Map<K, Set<V > > otherMap ) {
        this( otherMap, false );
    }

    public MultiSetMaptron( int initialCapacity ) {
        this( new LinkedHashMap<>( initialCapacity ) );
    }

    public MultiSetMaptron() {
        this( new LinkedHashMap<>() );
    }


    @Override
    protected Set<V> newCollection() {
        return new LinkedTreeSet<>();
    }
}