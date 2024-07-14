package com.pinecone.framework.unit.top;

import com.pinecone.framework.unit.multi.MultiCollectionMap;

import java.util.Collection;
import java.util.Map;
import java.util.NavigableMap;

public interface ToptronMultiMap<K, V > extends MultiCollectionMap<K, V >, Toptron<K, V > {
    Collection<V > get( Object key );

    ToptronMultiMap<K, V > setTopmostSize( int nTopmost );

    NavigableMap<K, Collection<V > > getMap();

    @Override
    Collection<V > update( K oldKey, K newKey ) ;

    Map.Entry<K, V > nextEviction();
}
