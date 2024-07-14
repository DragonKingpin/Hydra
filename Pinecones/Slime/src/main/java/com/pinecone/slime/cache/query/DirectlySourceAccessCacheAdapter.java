package com.pinecone.slime.cache.query;

public class DirectlySourceAccessCacheAdapter<V > extends ArchCountDictCache<V > implements UniformCountSelfLoadingDictCache<V > {
    private SourceRetriever<V > mSourceRetriever;

    public DirectlySourceAccessCacheAdapter( SourceRetriever<V > retriever ) {
        this.mSourceRetriever = retriever;
    }

    @Override
    protected V missKey( Object key ) {
        this.recordMiss();
        return this.mSourceRetriever.retrieve( key );
    }

    @Override
    public boolean implicatesKey( Object key ) {
        return this.mSourceRetriever.countsKey( key ) > 0;
    }

    @Override
    public long capacity() {
        return 0;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public V get( Object key ) {
        this.recordAccess();
        return this.missKey( key );
    }

    @Override
    public V erase( Object key ) {
        return null; // Do nothing.
    }

    @Override
    public boolean existsKey( Object key ) {
        return this.implicatesKey( key );
    }

    @Override
    public void clear() {
        // Do nothing.
    }

    @Override
    public SourceRetriever<V > getSourceRetriever() {
        return this.mSourceRetriever;
    }
}
