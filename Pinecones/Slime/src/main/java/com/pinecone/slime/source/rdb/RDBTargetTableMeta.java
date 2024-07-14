package com.pinecone.slime.source.rdb;

import com.pinecone.slime.source.ResultConverter;
import com.pinecone.slime.source.UniformQueryScopeMeta;

import java.util.Set;

public interface RDBTargetTableMeta extends UniformQueryScopeMeta {
    @Override
    default String getScopeNS() {
        return this.getTableName();
    }

    @Override
    default UniformQueryScopeMeta setScopeNS( String namespace ) {
        return this.setTableName( namespace );
    }

    String getTableName();
    RDBTargetTableMeta setTableName( String tableName );

    @Override
    RDBTargetTableMeta setPrimaryKey( String primaryKey );

    @Override
    RDBTargetTableMeta setIndexKey( String indexKey );

    @Override
    RDBTargetTableMeta setValueType( Class<?> valueType );

    <K, V >RDBQuerierDataManipulator<K, V > getDataManipulator();
    <K, V >RDBTargetTableMeta setDataManipulator( RDBQuerierDataManipulator<K, V > manipulator );

    /**
     * ValueMetaKeys
     * if set is empty => SELECT * FROM       => map / bean
     * if set has one  => SELECT set[0] FROM => map / bean / primitive
     * if set has more => SELECT ...set FROM => map / bean
     */
    @Override
    Set<String > getValueMetaKeys();
    @Override
    RDBTargetTableMeta setValueMetaKeys( Set<String > keys );
    @Override
    RDBTargetTableMeta addValueMetaKey( String key );
    @Override
    RDBTargetTableMeta removeValueMetaKey( String key );

    @Override
    <V > RDBTargetTableMeta setResultConverter( ResultConverter<V > converter );
}