package com.pinecone.hydra.registry.entity;

public class DefaultPropertyConverter implements TypeConverter {
    @Override
    public Object converter( String val, String type ) {
        return PropertyTypes.queryValue( val, type );
    }

    @Override
    public String queryType( Object val ) {
        return PropertyTypes.queryType( val );
    }

    @Override
    public String queryRecognizedType( String type ) {
        return PropertyTypes.queryRecognizedType( type );
    }

    @Override
    public boolean isJSON( Object val ) {
        return PropertyTypes.isJSON( val );
    }
}
