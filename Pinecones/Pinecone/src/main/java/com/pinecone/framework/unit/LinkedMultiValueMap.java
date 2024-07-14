package com.pinecone.framework.unit;

import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LinkedMultiValueMap<K, V > extends MultiValueMaptron<K, V, List<V > > implements MultiValueMap<K, V > {
    public LinkedMultiValueMap() {
        this( new LinkedHashMap<>() );
    }

    public LinkedMultiValueMap( int initialCapacity ) {
        this( new LinkedHashMap<>( initialCapacity ) );
    }

    public LinkedMultiValueMap( Map<K, List<V > > otherMap ) {
        super( otherMap, false );
    }

    @Override
    protected List<V > newCollection() {
        return new LinkedList<>();
    }
}
