package com.pinecone.framework.system.prototype;

import com.pinecone.framework.util.json.JSON;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObjectiveList<T> implements Objectom {
    protected List<T > mList;

    public ObjectiveList( List<T > list ) {
        this.mList = list;
    }

    public static Integer affirmIntegerKey( Object key ) {
        if ( key instanceof Integer ) {
            return (Integer) key;
        }
        else if ( key instanceof Long ) {
            return  (int)(long) key;
        }
        else if ( key instanceof Short ) {
            return (int)(short) key;
        }
        else if ( key instanceof Byte ) {
            return (int) (byte) key;
        }
        else if ( key instanceof String ) {
            String szKey = (String) key;
            return Integer.parseInt(szKey);
        }

        return null;
    }

    @Override
    public int size() {
        return this.mList.size();
    }

    @Override
    public boolean isEmpty() {
        return this.mList.isEmpty();
    }

    @Override
    public Object get( Object key ){
        Integer i = ObjectiveList.affirmIntegerKey(key);
        if( i == null ) {
            return null;
        }

        return this.mList.get(i);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set( Object key, Object val ){
        Integer i = ObjectiveList.affirmIntegerKey(key);
        if( i == null ) {
            return ;
        }

        this.mList.set(i, (T)val);
    }

    @Override
    public boolean hasOwnProperty( Object k ) {
        if( this.mList instanceof PineUnit ) {
            ( (PineUnit)this.mList ).hasOwnProperty( k );
        }
        return this.containsKey( k );
    }

    @Override
    public boolean containsKey( Object k ) {
        Integer i = ObjectiveList.affirmIntegerKey(k);
        if( i == null ) {
            return false;
        }

        int nLength = this.mList.size();
        if( i < 0 || nLength == 0 ){
            return false;
        }
        return nLength > i;
    }

    @Override
    public String toJSONString() {
        return JSON.stringify(this.mList);
    }

    @Override
    public TypeIndex prototype() {
        return Prototype.typeid( this.mList );
    }

    @Override
    public String  prototypeName() {
        return Prototype.prototypeName(this.mList);
    }

    @Override
    public Integer[] keys() {
        Integer[] list = new Integer[ this.mList.size() ];
        for ( int i = 0; i < this.mList.size(); ++i ) {
            list[ i ] = i;
        }
        return list;
    }
}
