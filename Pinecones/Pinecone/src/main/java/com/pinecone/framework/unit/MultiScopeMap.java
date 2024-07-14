package com.pinecone.framework.unit;

import java.util.*;

public interface MultiScopeMap<K, V > extends ScopeMap<K, V > {
    List<MultiScopeMap<K, V > >    getParents    ();

    Map<K, V >                     thisScope  ();

    MultiScopeMap<K, V >           setParents    ( List<MultiScopeMap<K, V > > that );

    MultiScopeMap<K, V >           setThisScope  ( Map<K, V > that );

    MultiScopeMap<K, V >           addParent     ( MultiScopeMap<K, V > that );

    String                         getName       ();

    MultiScopeMap<K, V >           setName       ( String name );

    default boolean                isAnonymous   () {
        return this.getName().isEmpty();
    }

    @Override
    default boolean                isProgenitor  () {
        return this.getParents() == null;
    }

    @Override
    default void                   purge() {
        this.setParents( null );
        this.clear();
    }

    @Override
    default void                   depurate() {
        List<MultiScopeMap<K, V > > p = this.getParents();
        if( p != null ) {
            for ( MultiScopeMap<K, V > m : p ) {
                m.depurate();
            }
        }

        this.clear();
    }

    @Override
    default void                   overrideTo ( Map<K, V > neo ) {
        Map<K, V > self = this.thisScope();
        for ( Map.Entry<? extends K, ? extends V> e : self.entrySet() ){
            neo.putIfAbsent( e.getKey(), e.getValue() );
        }

        List<MultiScopeMap<K, V > > p = this.getParents();
        if( p != null ) {
            for ( MultiScopeMap<K, V > m : p ) {
                m.overrideTo( neo );
            }
        }
    }

    @Override
    default boolean                isScopeEmpty () {
        boolean b = this.isEmpty();

        if( b ) {
            List<MultiScopeMap<K, V > > p = this.getParents();
            if( p != null ) {
                for ( MultiScopeMap<K, V > m : p ) {
                    b = m.isScopeEmpty();
                    if( !b ) {
                        break;
                    }
                }
            }
        }

        return b;
    }

    @Override
    @SuppressWarnings("unchecked")
    default ScopeMap<K, V >[]      ancestors  (){
        List<ScopeMap<K, V>> l = new ArrayList<>();
        ScopeTrees.groupByNodes( this, l );
        return l.toArray( (ScopeMap<K, V >[]) new MultiScopeMap[0] );
    }

    @Override
    @SuppressWarnings("unchecked")
    default ScopeMap<K, V >[]      scopes     (){
        ArrayList<ScopeMap<K, V > > l = new ArrayList<>();
        l.add( this );
        ScopeTrees.groupByNodes( this, l );
        return l.toArray( (ScopeMap<K, V >[]) new MultiScopeMap[0] );
    }

    @Override
    @SuppressWarnings("unchecked")
    default ScopeMap<K, V >        getAll        ( Object key, List<V > ret ) {
        V v = this.thisScope().get( key );
        if( v != null ) {
            ret.add( v );
        }

        ScopeTrees.search( this, ( Object...args )->{
            MultiScopeMap<K, V> currentMap = (MultiScopeMap<K, V>) args[0];
            if( currentMap != this ) {
                V t = currentMap.thisScope().get( key );
                if( t != null ) {
                    ret.add( t );
                }
            }
            return false;
        } );

        return this;
    }

    @SuppressWarnings("unchecked")
    default V                      query         ( Object key, String szParentNS ) {
        final V[] v = (V[]) new Object[1];
        v[0] = this.thisScope().get(key);
        if (v[0] != null) {
            return v[0];
        }

        ScopeTrees.search(this, (Object... args) -> {
            MultiScopeMap<K, V> currentMap = (MultiScopeMap<K, V>) args[0];
            if (currentMap != this) {
                V t = currentMap.thisScope().get(key);
                if (t != null && currentMap.getName().equals(szParentNS)) {
                    v[0] = t;
                    return true;
                }
            }
            return false;
        });

        return v[0];
    }

    @SuppressWarnings("unchecked")
    default MultiScopeMap<K, V >   getScopeByNS  ( String szParentNS ) {
        final Object[] v = new Object[1];

        ScopeTrees.search(this, (Object... args) -> {
            MultiScopeMap<K, V> currentMap = (MultiScopeMap<K, V>) args[0];
            if ( currentMap != this ) {
                if ( currentMap.getName().equals(szParentNS) ) {
                    v[0] = currentMap;
                    return true;
                }
            }
            return false;
        });

        return (MultiScopeMap<K, V > )v[0];
    }

    @Override
    default MultiScopeMap<K, V >   removeAll  ( Object key ) {
        this.thisScope().remove( key );

        List<MultiScopeMap<K, V > > p = this.getParents();
        if( p != null ) {
            for ( MultiScopeMap<K, V > m : p ) {
                m.removeAll( key );
            }
        }

        return this;
    }
}
