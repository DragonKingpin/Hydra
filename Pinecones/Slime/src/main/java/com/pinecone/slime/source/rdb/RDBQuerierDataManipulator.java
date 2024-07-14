package com.pinecone.slime.source.rdb;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.List;

public interface RDBQuerierDataManipulator<K, V > extends Pinenut {

    long counts                ( RDBTargetTableMeta meta, String szExSafeSQL );

    long countsByColumn        ( RDBTargetTableMeta meta, String szSpecificColumnKeyName, Object key );

    List selectList            ( RDBTargetTableMeta meta, String szExSafeSQL );

    List query                 ( RDBTargetTableMeta meta, String szStatementSQL );

    List<V > queryVal          ( RDBTargetTableMeta meta, String szStatementSQL );

    List selectListByColumn    ( RDBTargetTableMeta meta, String szSpecificColumnKeyName, Object key );

    V    selectByKey           ( RDBTargetTableMeta meta, Object key );

    void insert                ( RDBTargetTableMeta meta, K key, V entity );

    void update                ( RDBTargetTableMeta meta, K key, V entity );

    void deleteByKey           ( RDBTargetTableMeta meta, Object key );

    void truncate              ( RDBTargetTableMeta meta );

    void commit                ();

}