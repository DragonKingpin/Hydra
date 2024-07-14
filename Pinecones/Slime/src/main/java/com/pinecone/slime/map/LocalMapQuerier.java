package com.pinecone.slime.map;

import com.pinecone.framework.unit.Dictium;
import com.pinecone.framework.unit.ListDictium;
import com.pinecone.framework.unit.MapDictium;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class LocalMapQuerier<V > implements AlterableQuerier<V > {
    protected Dictium<V > mTarget;

    public LocalMapQuerier( Dictium<V > dictium ) {
        this.mTarget = dictium;
    }

    public LocalMapQuerier( boolean bUsingList ) {
        if( bUsingList ) {
            this.mTarget = new ListDictium<>();
        }
        else {
            this.mTarget = new MapDictium<>();
        }
    }

    public LocalMapQuerier() {
        this( false );
    }

    @Override
    public long size() {
        return this.mTarget.size();
    }

    @Override
    public boolean isEmpty() {
        return this.mTarget.isEmpty();
    }

    @Override
    public void clear() {
        this.mTarget.clear();
    }

    @Override
    public boolean containsKey( Object key ) {
        return this.mTarget.containsKey( key );
    }

    @Override
    public boolean containsValue( Object value ) {
        return this.mTarget.containsValue( value );
    }

    @Override
    public V get( Object key ) {
        return this.mTarget.get( key );
    }

    @Override
    public V insert( Object key, V value ) {
        return this.mTarget.insert( key, value );
    }

    @Override
    public V insertIfAbsent( Object key, V value ) {
        return this.mTarget.insertIfAbsent( key, value );
    }

    @Override
    public V erase( Object key ) {
        return this.mTarget.erase( key );
    }

    @Override
    public void expunge( Object key ) {
        this.erase( key );
    }

    @Override
    public Set<? > entrySet() {
        return this.mTarget.entrySet();
    }

    @Override
    public Collection<V > values() {
        return this.mTarget.values();
    }

    @Override
    public Map<?, V > toMap() {
        return this.mTarget.toMap();
    }

    @Override
    public List<V > toList() {
        return this.mTarget.toList();
    }

    @Override
    public boolean hasOwnProperty( Object elm ) {
        return this.mTarget.hasOwnProperty( elm );
    }

    @Override
    public String toJSONString() {
        return this.mTarget.toJSONString();
    }

    @Override
    public String toString() {
        return this.mTarget.toString();
    }

}
