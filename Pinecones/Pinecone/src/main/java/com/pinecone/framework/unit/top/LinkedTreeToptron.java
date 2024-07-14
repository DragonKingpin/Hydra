package com.pinecone.framework.unit.top;

import com.pinecone.framework.unit.LinkedTreeMap;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;

public class LinkedTreeToptron<K, V > extends TreeToptron<K, V > {
    protected LinkedTreeToptron( int nTopmost, NavigableMap<K, V > map, TopmostSelector<K, V > selector ) {
        super( nTopmost, map, selector );
    }

    public LinkedTreeToptron( int nTopmost, TopmostSelector<K, V > selector ) {
        this( nTopmost, new LinkedTreeMap<>( selector ), selector );
    }

    public LinkedTreeToptron( int nTopmost, TopmostSelector<K, V > selector, boolean accessOrder ) {
        this( nTopmost, new LinkedTreeMap<>( selector, accessOrder ), selector );
    }

    public LinkedTreeToptron( int nTopmost, boolean accessOrder ) {
        this( nTopmost, new LinkedTreeMap<>( accessOrder ), TopmostSelector.newGenericGreatestSelector( false ) );
    }

    public LinkedTreeToptron( int nTopmost ) {
        this( nTopmost, true );
    }

    @Override
    public LinkedTreeMap<K, V > getMap() {
        return ( LinkedTreeMap<K, V > ) this.mTopNCoreMap;
    }

    @Override
    public Set<Entry<K, V > > bottomEntrySet(){
        return this.getMap().treeEntrySet();
    }
}
