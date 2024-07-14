package com.pinecone.framework.unit.affinity;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.List;
import java.util.Map;

public interface ObjectOverrider<K, V > extends Pinenut {

    void override       ( Object instance, Object prototype, boolean bRecursive ) ;

    void overrideObject ( Map<K, V> instance, Map<K, V> parentScope, boolean bRecursive ) ;

    void overrideList   ( List<V> instanceList, List<V> templateList, boolean bRecursive ) ;

}
