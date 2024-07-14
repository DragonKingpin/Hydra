package com.pinecone.slime.source.indexable;

import com.pinecone.slime.source.ResultConverter;
import com.pinecone.slime.source.UniformQueryScopeMeta;

import java.util.Set;

public interface IndexableTargetScopeMeta extends UniformQueryScopeMeta {
    @Override
    IndexableTargetScopeMeta setPrimaryKey( String primaryKey );

    @Override
    IndexableTargetScopeMeta setIndexKey( String indexKey );

    @Override
    IndexableTargetScopeMeta setValueType( Class<?> valueType );

    <K, V >IndexableDataManipulator<K, V > getDataManipulator();
    <K, V >IndexableTargetScopeMeta setDataManipulator( IndexableDataManipulator<K, V > manipulator );

    @Override
    Set<String > getValueMetaKeys();
    @Override
    IndexableTargetScopeMeta setValueMetaKeys( Set<String > keys );
    @Override
    IndexableTargetScopeMeta addValueMetaKey( String key );
    @Override
    IndexableTargetScopeMeta removeValueMetaKey( String key );

    @Override
    <V > IndexableTargetScopeMeta setResultConverter( ResultConverter<V > converter );
}
