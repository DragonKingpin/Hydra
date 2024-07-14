package com.pinecone.slime.cache.query;

public interface UniformSelfLoadingDictCache<V > extends UniformDictCache<V > {
    // Searching key in cache and data-source.
    // If key is missed in cache, and there will triggers self-loading from data-source.
    boolean implicatesKey( Object key );

    SourceRetriever<V > getSourceRetriever();
}
