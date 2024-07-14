package com.pinecone.framework.unit.top;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;

public interface TopmostSelector<K, V > extends Pinenut, Comparator<K > {
    Map.Entry<K, V > getMostEntry( NavigableMap<K,V > map );

    // Selecting candidate if it is meets qualification.
    default boolean selects ( Map.Entry<K, V > most, Map.Entry<K, V > candidate ) {
        return this.selects( most, candidate.getKey() );
    }

    boolean selects ( Map.Entry<K, V > most, K candidateKey );

    @Override
    @SuppressWarnings("unchecked")
    default int compare( Object o1, Object o2 ) {
        return ( (Comparable)o1 ).compareTo( o2 );
    }


    // Selecting greatest top-N elements
    static <K, V > TopmostSelector<K, V > newGenericGreatestSelector( boolean bInsertDirectly ) {
        return new TopmostSelector<>() {
            @Override
            public Map.Entry<K, V > getMostEntry( NavigableMap<K, V > map ) {
                return map.firstEntry();
            }

            @Override
            @SuppressWarnings("unchecked")
            public boolean selects(  Map.Entry<K, V > most, K candidateKey ) {
                if( bInsertDirectly ) {
                    return true;
                }
                return ( (Comparable<K >)most.getKey() ).compareTo( candidateKey ) < 0; // most < candidate
            }
        };
    }

    // Selecting smallest top-N elements
    static <K, V > TopmostSelector<K, V > newGenericSmallestSelector( boolean bInsertDirectly ) {
        return new TopmostSelector<>() {
            @Override
            public Map.Entry<K, V > getMostEntry( NavigableMap<K, V > map ) {
                return map.lastEntry();
            }

            @Override
            @SuppressWarnings("unchecked")
            public boolean selects(  Map.Entry<K, V > most, K candidateKey ) {
                if( bInsertDirectly ) {
                    return true;
                }
                return ( (Comparable<K >)most.getKey() ).compareTo( candidateKey ) > 0; // most > candidate
            }
        };
    }
}
