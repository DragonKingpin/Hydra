package com.pinecone.framework.unit;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.system.prototype.Prototype;
import com.pinecone.framework.system.prototype.TypeIndex;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.json.JSON;

import java.util.Map;

public class KeyValue<K, V > implements Map.Entry<K, V >, Pinenut {
    protected K key;

    protected V value;

    public KeyValue( K key, V value ) {
        this.key = key;
        this.value = value;
    }

    public KeyValue( Map.Entry<K, V > other ) {
        this( other.getKey(), other.getValue() );
    }


    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public V setValue( V value ) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    @Override
    public boolean equals( Object o ) {
        if ( !(o instanceof Map.Entry) ) {
            return false;
        }
        Map.Entry<?,?> e = (Map.Entry<?,?>)o;

        return valEquals( this.key,e.getKey()) && valEquals( this.value,e.getValue() );
    }

    @Override
    public int hashCode() {
        int keyHash = (this.key==null ? 0 : this.key.hashCode());
        int valueHash = (this.value==null ? 0 : this.value.hashCode());
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


    static final boolean valEquals( Object o1, Object o2 ) {
        return (o1==null ? o2==null : o1.equals(o2));
    }
}
