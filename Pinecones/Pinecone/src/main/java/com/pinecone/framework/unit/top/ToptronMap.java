package com.pinecone.framework.unit.top;

import java.util.Map;
import java.util.NavigableMap;

public interface ToptronMap<K, V > extends Map<K, V >, Toptron<K, V > {
    @Override
    V get( Object key );

    @Override
    default V put( K key, V value ) {
        return this.add( key, value );
    }

    @Override
    V update( K oldKey, K newKey ) ;

    @Override
    ToptronMap<K, V > setTopmostSize( int nTopmost );

    @Override
    NavigableMap<K, V > getMap();
}
