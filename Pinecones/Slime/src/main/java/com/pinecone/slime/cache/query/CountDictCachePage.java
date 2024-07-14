package com.pinecone.slime.cache.query;

public interface CountDictCachePage<V > extends DictCachePage<V >, UniformCountDictCache<V > {
    @Override
    default long size(){
        return this.elementSize();
    }
}
