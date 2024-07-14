package com.pinecone.slime.cache.query;

public abstract class ArchCountDictCache<V > implements UniformCountDictCache<V > {
    protected long mnMisses;
    protected long mnAccesses;

    protected ArchCountDictCache(){
        this.mnMisses   = 0;
        this.mnAccesses = 0;
    }

    protected void afterKeyVisited( Object key ) {
        this.recordAccess();
    }

    protected abstract V missKey( Object key ) ;

    protected void recordMiss() {
        ++this.mnMisses;
    }

    protected void recordAccess() {
        ++this.mnAccesses;
    }

    @Override
    public long getMisses() {
        return this.mnMisses;
    }

    @Override
    public long getAccesses() {
        return this.mnAccesses;
    }
}
