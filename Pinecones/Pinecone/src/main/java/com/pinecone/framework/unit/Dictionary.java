package com.pinecone.framework.unit;

import com.pinecone.framework.util.json.JSONUtils;

import java.util.List;
import java.util.Map;

/**
 *  Dictionary
 *  Author: Harold.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  PHP Array / Python Dictionary Style
 *  *****************************************************************************************
 *  Dragon King, the undefined
 */
public interface Dictionary<V > extends Dictium<V > {
    default void reset () {
        this.resetAsList();
    }

    @Override
    default void clear () {
        if( this.isMap() ) {
            this.getMap().clear();
        }
        else {
            this.getList().clear();
        }
    }

    default void reduce () {
        if( this.isMap() ) {
            this.resetAsList();
        }
        else {
            this.getList().clear();
        }
    }

    @Override
    default V get( Object key ) {
        if( this.isMap() ) {
            return this.getMap().get( key );
        }

        int index = JSONUtils.asInt32Key( key );
        return this.getList().get( index );
    }

    @Override
    default V erase( Object key ) {
        if( this.isMap() ) {
            return this.getMap().remove( key );
        }

        int index = JSONUtils.asInt32Key( key );
        return this.getList().remove( index );
    }

    boolean isMap();

    boolean isList();

    Map<?, V > affirmMap() ;

    List<V >   affirmList() ;

    Map<?, V > resetAsMap() ;

    List<V >   resetAsList() ;

    Dictionary<V > convertToMap();

    Dictionary<V > convertToList();

    Map<?, V > getMap() throws ClassCastException ;

    List<V >   getList() throws ClassCastException ;
}
