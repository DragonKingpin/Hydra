package com.pinecone.framework.unit.affinity;

import java.util.List;
import java.util.Map;

public class RecursiveUnitOverrider<K, V> implements ObjectOverrider<K, V> {
    public RecursiveUnitOverrider() { }

    @SuppressWarnings( "unchecked" )
    public void override       ( Object instance, Object prototype, boolean bRecursive ) {
        if ( instance != null && prototype != null ) {
            if ( instance instanceof Map && prototype instanceof Map ) {
                this.overrideObject( (Map<K, V>) instance, (Map<K, V>) prototype, bRecursive );
            }
            else if ( instance instanceof List && prototype instanceof List ) {
                this.overrideList( (List<V>) instance, (List<V>) prototype, bRecursive );
            }
        }
    }

    public void overrideObject ( Map<K, V> instance, Map<K, V> parentScope, boolean bRecursive ) {
        for ( Map.Entry<K, V > kv : parentScope.entrySet() ) {
            K key = kv.getKey();
            V templateValue = kv.getValue();

            if ( !instance.containsKey( key ) ) {
                instance.put( key, templateValue );
            }
            else {
                Object instanceValue = instance.get( key );
                this.override( instanceValue, templateValue, bRecursive );
            }
        }
    }

    public void overrideList   ( List<V> instanceList, List<V> templateList, boolean bRecursive ) {
        for ( int i = 0; i < templateList.size(); ++i ) {
            V templateElement = templateList.get( i );

            if ( i < instanceList.size() ) {
                V instanceElement = instanceList.get( i );

                this.override( instanceElement, templateElement, bRecursive );
            }
            else {
                instanceList.add( templateElement );
            }
        }
    }

}
