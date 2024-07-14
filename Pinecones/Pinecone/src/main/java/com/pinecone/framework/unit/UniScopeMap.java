package com.pinecone.framework.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface UniScopeMap<K, V > extends ScopeMap<K, V > {
    UniScopeMap<K, V >             parent();

    Map<K, V >                     thisScope();

    UniScopeMap<K, V >             setParent    ( UniScopeMap<K, V > that );

    UniScopeMap<K, V >             setThisScope ( Map<K, V > that );

    @Override
    default boolean                isProgenitor() {
        return this.parent() == null;
    }

    @Override
    default void                   purge() {
        this.setParent( null );
        this.clear();
    }

    @Override
    default void                   depurate() {
        UniScopeMap<K, V > p = this.parent();

        while ( p != null ) {
            p.clear();
            p = p.parent();
        }

        this.clear();
    }

    @Override
    default void                   overrideTo ( Map<K, V > neo ) {
        neo.putAll( this.thisScope() );
        UniScopeMap<K, V > p = this.parent();
        while ( p != null ) {
            Map<K, V > pm = p.thisScope();
            for( Map.Entry<K, V > o : pm.entrySet() ) {
                neo.putIfAbsent( o.getKey(), o.getValue() );
            }

            p = p.parent();
        }
    }

    @Override
    default boolean                isScopeEmpty  () {
        boolean b = this.isEmpty();

        if( b ) {
            UniScopeMap<K, V > p = this.parent();

            while ( p != null ) {
                b = p.isEmpty();
                if( !b ) {
                    return b;
                }

                p = p.parent();
            }
        }

        return b;
    }

    @Override
    @SuppressWarnings("unchecked")
    default ScopeMap<K, V >[]      ancestors     (){
        ArrayList<ScopeMap<K, V > > l = new ArrayList<>();
        ScopeTrees.groupByNodes( this, l );
        return l.toArray( (ScopeMap<K, V >[]) new UniScopeMap[0] );
    }

    @Override
    @SuppressWarnings("unchecked")
    default ScopeMap<K, V >[]      scopes        (){
        ArrayList<ScopeMap<K, V > > l = new ArrayList<>();
        l.add( this );
        ScopeTrees.groupByNodes( this, l );
        return l.toArray( (ScopeMap<K, V >[]) new UniScopeMap[0] );
    }

    default UniScopeMap<K, V >     progenitor () {
        if( this.parent() == null ) {
            return this;
        }
        else {
            UniScopeMap<K, V > p = this.parent();
            while ( p != null ) {
                p = p.parent();
            }

            return p;
        }
    }

    @Override
    default ScopeMap<K, V >        getAll        ( Object key, List<V > ret ) {
        V v = this.thisScope().get( key );
        if( v != null ) {
            ret.add( v );
        }

        UniScopeMap<K, V > p = this.parent();
        while ( p != null ) {
            Map<K, V > pm = p.thisScope();
            v = pm.get( key );
            if( v != null ) {
                ret.add( v );
            }

            p = p.parent();
        }

        return this;
    }

    @Override
    default ScopeMap<K, V >        removeAll  ( Object key ) {
        this.thisScope().remove( key );

        UniScopeMap<K, V > p = this.parent();
        while ( p != null ) {
            Map<K, V > pm = p.thisScope();
            pm.remove( key );

            p = p.parent();
        }

        return this;
    }

}
