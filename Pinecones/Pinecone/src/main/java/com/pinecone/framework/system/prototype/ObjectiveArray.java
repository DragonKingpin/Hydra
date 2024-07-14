package com.pinecone.framework.system.prototype;

import com.pinecone.framework.util.json.JSON;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ObjectiveArray implements Objectom {
    protected Object[] mArray;

    public ObjectiveArray( Object[] arr ) {
        this.mArray = arr;
    }

    @Override
    public int size() {
        return this.mArray.length;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public Object get( Object key ){
        Integer i = ObjectiveList.affirmIntegerKey(key);
        if( i == null ) {
            return null;
        }

        return this.mArray[i];
    }

    @Override
    public void set( Object key, Object val ){
        Integer i = ObjectiveList.affirmIntegerKey(key);
        if( i == null ) {
            return ;
        }

        this.mArray[i] = val;
    }

    @Override
    public boolean hasOwnProperty( Object k ) {
        return this.containsKey( k );
    }

    @Override
    public boolean containsKey( Object k ) {
        Integer i = ObjectiveList.affirmIntegerKey(k);
        if( i == null ) {
            return false;
        }

        int nLength = this.mArray.length;
        if( i < 0 || nLength == 0 ){
            return false;
        }
        return nLength > i;
    }

    @Override
    public String toJSONString() {
        return JSON.stringify( this.mArray );
    }

    @Override
    public TypeIndex prototype() {
        return Prototype.typeid( this.mArray );
    }

    @Override
    public String  prototypeName() {
        return Prototype.prototypeName(this.mArray);
    }

    @Override
    public Integer[] keys() {
        Integer[] list = new Integer[ this.mArray.length ];
        for ( int i = 0; i < this.mArray.length; ++i ) {
            list[ i ] = i;
        }
        return list;
    }
}