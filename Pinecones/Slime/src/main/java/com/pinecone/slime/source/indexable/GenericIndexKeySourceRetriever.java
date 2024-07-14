package com.pinecone.slime.source.indexable;

import com.pinecone.slime.cache.query.SourceRetriever;

public class GenericIndexKeySourceRetriever<K, V >  implements SourceRetriever<V > {
    private   IndexableDataManipulator<K, V >   mManipulator;
    protected IndexableTargetScopeMeta          mIndexMeta;

    public GenericIndexKeySourceRetriever( IndexableTargetScopeMeta meta ) {
        this.mIndexMeta      = meta;
        this.mManipulator    = (IndexableDataManipulator<K, V >) meta.<K, V >getDataManipulator();
    }

    @Override
    public V retrieve( Object key ) {
        return this.mManipulator.selectByKey( this.mIndexMeta, key );
    }

    @Override
    public long countsKey( Object key ) {
        return this.mManipulator.countsByNS( this.mIndexMeta, this.mIndexMeta.getIndexKey(), key );
    }
}