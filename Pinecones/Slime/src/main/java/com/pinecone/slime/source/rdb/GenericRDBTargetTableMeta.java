package com.pinecone.slime.source.rdb;

import com.pinecone.slime.source.ArchQueryScopeMeta;
import com.pinecone.slime.source.ResultConverter;

import java.util.Set;
import java.util.TreeSet;

public class GenericRDBTargetTableMeta extends ArchQueryScopeMeta implements RDBTargetTableMeta {
    private RDBQuerierDataManipulator            mDataManipulator;

    public <K, V > GenericRDBTargetTableMeta( String tableName, String primaryKey, String indexKey, Class<?> valueType, RDBQuerierDataManipulator<K, V > manipulator, Set<String > valueMetaKeys ) {
        super( tableName, primaryKey, indexKey, valueType, valueMetaKeys );
        this.mDataManipulator = manipulator;
    }

    public <K, V > GenericRDBTargetTableMeta( String tableName, String indexKey, Class<?> valueType, RDBQuerierDataManipulator<K, V > manipulator, Set<String > valueMetaKeys ) {
        this( tableName, indexKey, indexKey, valueType, manipulator, valueMetaKeys );
    }

    public <K, V > GenericRDBTargetTableMeta( String tableName, String indexKey, Class<?> valueType, RDBQuerierDataManipulator<K, V > manipulator ) {
        this( tableName, indexKey, valueType, manipulator, new TreeSet<>() );
    }

    public GenericRDBTargetTableMeta( String tableName, String indexKey, Class<?> valueType ) {
        this( tableName, indexKey, valueType, null );
    }

    @Override
    public RDBTargetTableMeta setScopeNS( String namespace ) {
        super.setScopeNS( namespace );
        return this;
    }

    @Override
    public String getTableName() {
        return this.getScopeNS();
    }

    @Override
    public RDBTargetTableMeta setTableName( String tableName ) {
        return this.setScopeNS( tableName );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <K, V > RDBQuerierDataManipulator<K, V > getDataManipulator() {
        return this.mDataManipulator;
    }

    @Override
    public <K, V >RDBTargetTableMeta setDataManipulator( RDBQuerierDataManipulator<K, V > manipulator ){
        this.mDataManipulator = manipulator;
        return this;
    }

    @Override
    public RDBTargetTableMeta setPrimaryKey( String primaryKey ) {
        super.setPrimaryKey( primaryKey );
        return this;
    }

    @Override
    public RDBTargetTableMeta setIndexKey( String indexKey ) {
        super.setIndexKey( indexKey );
        return this;
    }

    @Override
    public RDBTargetTableMeta setValueType( Class<?> valueType ) {
        super.setValueType( valueType );
        return this;
    }

    @Override
    public RDBTargetTableMeta setValueMetaKeys( Set<String > keys ){
        super.setValueMetaKeys( keys );
        return this;
    }

    @Override
    public RDBTargetTableMeta addValueMetaKey( String key ) {
        super.addValueMetaKey( key );
        return this;
    }

    @Override
    public RDBTargetTableMeta removeValueMetaKey( String key ) {
        super.removeValueMetaKey( key );
        return this;
    }

    @Override
    public <V > RDBTargetTableMeta setResultConverter( ResultConverter<V > converter ) {
        super.setResultConverter( converter );
        return this;
    }

    @Override
    public GenericRDBTargetTableMeta clone() {
        return (GenericRDBTargetTableMeta) super.clone();  // Refers inner pointer.
    }

}
