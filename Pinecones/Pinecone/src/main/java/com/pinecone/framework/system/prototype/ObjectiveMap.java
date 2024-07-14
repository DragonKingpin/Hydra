package com.pinecone.framework.system.prototype;

import com.pinecone.framework.util.json.JSON;

import java.util.Collection;
import java.util.Map;

public class ObjectiveMap<K, V> implements Objectom {
    protected Map<K, V > mMap;

    public ObjectiveMap( Map<K, V > map ) {
        this.mMap = map;
    }

    @Override
    public int size() {
        return this.mMap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.mMap.isEmpty();
    }

    public Object get( Object key ){
        return this.mMap.get(key);
    }

    @SuppressWarnings("unchecked")
    public void set( Object key, Object val ){
        this.mMap.put((K)key, (V)val);
    }

    @Override
    public boolean hasOwnProperty( Object k ) {
        if( this.mMap instanceof PineUnit ) {
            ( (PineUnit)this.mMap ).hasOwnProperty( k );
        }
        return this.containsKey(k);
    }

    @Override
    public boolean containsKey( Object k ) {
        return this.mMap.containsKey(k);
    }

    @Override
    public String toJSONString() {
        return JSON.stringify(this.mMap);
    }

    @Override
    public TypeIndex prototype() {
        return Prototype.typeid( this.mMap );
    }

    @Override
    public String  prototypeName() {
        return Prototype.prototypeName(this.mMap);
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public K[] keys() {
        return (K[])this.mMap.keySet().toArray();
    }
}
