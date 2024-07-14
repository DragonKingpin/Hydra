package com.pinecone.framework.unit;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.system.prototype.Prototype;
import com.pinecone.framework.system.prototype.TypeIndex;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.json.JSON;

import java.util.Map;

public class BidLinkedEntry<K,V> implements Map.Entry<K,V>, Pinenut {
    protected K key;
    protected V value;
    protected BidLinkedEntry<K,V> before;
    protected BidLinkedEntry<K,V> after;

    BidLinkedEntry( K key, V value ) {
        this.key = key;
        this.value = value;
    }

    public void extend( Map.Entry<K,V > entry ) {
        this.key   = entry.getKey();
        this.value = entry.getValue();
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    @Override
    public int hashCode() {
        int keyHash = (key==null ? 0 : key.hashCode());
        int valueHash = (value==null ? 0 : value.hashCode());
        return keyHash ^ valueHash;
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public String toJSONString() {
        return "{" + StringUtils.jsonQuote( this.key.toString() ) + ":" + JSON.stringify( this.value ) + "}";
    }

    @Override
    public TypeIndex prototype() {
        return Prototype.typeid( this );
    }
}
