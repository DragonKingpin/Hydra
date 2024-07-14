package com.pinecone.framework.unit.multi;

import com.pinecone.framework.unit.MultiValueMaptron;
import com.pinecone.framework.unit.MultiValueMap;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultiListMaptron<K, V > extends MultiValueMaptron<K, V, List<V > > implements MultiValueMap<K, V > {
    private static final long serialVersionUID = 3801124242820219131L;

    public MultiListMaptron( Map<K, List<V > > otherMap, boolean bAssimilate ) {
        super( otherMap, bAssimilate );
    }

    public MultiListMaptron( Map<K, List<V > > otherMap ) {
        this( otherMap, false );
    }

    public MultiListMaptron( int initialCapacity ) {
        this( new LinkedHashMap<>( initialCapacity ) );
    }

    public MultiListMaptron() {
        this( new LinkedHashMap<>() );
    }

    @Override
    protected List<V > newCollection() {
        return new ArrayList<>();
    }

}