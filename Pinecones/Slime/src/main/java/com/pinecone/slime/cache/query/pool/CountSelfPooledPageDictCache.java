package com.pinecone.slime.cache.query.pool;

import com.pinecone.slime.cache.query.UniformCountSelfLoadingDictCache;

public interface CountSelfPooledPageDictCache<V > extends UniformCountSelfLoadingDictCache<V >, PooledPageDictCache<V > {
}
