package com.pinecone.framework.unit;

import com.pinecone.framework.system.prototype.PineUnit;

import java.util.Map;

public interface Mapnut<K, V > extends PineUnit, Map<K, V > {
    // WARNING, Modified outside will provokes unpredictable results. [ Readonly for performance purpose, in principle ]
    // Java has not the const function, this inconvenient...
    // Equals `Map::Entry<K, V > getEntryByKey( Object compatibleKey )`
    Map.Entry<K, V > getEntryByKey( Object compatibleKey ); // Jesus christ... Even it is not full-safe outside, but we need this!

    // Equals `const Map::Entry<K, V > getEntryByKey( Object compatibleKey ) const`
    Map.Entry<K, V > getEntryCopyByKey( Object compatibleKey ); // Ah, this one is more safer.

    default long megaSize(){
        return this.size();
    }
}
