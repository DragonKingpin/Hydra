package com.pinecone.slime.chunk.scheduler;


import com.pinecone.framework.util.json.JSON;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Collection;

public class LocalMapChunkRegister<K, V > implements ChunkRegister<K, V > {
    private final Map<K, V > targetMap;

    public LocalMapChunkRegister() {
        this.targetMap = new LinkedHashMap<>();
    }

    public LocalMapChunkRegister( Map<K, V > otherMap ) {
        this.targetMap = otherMap;
    }

    @Override
    public int size() {
        return this.targetMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.targetMap.isEmpty();
    }

    @Override
    public boolean containsKey( Object key ) {
        return this.targetMap.containsKey(key);
    }

    @Override
    public boolean containsValue( Object value ) {
        return this.targetMap.containsValue(value);
    }

    @Override
    public V get( Object key ) {
        return this.targetMap.get(key);
    }

    @Override
    public V put( K key, V value ) {
        return this.targetMap.put(key, value);
    }

    @Override
    public V remove( Object key ) {
        return this.targetMap.remove(key);
    }

    @Override
    public void putAll( Map<? extends K, ? extends V> m ) {
        this.targetMap.putAll(m);
    }

    @Override
    public void clear() {
        this.targetMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return this.targetMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return this.targetMap.values();
    }

    @Override
    public Set<Map.Entry<K, V> > entrySet() {
        return this.targetMap.entrySet();
    }

    @Override
    public boolean equals( Object o ){
        return this.targetMap.equals(o);
    }

    @Override
    public int hashCode(){
        return this.targetMap.hashCode();
    }


    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return JSON.stringify( this );
    }
}
