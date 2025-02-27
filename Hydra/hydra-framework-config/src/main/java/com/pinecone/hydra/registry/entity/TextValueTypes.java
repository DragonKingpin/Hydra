package com.pinecone.hydra.registry.entity;

public final class TextValueTypes {
    public final static String STRING_TYPE_NAME      = PropertyTypes.ELEMENT_STRING_TYPE_NAME;
    public final static String INT64_TYPE_NAME       = PropertyTypes.ELEMENT_INT64_TYPE_NAME;
    public final static String INT32_TYPE_NAME       = PropertyTypes.ELEMENT_INT32_TYPE_NAME;
    public final static String FLOAT32_TYPE_NAME     = PropertyTypes.ELEMENT_FLOAT32_TYPE_NAME;
    public final static String FLOAT64_TYPE_NAME     = PropertyTypes.ELEMENT_FLOAT64_TYPE_NAME;
    public final static String BOOLEAN_TYPE_NAME     = PropertyTypes.ELEMENT_BOOLEAN_TYPE_NAME;
    public final static String NULL_TYPE_NAME        = PropertyTypes.ELEMENT_NULL_TYPE_NAME;

    public final static String JSONOBJECT_TYPE_NAME  = PropertyTypes.ELEMENT_JSONOBJECT_TYPE_NAME;
    public final static String JSONARRAY_TYPE_NAME   = PropertyTypes.ELEMENT_JSONARRAY_TYPE_NAME;

    public final static String YAML_TYPE_NAME        = "Yaml";
    public final static String XML_TYPE_NAME         = "XML";
    public final static String INI_TYPE_NAME         = "INI";


    public static String queryType( Object val ) {
        return PropertyTypes.queryType( val );
    }

    public static Object queryValue( String val, String type ) {
        if( val == null ) {
            return null;
        }

        Object ret = PropertyTypes.queryValue( val, type );
        if( ret != null ) {
            return ret;
        }

        return val;
    }

    public static String queryRecognizedType( String type ) {
        String ret = PropertyTypes.queryRecognizedType( type );
        if( ret == null ) {
            switch ( type ) {
                case TextValueTypes.YAML_TYPE_NAME: {
                    return TextValueTypes.YAML_TYPE_NAME;
                }
                case TextValueTypes.XML_TYPE_NAME: {
                    return TextValueTypes.XML_TYPE_NAME;
                }
                case TextValueTypes.INI_TYPE_NAME: {
                    return TextValueTypes.INI_TYPE_NAME;
                }
            }
        }
        else {
            return ret;
        }

        return null;
    }

    public static boolean isJSON( Object val ) {
        return PropertyTypes.isJSON( val );
    }
}
