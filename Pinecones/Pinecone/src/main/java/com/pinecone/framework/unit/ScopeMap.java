package com.pinecone.framework.unit;

import com.pinecone.framework.system.prototype.PineUnit;

import java.util.*;

public interface ScopeMap<K, V > extends PineUnit, Map<K, V > {

    Iterator<Entry<K, V> >         scopeIterator() ;

    Set<Entry<K,V> >               scopeEntrySet() ;

    Set<K >                        scopeKeySet()   ;

    Collection<V >                 scopeValues()   ;

    boolean                        isProgenitor()     ;

    // [肃清] Clear itself and its ancestors, nothing left.
    void                           purge();

    // [净化] Clear itself and its ancestors's elements, reserving its ancestors tree.
    void                           depurate();

    // Override and apply all ancestors and itself to a new map.
    void                           overrideTo ( Map<K, V > neo ) ;

    boolean                        isScopeEmpty();

    // Elevate self to a new super class as a new parent, append this child to current self this-scope
    ScopeMap<K, V >                elevate    ( Map<K, V > child ) ;

    ScopeMap<K, V >[]              ancestors  ();

    ScopeMap<K, V >[]              scopes     ();

    ScopeMap<K, V >                getAll     ( Object key, List<V> ret );

    @SuppressWarnings("unchecked")
    default V[]                    getAll     ( Object key ) {
        ArrayList<V> a = new ArrayList<>();
        this.getAll( key, a );
        return (V[]) a.toArray();
    }

    ScopeMap<K, V >                removeAll  ( Object key ) ;

}
