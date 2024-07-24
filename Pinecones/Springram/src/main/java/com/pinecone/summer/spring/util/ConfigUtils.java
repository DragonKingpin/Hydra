package com.pinecone.summer.spring.util;

import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.unit.Units;
import com.pinecone.framework.unit.tabulate.FamilyEntryNameEncoder;
import com.pinecone.framework.unit.tabulate.GenericNamespaceFamilyEntryNameEncoder;
import com.pinecone.framework.unit.tabulate.RecursiveFamilyIterator;
import com.pinecone.framework.unit.tabulate.UnitFamilyNode;


import java.util.Collection;
import java.util.Map;

public final class ConfigUtils {
    /**
     * Convert JSON formatted or recursion map to spring-properties map.
     * So spring can using json or json5.
     * e.g. { server : { port : 1234 } } => { server.port : 1234 }.
     * @return Spring Properties Map
     */
    public static Map<String, Object > recursionMapToPropertiesMap( Map<String, Object > recursionMap ){
        RecursiveFamilyIterator<Object > iterator = new RecursiveFamilyIterator<>( recursionMap, true );
        FamilyEntryNameEncoder entryNameEncoder = new GenericNamespaceFamilyEntryNameEncoder( ".", true );
        Map<String, Object > neo = Units.spawnExtendParent( recursionMap );

        while( iterator.hasNext() ) {
            UnitFamilyNode<Object, Object > node = iterator.next();

            String k = entryNameEncoder.encode( node );
            k = k.substring( 1 ); // Skip '.'
            neo.put( k, node.getEntry().getValue() );
        }

        return neo;
    }
}
