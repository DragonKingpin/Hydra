package com.pinecone.slime.source.indexable;

import com.pinecone.slime.source.ArchQueryScopeMeta;
import com.pinecone.slime.source.ResultConverter;

import java.util.Set;
import java.util.TreeSet;

public class GenericIndexableTargetScopeMeta extends ArchQueryScopeMeta implements IndexableTargetScopeMeta {
    private IndexableDataManipulator            mDataManipulator;

    public <K, V > GenericIndexableTargetScopeMeta( String scopeName, String primaryKey, String indexKey, Class<?> valueType, IndexableDataManipulator<K, V > manipulator, Set<String > valueMetaKeys ) {
        super( scopeName, primaryKey, indexKey, valueType, valueMetaKeys );
        this.mDataManipulator = manipulator;
    }

    public <K, V > GenericIndexableTargetScopeMeta( String scopeName, String indexKey, Class<?> valueType, IndexableDataManipulator<K, V > manipulator, Set<String > valueMetaKeys ) {
        this( scopeName, indexKey, indexKey, valueType, manipulator, valueMetaKeys );
    }

    public <K, V > GenericIndexableTargetScopeMeta( String scopeName, String indexKey, Class<?> valueType, IndexableDataManipulator<K, V > manipulator ) {
        this( scopeName, indexKey, valueType, manipulator, new TreeSet<>() );
    }

    public GenericIndexableTargetScopeMeta( String scopeName, String indexKey, Class<?> valueType ) {
        this( scopeName, indexKey, valueType, null );
    }

    @Override
    public IndexableTargetScopeMeta setScopeNS( String namespace ) {
        super.setScopeNS( namespace );
        return this;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <K, V > IndexableDataManipulator<K, V > getDataManipulator() {
        return this.mDataManipulator;
    }

    @Override
    public <K, V >IndexableTargetScopeMeta setDataManipulator( IndexableDataManipulator<K, V > manipulator ){
        this.mDataManipulator = manipulator;
        return this;
    }

    @Override
    public IndexableTargetScopeMeta setPrimaryKey( String primaryKey ) {
        super.setPrimaryKey( primaryKey );
        return this;
    }

    @Override
    public IndexableTargetScopeMeta setIndexKey( String indexKey ) {
        super.setIndexKey( indexKey );
        return this;
    }

    @Override
    public IndexableTargetScopeMeta setValueType( Class<?> valueType ) {
        super.setValueType( valueType );
        return this;
    }

    @Override
    public IndexableTargetScopeMeta setValueMetaKeys( Set<String > keys ){
        super.setValueMetaKeys( keys );
        return this;
    }

    @Override
    public IndexableTargetScopeMeta addValueMetaKey( String key ) {
        super.addValueMetaKey( key );
        return this;
    }

    @Override
    public IndexableTargetScopeMeta removeValueMetaKey( String key ) {
        super.removeValueMetaKey( key );
        return this;
    }

    @Override
    public <V > IndexableTargetScopeMeta setResultConverter( ResultConverter<V > converter ) {
        super.setResultConverter( converter );
        return this;
    }

    @Override
    public GenericIndexableTargetScopeMeta clone() {
        return (GenericIndexableTargetScopeMeta) super.clone();  // Refers inner pointer.
    }

}
