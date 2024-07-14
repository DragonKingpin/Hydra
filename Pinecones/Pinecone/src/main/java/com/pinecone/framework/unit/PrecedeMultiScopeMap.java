package com.pinecone.framework.unit;

import java.util.Map;

public interface PrecedeMultiScopeMap<K, V > extends MultiScopeMap<K, V > {
    MultiScopeMap<K, V>            getPrecedeScope();

    MultiScopeMap<K, V >           setPrecedeScope  ( MultiScopeMap<K, V > that );

    default MultiScopeMap<K, V >   setPrecedeScope  ( Map<K, V > that ) {
        return this.setPrecedeScope( new MultiScopeMaptron<>( that ) );
    }

    void                           onlyOverrideFamilyTo ( Map<K, V > neo ) ;
}
