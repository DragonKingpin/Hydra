package com.pinecone.hydra.registry.entity;

import java.util.Date;

import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONArraytron;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;

public final class PropertyTypes {
    public final static String ELEMENT_STRING_TYPE_NAME      = "String";
    public final static String ELEMENT_INT64_TYPE_NAME       = "int64";
    public final static String ELEMENT_INT32_TYPE_NAME       = "int32";
    public final static String ELEMENT_FLOAT32_TYPE_NAME     = "float32";
    public final static String ELEMENT_FLOAT64_TYPE_NAME     = "float64";
    public final static String ELEMENT_BOOLEAN_TYPE_NAME     = "bool";
    public final static String ELEMENT_NULL_TYPE_NAME        = "Null";

    public final static String ELEMENT_JSONOBJECT_TYPE_NAME  = "JSONObject";
    public final static String ELEMENT_JSONARRAY_TYPE_NAME   = "JSONArray";

    public static String queryType( Object val ) {
        String type = PropertyTypes.ELEMENT_STRING_TYPE_NAME;
        if( val == null ) {
            type = PropertyTypes.ELEMENT_NULL_TYPE_NAME;
        }
        else if( val instanceof JSONObject ) {
            type = PropertyTypes.ELEMENT_JSONOBJECT_TYPE_NAME;
        }
        else if( val instanceof JSONArray ) {
            type = PropertyTypes.ELEMENT_JSONARRAY_TYPE_NAME;
        }
        else if( val instanceof Byte || val instanceof Short || val instanceof Integer ) {
            type = PropertyTypes.ELEMENT_INT32_TYPE_NAME;
        }
        else if( val instanceof Long ) {
            type = PropertyTypes.ELEMENT_INT64_TYPE_NAME;
        }
        else if( val instanceof Float ) {
            type = PropertyTypes.ELEMENT_FLOAT32_TYPE_NAME;
        }
        else if( val instanceof Double ) {
            type = PropertyTypes.ELEMENT_FLOAT64_TYPE_NAME;
        }
        else if( val instanceof Boolean ) {
            type = PropertyTypes.ELEMENT_BOOLEAN_TYPE_NAME;
        }

        return type;
    }

    public static Object queryValue( String val, String type ) {
        switch ( type ) {
            case PropertyTypes.ELEMENT_NULL_TYPE_NAME: {
                return null;
            }
            case PropertyTypes.ELEMENT_STRING_TYPE_NAME :{
                return val;
            }
            case PropertyTypes.ELEMENT_JSONOBJECT_TYPE_NAME :{
                return new JSONMaptron( val );
            }
            case PropertyTypes.ELEMENT_JSONARRAY_TYPE_NAME :{
                return new JSONArraytron( val );
            }
            case PropertyTypes.ELEMENT_INT32_TYPE_NAME :{
                return Integer.parseInt( val );
            }
            case PropertyTypes.ELEMENT_INT64_TYPE_NAME :{
                return Long.parseLong( val );
            }
            case PropertyTypes.ELEMENT_FLOAT32_TYPE_NAME :{
                return Float.parseFloat( val );
            }
            case PropertyTypes.ELEMENT_FLOAT64_TYPE_NAME :{
                return Double.parseDouble( val );
            }
            case PropertyTypes.ELEMENT_BOOLEAN_TYPE_NAME :{
                return Boolean.parseBoolean( val );
            }
        }
        return null;
    }

    public static String queryRecognizedType( String type ) {
        switch ( type ) {
            case PropertyTypes.ELEMENT_NULL_TYPE_NAME: {
                return PropertyTypes.ELEMENT_NULL_TYPE_NAME;
            }
            case PropertyTypes.ELEMENT_STRING_TYPE_NAME :{
                return PropertyTypes.ELEMENT_STRING_TYPE_NAME;
            }
            case PropertyTypes.ELEMENT_JSONOBJECT_TYPE_NAME :{
                return PropertyTypes.ELEMENT_JSONOBJECT_TYPE_NAME;
            }
            case PropertyTypes.ELEMENT_JSONARRAY_TYPE_NAME :{
                return PropertyTypes.ELEMENT_JSONARRAY_TYPE_NAME;
            }
            case PropertyTypes.ELEMENT_INT32_TYPE_NAME :{
                return PropertyTypes.ELEMENT_INT32_TYPE_NAME;
            }
            case PropertyTypes.ELEMENT_INT64_TYPE_NAME :{
                return PropertyTypes.ELEMENT_INT64_TYPE_NAME;
            }
            case PropertyTypes.ELEMENT_FLOAT32_TYPE_NAME :{
                return PropertyTypes.ELEMENT_FLOAT32_TYPE_NAME;
            }
            case PropertyTypes.ELEMENT_FLOAT64_TYPE_NAME :{
                return PropertyTypes.ELEMENT_FLOAT64_TYPE_NAME;
            }
            case PropertyTypes.ELEMENT_BOOLEAN_TYPE_NAME :{
                return PropertyTypes.ELEMENT_BOOLEAN_TYPE_NAME;
            }
        }
        return null;
    }

    public static boolean isStringBasedType( String type ) {
        switch ( type ) {
            case PropertyTypes.ELEMENT_STRING_TYPE_NAME :{
                return true;
            }
            case PropertyTypes.ELEMENT_JSONOBJECT_TYPE_NAME :{
                return true;
            }
            case PropertyTypes.ELEMENT_JSONARRAY_TYPE_NAME :{
                return true;
            }
        }

        return false;
    }

    public static boolean isJSON( Object val ) {
        Class<?> type = val.getClass();
        return val instanceof JSONObject || val instanceof JSONArray || (
                type.isPrimitive() ||
                        type == String.class ||
                        Number.class.isAssignableFrom(type) ||
                        type == Boolean.class ||
                        type == Character.class ||
                        type == Date.class ||
                        type.isEnum() ||
                        type == byte[].class
        );
    }
}
