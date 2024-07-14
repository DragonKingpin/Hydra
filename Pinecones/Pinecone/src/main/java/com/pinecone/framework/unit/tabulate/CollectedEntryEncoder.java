package com.pinecone.framework.unit.tabulate;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public interface CollectedEntryEncoder<V > extends Pinenut {
    Collection<Map.Entry<?, V > > encode();

    // To single layer map.
    Map<?, V > regress( Class<? extends Map > stereotypedClass );

    default Map<?, V > regress() {
        return this.regress( LinkedHashMap.class );
    }
}
