package com.pinecone.slime.cache.query;

import com.pinecone.framework.unit.Dictium;

public interface LocalDictCachePage<V > extends CountDictCachePage<V > {
    Dictium<V > getDictium();
}
