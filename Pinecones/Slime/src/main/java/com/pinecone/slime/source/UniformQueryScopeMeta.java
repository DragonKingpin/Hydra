package com.pinecone.slime.source;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.Set;

public interface UniformQueryScopeMeta extends Pinenut, Cloneable {
    String getScopeNS();
    UniformQueryScopeMeta setScopeNS( String namespace );

    String getPrimaryKey();
    UniformQueryScopeMeta setPrimaryKey( String primaryKey );

    String getIndexKey();
    UniformQueryScopeMeta setIndexKey( String indexKey );

    Class<?> getValueType();
    UniformQueryScopeMeta setValueType( Class<?> valueType );


    Set<String > getValueMetaKeys();
    UniformQueryScopeMeta setValueMetaKeys( Set<String > keys );
    UniformQueryScopeMeta addValueMetaKey( String key );
    UniformQueryScopeMeta removeValueMetaKey( String key );

    <V > ResultConverter<V > getResultConverter();
    <V > UniformQueryScopeMeta setResultConverter( ResultConverter<V > converter );
}
