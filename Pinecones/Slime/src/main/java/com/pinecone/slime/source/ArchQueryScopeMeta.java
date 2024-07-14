package com.pinecone.slime.source;

import java.util.Set;

public abstract class ArchQueryScopeMeta implements UniformQueryScopeMeta {
    private String                               mszScopeName;
    private String                               mszPrimaryKey;
    private String                               mszIndexKey;
    private Class<? >                            mValueType;
    private Set<String >                         mValueMetaKeys;
    private ResultConverter                      mResultConverter;

    protected <K, V > ArchQueryScopeMeta( String namespace, String primaryKey, String indexKey, Class<?> valueType, Set<String > valueMetaKeys ) {
        this.mszScopeName     = namespace;
        this.mszPrimaryKey    = primaryKey;
        this.mszIndexKey      = indexKey;
        this.mValueType       = valueType;
        this.mValueMetaKeys   = valueMetaKeys;
    }

    @Override
    public String getScopeNS() {
        return this.mszScopeName;
    }

    @Override
    public UniformQueryScopeMeta setScopeNS( String namespace ) {
        this.mszScopeName = namespace;
        return this;
    }

    @Override
    public String getPrimaryKey() {
        return this.mszPrimaryKey;
    }

    @Override
    public UniformQueryScopeMeta setPrimaryKey( String primaryKey ) {
        this.mszPrimaryKey = primaryKey;
        return this;
    }

    @Override
    public String getIndexKey() {
        return this.mszIndexKey;
    }

    @Override
    public UniformQueryScopeMeta setIndexKey( String indexKey ) {
        this.mszIndexKey = indexKey;
        return this;
    }

    @Override
    public Class<?> getValueType() {
        return this.mValueType;
    }

    @Override
    public UniformQueryScopeMeta setValueType( Class<?> valueType ) {
        this.mValueType = valueType;
        return this;
    }

    @Override
    public Set<String > getValueMetaKeys(){
        return this.mValueMetaKeys;
    }

    @Override
    public UniformQueryScopeMeta setValueMetaKeys( Set<String > keys ){
        this.mValueMetaKeys = keys;
        return this;
    }

    @Override
    public UniformQueryScopeMeta addValueMetaKey( String key ) {
        this.getValueMetaKeys().add( key );
        return this;
    }

    @Override
    public UniformQueryScopeMeta removeValueMetaKey( String key ) {
        this.getValueMetaKeys().remove( key );
        return this;
    }

    @Override
    public UniformQueryScopeMeta clone() {
        try {
            return (UniformQueryScopeMeta) super.clone();  // Refers inner pointer.
        }
        catch ( CloneNotSupportedException e ) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <V > ResultConverter<V > getResultConverter() {
        return this.mResultConverter;
    }

    @Override
    public <V > UniformQueryScopeMeta setResultConverter( ResultConverter<V > converter ) {
        this.mResultConverter = converter;
        return this;
    }
}
