package com.pinecone.framework.unit;

import com.pinecone.framework.system.PineRuntimeException;
import com.pinecone.framework.system.functions.Function;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public abstract class ScopeTrees {
    public static <K, V> void search ( MultiScopeMap<K, V > that, Function fn ) {
        Deque<MultiScopeMap<K, V>> stack = new ArrayDeque<>();
        stack.push( that );
        while ( !stack.isEmpty() ) {
            MultiScopeMap<K, V> currentMap = stack.pop();

            try{
                if( (boolean) fn.invoke( currentMap ) ) {
                    break;
                }
            }
            catch ( Exception e ) {
                throw new PineRuntimeException( e );
            }

            List<MultiScopeMap<K, V> > parents = currentMap.getParents();
            if( parents != null ) {
                for ( MultiScopeMap<K, V> parent : parents ) {
                    stack.push( parent );
                }
            }
        }
    }

    public static <K, V> void groupByNodes( MultiScopeMap<K, V > that, List<ScopeMap<K, V> > list ) {
        Deque<MultiScopeMap<K, V>> stack = new ArrayDeque<>();
        stack.push( that );
        while ( !stack.isEmpty() ) {
            MultiScopeMap<K, V> currentMap = stack.pop();
            if( currentMap != that ) {
                list.add( currentMap );
            }

            List<MultiScopeMap<K, V> > parents = currentMap.getParents();
            if( parents != null ) {
                for ( MultiScopeMap<K, V> parent : parents ) {
                    stack.push( parent );
                }
            }
        }
    }

    public static <K, V> void groupByNodes( UniScopeMap<K, V > that, List<ScopeMap<K, V> > list ) {
        UniScopeMap<K, V > p = that.parent();
        while ( p != null ) {
            list.add( p );
            p = p.parent();
        }
    }
}
