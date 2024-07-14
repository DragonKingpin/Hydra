package com.pinecone.slime.cache.query;

import com.pinecone.slime.chunk.Page;

public interface DictCachePage<V > extends Page, UniformDictCache<V > {
    @Override
    default long size(){
        return this.elementSize();
    }
}
