package com.pinecone.slime.cache.query;

public interface UniformCountDictCache<V > extends UniformDictCache<V > {
    long getMisses();

    long getAccesses();

    default double getHitRate() {
        double acc = (double)this.getAccesses();
        return 1 - (double) this.getMisses() / acc;
    }
}
