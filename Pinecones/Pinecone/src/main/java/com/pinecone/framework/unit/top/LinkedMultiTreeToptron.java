package com.pinecone.framework.unit.top;

import com.pinecone.framework.unit.LinkedTreeMap;

import java.util.Collection;

import java.util.NavigableMap;
import java.util.Set;

public class LinkedMultiTreeToptron<K, V > extends MultiTreeToptron<K, V > {
    protected LinkedMultiTreeToptron( int nTopmost, NavigableMap<K, Collection<V > > coreMap, MultiToptronValueAdapter<V > valueAdapter, TopmostSelector<K, Collection<V > > selector ) {
        super( nTopmost, coreMap, valueAdapter, selector );
    }

    public LinkedMultiTreeToptron( int nTopmost, MultiToptronValueAdapter<V > valueAdapter, TopmostSelector<K, Collection<V > > selector, boolean accessOrder ) {
        this( nTopmost, new LinkedTreeMap<>( accessOrder ), valueAdapter, selector );
    }

    public LinkedMultiTreeToptron( int nTopmost, TopmostSelector<K, Collection<V > > selector, boolean accessOrder ) {
        this( nTopmost, MultiTreeToptron.newLinkdedHashValueAdapter(), selector, accessOrder );
    }

    public LinkedMultiTreeToptron( int nTopmost, boolean accessOrder ) {
        this( nTopmost, MultiTreeToptron.newLinkdedHashValueAdapter(), TopmostSelector.newGenericGreatestSelector( false ), accessOrder );
    }

    public LinkedMultiTreeToptron( int nTopmost ) {
        this( nTopmost, true );
    }

    @Override
    public LinkedTreeMap<K, Collection<V > > getMap() {
        return ( LinkedTreeMap<K, Collection<V > > ) this.mTopNCoreMap;
    }

    @Override
    public Set<Entry<K, Collection<V > > > bottomEntrySet(){
        return this.getMap().treeEntrySet();
    }
}
