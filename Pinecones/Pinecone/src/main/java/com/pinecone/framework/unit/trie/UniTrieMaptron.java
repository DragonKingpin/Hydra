package com.pinecone.framework.unit.trie;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.util.*;
import java.util.function.Supplier;

public class UniTrieMaptron<K extends String, V> implements TrieMap<K, V> {

    private final TrieNode root;
    private final Supplier<Map<String, TrieNode>> mapSupplier;
    private int size;
    private String mszSeparator;

    public UniTrieMaptron() {
        this(HashMap::new);
        this.mszSeparator = "/";
    }

    public UniTrieMaptron(String mszSeparator) {
        this(HashMap::new);
        this.mszSeparator = mszSeparator;
    }

    public UniTrieMaptron(Supplier<Map<String, TrieNode>> mapSupplier) {
        if (mapSupplier == null) {
            throw new IllegalArgumentException("Map supplier cannot be null.");
        }
        this.mapSupplier = mapSupplier;
        this.root = new TrieNode(mapSupplier.get());
        this.size = 0;
        this.mszSeparator = "/";
    }

    @SuppressWarnings("unchecked")
    protected V convertValue(Object value){
        return (V) value;
    }

    @Override
    public boolean hasOwnProperty(Object elm) {
        return this.containsKey( elm );
    }

    @Override
    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        String[] segments = key.split(mszSeparator);
        TrieNode node = root;

        for (String segment : segments) {
            node.children.putIfAbsent(segment, new TrieNode(mapSupplier.get()));
            node = node.children.get(segment);
        }

        if (!node.isEnd) {
            size++;
        }
        node.isEnd = true;
        Object oldValue = node.value;
        node.value = value;

        return this.convertValue(oldValue);
    }

    public V putReference(K key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null.");
        }
        String[] segments = key.split(mszSeparator);
        TrieNode node = root;

        for (String segment : segments) {
            node.children.putIfAbsent(segment, new TrieNode(mapSupplier.get()));
            node = node.children.get(segment);
        }

        if (!node.isEnd) {
            size++;
        }
        node.isEnd = true;
        Object oldValue = node.value;
        node.value = value;

        return this.convertValue(oldValue);
    }

    @Override
    public V get(Object key) {
        if ( !(key instanceof String) ) {
            return null;
        }

        TrieNode node = this.getNode((String) key);
        while ( true ){
            if( node.value instanceof TrieReparseNode ) {
                TrieReparseNode trieReparseNode = (TrieReparseNode) node.value;
                node = this.getNode( trieReparseNode.getPath() );
            }
            else {
                return this.convertValue( node.value );
            }
        }
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null || !(key instanceof String)) {
            return false;
        }

        TrieNode node = this.getNode((String) key);
        return node != null && node.isEnd;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(TrieNode node, Object value) {
        if (node == null) {
            return false;
        }
        if (node.isEnd && Objects.equals(node.value, value)) {
            return true;
        }
        for (TrieNode child : node.children.values()) {
            if (containsValueRecursive(child, value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V remove(Object key) {
        if ( !(key instanceof String) ) {
            return null;
        }

        return remove(root, ((String) key).split(mszSeparator), 0);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> result = new HashSet<>();
        collectKeys(root, new StringBuilder(), result);
        return result;
    }

    private void collectKeys(TrieNode node, StringBuilder path, Set<K> result) {
        if (node.isEnd) {
            result.add((K) path.toString());
        }

        for (Map.Entry<String, TrieNode> entry : node.children.entrySet()) {
            StringBuilder newPath = new StringBuilder(path);
            if (newPath.length() > 0) {
                newPath.append(mszSeparator);
            }
            newPath.append(entry.getKey());
            collectKeys(entry.getValue(), newPath, result);
        }
    }

    @Override
    public Collection<V> values() {
        List<V> result = new ArrayList<>();
        collectValues(root, result);
        return result;
    }

    private void collectValues(TrieNode node, List<V> result) {
        if (node.isEnd) {
            result.add(this.convertValue(node.value));
        }

        for (TrieNode child : node.children.values()) {
            collectValues(child, result);
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> result = new HashSet<>();
        collectEntries(root, new StringBuilder(), result);
        return result;
    }

    private void collectEntries(TrieNode node, StringBuilder path, Set<Entry<K, V>> result) {
        if (node.isEnd) {
            result.add(new AbstractMap.SimpleEntry<>((K) path.toString(), this.convertValue(node.value)));
        }

        for (Map.Entry<String, TrieNode> entry : node.children.entrySet()) {
            StringBuilder newPath = new StringBuilder(path);
            if (newPath.length() > 0) {
                newPath.append(mszSeparator);
            }
            newPath.append(entry.getKey());
            collectEntries(entry.getValue(), newPath, result);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    // 根据路径段获取节点
    public TrieNode getNode(String key) {
        String[] segments = key.split(mszSeparator);
        TrieNode node = root;

        for (String segment : segments) {
            node = node.children.get(segment);
            if (node == null) {
                return null;
            }
        }

        return node;
    }

    // 辅助递归移除指定路径键
    private V remove(TrieNode node, String[] segments, int depth) {
        if (node == null) {
            return null;
        }

        if (depth == segments.length) {
            if (!node.isEnd) {
                return null;
            }
            node.isEnd = false;
            V oldValue = this.convertValue(node.value);
            node.value = null;
            size--;
            return oldValue;
        }

        String segment = segments[depth];
        TrieNode nextNode = node.children.get(segment);
        V result = remove(nextNode, segments, depth + 1);

        if (nextNode != null && nextNode.children.isEmpty() && !nextNode.isEnd) {
            node.children.remove(segment);
        }

        return result;
    }
}

