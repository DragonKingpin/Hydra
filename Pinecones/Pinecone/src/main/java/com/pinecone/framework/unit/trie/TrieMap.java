package com.pinecone.framework.unit.trie;

import com.pinecone.framework.system.prototype.PineUnit;

import java.util.Map;

public interface TrieMap<K,V> extends Map<K,V>, PineUnit {

    V put(K key, V value);

    V get(Object key);

    boolean containsKey(Object key);

    V remove(Object key);

    int size();

    boolean isEmpty();

    TrieNode getNode(String path);
}
