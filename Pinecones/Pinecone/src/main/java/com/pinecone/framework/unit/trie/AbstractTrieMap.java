package com.pinecone.framework.unit.trie;

public abstract class AbstractTrieMap<K, V> implements TrieMap<K, V> {
    public enum ItemListMode {
        All,
        Dir,
        Value
    }
}
