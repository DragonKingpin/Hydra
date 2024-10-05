package com.pinecone.hydra.registry.entity;

public class DefaultTextValueConverter implements TypeConverter {
    @Override
    public Object converter( String val, String type ) {
        return TextValueTypes.queryValue( val, type );
    }

    @Override
    public String queryType( Object val ) {
        return TextValueTypes.queryType( val );
    }

    @Override
    public String queryRecognizedType( String type ) {
        return TextValueTypes.queryRecognizedType( type );
    }

    @Override
    public boolean isJSON( Object val ) {
        return TextValueTypes.isJSON( val );
    }
}
