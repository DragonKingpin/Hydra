package com.pinecone.slime.source.rdb;

import com.pinecone.slime.cache.query.SourceRetriever;

public class GenericSingleKeySourceRetriever<K, V >  implements SourceRetriever<V > {
    private   RangedRDBQuerierDataManipulator<K, V > mDataMapper;
    protected RDBTargetTableMeta                     mTableMeta;

    public GenericSingleKeySourceRetriever( RDBTargetTableMeta tableMeta ) {
        this.mTableMeta     = tableMeta;
        this.mDataMapper    = (RangedRDBQuerierDataManipulator<K, V >) tableMeta.<K, V >getDataManipulator();
    }

    @Override
    public V retrieve( Object key ) {
        return this.mDataMapper.selectByKey( this.mTableMeta, key );
    }

    @Override
    public long countsKey( Object key ) {
        return this.mDataMapper.countsByColumn( this.mTableMeta, this.mTableMeta.getIndexKey(), key );
    }
}
