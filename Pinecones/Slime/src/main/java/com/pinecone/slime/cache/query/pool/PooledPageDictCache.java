package com.pinecone.slime.cache.query.pool;

import com.pinecone.slime.cache.query.UniformDictCache;

public interface PooledPageDictCache<V > extends UniformDictCache<V > {
    long getPooledPagesCapacity();
}
