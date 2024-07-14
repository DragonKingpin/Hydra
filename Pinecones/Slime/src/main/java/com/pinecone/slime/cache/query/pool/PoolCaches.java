package com.pinecone.slime.cache.query.pool;

import com.pinecone.slime.cache.query.RangedDictCachePage;
import com.pinecone.slime.unitization.PartialRange;

import java.util.Map;

public final class PoolCaches {
    public static <IK extends Comparable<IK >, V > long countPoolSize( Map<PartialRange<IK >, RangedDictCachePage<V >> pool ) {
        long n = 0;
        for( Map.Entry<PartialRange<IK >, RangedDictCachePage<V >> kv : pool.entrySet() ) {
            n += kv.getValue().size();
        }
        return n;
    }
}
